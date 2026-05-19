package com.example.healthcare.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specialties")
@Data // Đã có lombok thì nó tự sinh getter/setter
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // PHẢI THÊM DÒNG NÀY ĐỂ JAVA ĐỌC ĐƯỢC TIỀN TỪ DATABASE
    private Double price;
}