package com.example.healthcare.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private Long specialtyId;
    private Long doctorId;
    private LocalDate date;
    private LocalTime time;
}