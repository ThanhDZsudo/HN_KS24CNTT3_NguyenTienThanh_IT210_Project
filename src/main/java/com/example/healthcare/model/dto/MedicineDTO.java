package com.example.healthcare.model.dto;

import lombok.Data;

@Data
public class MedicineDTO {
    private Long id; // Dùng khi sửa thuốc, khi thêm mới thì để null
    private String name;
    private String unit;
    private String description;
}