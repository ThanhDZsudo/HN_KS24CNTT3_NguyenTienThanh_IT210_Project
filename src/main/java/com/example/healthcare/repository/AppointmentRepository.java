package com.example.healthcare.repository;

import com.example.healthcare.model.dto.RevenueDTO;
import com.example.healthcare.model.dto.TopDoctorDTO;
import com.example.healthcare.model.entity.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, String status);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(Long doctorId, LocalDate date, LocalTime time);

    // HÀM QUAN TRỌNG: Lấy lịch sử khám của bác sĩ (Mới nhất lên đầu)
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateDescAppointmentTimeDesc(Long doctorId, String status);

    @Query("SELECT a.doctor.profile.fullName AS doctorName, COUNT(a.id) AS appointmentCount " +
            "FROM Appointment a " +
            "GROUP BY a.doctor.id, a.doctor.profile.fullName " +
            "ORDER BY appointmentCount DESC")
    List<TopDoctorDTO> getTopDoctors(Pageable pageable);

    @Query(value = "SELECT MONTH(a.appointment_date) as month, SUM(s.price) as total " +
            "FROM appointments a " +
            "JOIN specialties s ON a.specialty_id = s.id " +
            "WHERE a.status <> 'CANCELLED' " +
            "GROUP BY MONTH(a.appointment_date) " +
            "ORDER BY MONTH(a.appointment_date) ASC",
            nativeQuery = true)
    List<RevenueDTO> getMonthlyRevenue();
}