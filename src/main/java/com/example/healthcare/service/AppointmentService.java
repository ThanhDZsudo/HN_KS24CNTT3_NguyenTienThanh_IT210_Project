package com.example.healthcare.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentService {
    void bookAppointment(Long patientId, Long doctorId, Long specialtyId, LocalDate date, LocalTime time) throws Exception;

    // Hàm hủy lịch (CORE-09)
    void cancelAppointment(Long appointmentId, Long patientId) throws Exception;
}