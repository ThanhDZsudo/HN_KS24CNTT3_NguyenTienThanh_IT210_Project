package com.example.healthcare.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String prescription;

    // Trạng thái cấp thuốc: PENDING (Chờ phát), DISPENSED (Đã phát)
    private String dispenseStatus;

    private LocalDateTime createdAt = LocalDateTime.now();
}