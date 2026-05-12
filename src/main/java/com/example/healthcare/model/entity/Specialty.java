package com.example.healthcare.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specialties")
@Data
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Bắt buộc phải có trường này để tính doanh thu
    private Double price;
}