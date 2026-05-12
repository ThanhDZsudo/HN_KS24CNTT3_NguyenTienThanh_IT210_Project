package com.example.healthcare.service.impl;

import com.example.healthcare.model.dto.DoctorCreateDTO;
import com.example.healthcare.model.dto.UserProfileDTO;
import com.example.healthcare.model.dto.UserRegisterDTO;
import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.model.entity.UserProfile;
import com.example.healthcare.repository.SpecialtyRepository;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.service.UserService;
import com.example.healthcare.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;

    @Override
    @Transactional
    public User register(UserRegisterDTO dto) throws Exception {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(HashUtil.hashPassword(dto.getPassword()));
        user.setRole(Role.PATIENT);

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getPassword().equals(HashUtil.hashPassword(password))) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UserProfileDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        UserProfile profile = user.getProfile();
        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setDateOfBirth(dto.getDateOfBirth());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User createDoctor(DoctorCreateDTO dto) throws Exception {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(HashUtil.hashPassword(dto.getPassword()));
        user.setRole(Role.DOCTOR);

        // Gán chuyên khoa
        if (dto.getSpecialtyId() != null) {
            user.setSpecialty(specialtyRepository.findById(dto.getSpecialtyId()).orElse(null));
        }

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setEmail(dto.getEmail());
        profile.setPhone(dto.getPhone());
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
    }
}