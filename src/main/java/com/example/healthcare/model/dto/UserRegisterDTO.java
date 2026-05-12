package com.example.healthcare.model.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String gender;
}