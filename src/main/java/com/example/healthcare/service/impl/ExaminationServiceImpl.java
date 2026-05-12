package com.example.healthcare.service.impl;

import com.example.healthcare.model.entity.Appointment;
import com.example.healthcare.model.entity.MedicalRecord;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.MedicalRecordRepository;
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

    @Override
    @Transactional
    public void dispenseMedicine(Long recordId) {
        MedicalRecord record = recordRepo.findById(recordId).orElseThrow();
        record.setDispenseStatus("DISPENSED");
        recordRepo.save(record);
    }
}