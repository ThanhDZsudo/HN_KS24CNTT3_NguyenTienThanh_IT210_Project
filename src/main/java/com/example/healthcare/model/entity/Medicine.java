package com.example.healthcare.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medicines")
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String unit;
    private String description;
}