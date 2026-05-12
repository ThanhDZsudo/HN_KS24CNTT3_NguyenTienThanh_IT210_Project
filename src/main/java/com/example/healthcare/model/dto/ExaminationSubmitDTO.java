package com.example.healthcare.model.dto;

import lombok.Data;

@Data
public class ExaminationSubmitDTO {
    private Long appointmentId;
    private String symptoms;
    private String diagnosis;
    private String prescription; // Nội dung đơn thuốc bác sĩ gõ tay
}