package com.example.healthcare.service;

import com.example.healthcare.model.dto.DoctorCreateDTO;
import com.example.healthcare.model.dto.UserProfileDTO;
import com.example.healthcare.model.dto.UserRegisterDTO;
import com.example.healthcare.model.entity.User;

public interface UserService {
    User register(UserRegisterDTO dto) throws Exception;
    User login(String username, String password);
    void updateProfile(Long userId, UserProfileDTO dto);

    // Hàm mới: Admin cấp tài khoản Bác sĩ
    User createDoctor(DoctorCreateDTO dto) throws Exception;
}