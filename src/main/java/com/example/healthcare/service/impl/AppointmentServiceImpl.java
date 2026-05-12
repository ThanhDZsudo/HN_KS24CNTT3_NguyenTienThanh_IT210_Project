package com.example.healthcare.service.impl;

import com.example.healthcare.model.entity.Appointment;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.SpecialtyRepository;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appRepo;
    private final UserRepository userRepo;
    private final SpecialtyRepository specRepo;

    @Override
    @Transactional
    public void bookAppointment(Long patientId, Long doctorId, Long specialtyId, LocalDate date, LocalTime time) throws Exception {
        if (appRepo.existsByDoctorIdAndAppointmentDateAndAppointmentTime(doctorId, date, time)) {
            throw new Exception("Bác sĩ đã có lịch hẹn vào giờ này!");
        }
        Appointment app = new Appointment();
        app.setPatient(userRepo.findById(patientId).orElseThrow());
        app.setDoctor(userRepo.findById(doctorId).orElseThrow());
        app.setSpecialty(specRepo.findById(specialtyId).orElseThrow());
        app.setAppointmentDate(date);
        app.setAppointmentTime(time);
        app.setStatus("PENDING");
        appRepo.save(app);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId, Long patientId) throws Exception {
        Appointment app = appRepo.findById(appointmentId).orElseThrow();

        if (!app.getPatient().getId().equals(patientId)) {
            throw new Exception("Bạn không có quyền thao tác lịch này!");
        }

        // Logic CORE-09: Kiểm tra thời gian hủy trước 24 giờ
        LocalDateTime appointmentDateTime = LocalDateTime.of(app.getAppointmentDate(), app.getAppointmentTime());
        if (LocalDateTime.now().plusHours(24).isAfter(appointmentDateTime)) {
            throw new Exception("Quá hạn hủy! Chỉ được phép hủy trước giờ khám 24 tiếng.");
        }

        app.setStatus("CANCELLED"); // Đổi trạng thái lịch thành Đã hủy
        appRepo.save(app);
    }
}