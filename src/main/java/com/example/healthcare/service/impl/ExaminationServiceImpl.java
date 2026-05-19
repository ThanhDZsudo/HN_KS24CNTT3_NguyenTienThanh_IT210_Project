package com.example.healthcare.service.impl;

import com.example.healthcare.model.entity.Appointment;
import com.example.healthcare.model.entity.MedicalRecord;
import com.example.healthcare.model.entity.Medicine;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.MedicalRecordRepository;
import com.example.healthcare.repository.MedicineRepository;
import com.example.healthcare.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final AppointmentRepository appRepo;
    private final MedicalRecordRepository recordRepo;

    // 1. THÊM REPO NÀY ĐỂ KẾT NỐI VỚI BẢNG THUỐC
    private final MedicineRepository medicineRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitExamination(Long appointmentId, String symptoms, String diagnosis, String prescription) {
        Appointment app = appRepo.findById(appointmentId).orElseThrow();
        app.setStatus("COMPLETED");
        appRepo.save(app);

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(app);
        record.setSymptoms(symptoms);
        record.setDiagnosis(diagnosis);
        record.setPrescription(prescription);
        record.setDispenseStatus("PENDING");
        recordRepo.save(record);
    }

    @Override
    public List<MedicalRecord> getPendingPrescriptions() {
        return recordRepo.findByDispenseStatus("PENDING");
    }

    @Override
    public List<MedicalRecord> getDispensedPrescriptions() {
        return recordRepo.findByDispenseStatus("DISPENSED");
    }

    // 2. NÂNG CẤP HÀM NÀY ĐỂ VỪA PHÁT THUỐC VỪA TRỪ KHO
    @Override
    @Transactional
    public void dispenseMedicine(Long recordId) {
        MedicalRecord record = recordRepo.findById(recordId).orElseThrow();

        // Đổi trạng thái thành đã phát
        record.setDispenseStatus("DISPENSED");
        recordRepo.save(record);

        // TRỪ SỐ LƯỢNG THUỐC TRONG KHO
        String prescription = record.getPrescription();
        if (prescription != null && !prescription.isEmpty()) {

            // Cắt chuỗi đơn thuốc theo từng dòng (xuống dòng là \n)
            String[] lines = prescription.split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                try {
                    // Dựa vào format: "- Paracetamol 500mg (SL: 10) | HD: Sáng tối"
                    int slStart = line.indexOf("(SL: ");
                    if (slStart != -1) {
                        // Cắt lấy Tên thuốc (Bỏ chữ "- " ở đầu và khoảng trắng ở cuối)
                        String medName = line.substring(2, slStart).trim();

                        // Cắt lấy Số lượng cần trừ
                        int slEnd = line.indexOf(")", slStart);
                        int quantityToDeduct = Integer.parseInt(line.substring(slStart + 5, slEnd));

                        // Tìm thuốc trong kho bằng tên và tiến hành trừ
                        Medicine medicine = medicineRepo.findByName(medName).orElse(null);
                        if (medicine != null) {
                            int newQuantity = medicine.getQuantity() - quantityToDeduct;

                            // Ép về 0 nếu chẳng may số lượng bị âm (tránh lỗi ngớ ngẩn)
                            medicine.setQuantity(Math.max(newQuantity, 0));
                            medicineRepo.save(medicine);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi đọc đơn thuốc để trừ kho tại dòng: " + line);
                }
            }
        }
    }
}