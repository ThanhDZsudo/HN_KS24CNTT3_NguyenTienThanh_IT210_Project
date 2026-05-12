package com.example.healthcare.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserProfileDTO {
    private String fullName;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}