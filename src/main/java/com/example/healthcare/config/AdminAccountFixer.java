package com.example.healthcare.config;

import com.example.healthcare.model.entity.User;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountFixer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Tìm tài khoản admin trong Database (Thêm .orElse(null) để xử lý Optional)
        User admin = userRepository.findByUsername("admin").orElse(null);

        if (admin != null) {
            // Lấy đúng thuật toán HashUtil của project cậu để băm lại chữ "123456"
            admin.setPassword(HashUtil.hashPassword("123456"));
            userRepository.save(admin);
        }
    }
}