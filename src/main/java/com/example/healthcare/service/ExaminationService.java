package com.example.healthcare.service;

import com.example.healthcare.model.entity.MedicalRecord;
import java.util.List;

public interface ExaminationService {
    void submitExamination(Long appointmentId, String symptoms, String diagnosis, String prescription);

    List<MedicalRecord> getPendingPrescriptions();

    // Hàm mới: Lấy lịch sử đã cấp phát
    List<MedicalRecord> getDispensedPrescriptions();

    void dispenseMedicine(Long recordId);
}