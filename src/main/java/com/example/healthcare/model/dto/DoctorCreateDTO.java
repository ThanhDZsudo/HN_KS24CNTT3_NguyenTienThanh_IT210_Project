package com.example.healthcare.model.dto;

import lombok.Data;

@Data
public class DoctorCreateDTO {
    private String fullName;
    private String gender;
    private String email;
    private String phone;

    // ĐÂY NÀY! Bắt buộc phải có dòng này thì mới hết báo đỏ nhé
    private Long specialtyId;

    private String username;
    private String password;
}