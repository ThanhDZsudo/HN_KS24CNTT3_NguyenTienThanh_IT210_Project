package com.example.healthcare.controller;

import com.example.healthcare.model.dto.UserRegisterDTO;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.model.entity.Role;
import com.example.healthcare.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("dto") UserRegisterDTO dto, Model model) {
        boolean hasError = false;

        // 1. Kiểm tra Họ và Tên (Rỗng hoặc độ dài < 6)
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            model.addAttribute("nameError", "Họ và tên không được để trống!");
            hasError = true;
        } else if (dto.getFullName().trim().length() < 6) {
            model.addAttribute("nameError", "Họ và tên phải có ít nhất 6 ký tự!");
            hasError = true;
        }

        // 2. Kiểm tra Giới tính
        if (dto.getGender() == null || dto.getGender().isEmpty()) {
            model.addAttribute("genderError", "Vui lòng chọn giới tính!");
            hasError = true;
        }

        // 3. Kiểm tra Username (Rỗng hoặc độ dài < 3)
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            model.addAttribute("usernameError", "Tên đăng nhập không được để trống!");
            hasError = true;
        } else if (dto.getUsername().trim().length() < 3) {
            model.addAttribute("usernameError", "Tên đăng nhập phải có ít nhất 3 ký tự!");
            hasError = true;
        }

        // 4. Kiểm tra Mật khẩu (Rỗng hoặc độ dài < 6)
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống!");
            hasError = true;
        } else if (dto.getPassword().trim().length() < 6) {
            model.addAttribute("passwordError", "Mật khẩu phải chứa ít nhất 6 ký tự!");
            hasError = true;
        }

        // 5. Kiểm tra Xác nhận mật khẩu
        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
            model.addAttribute("confirmError", "Vui lòng nhập lại mật khẩu!");
            hasError = true;
        } else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("confirmError", "Mật khẩu xác nhận không khớp!");
            hasError = true;
        }

        // Nếu có lỗi, chặn lại và trả về trang đăng ký
        if (hasError) return "register";

        try {
            userService.register(dto);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "password", defaultValue = "") String password,
                              Model model, HttpSession session) {
        boolean hasError = false;

        if (username.trim().isEmpty()) {
            model.addAttribute("usernameError", "Vui lòng nhập tên đăng nhập!");
            hasError = true;
        }
        if (password.trim().isEmpty()) {
            model.addAttribute("passwordError", "Vui lòng nhập mật khẩu!");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("oldUsername", username);
            return "login";
        }

        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);

            // --- ĐIỀU HƯỚNG CHUẨN XÁC DỰA VÀO QUYỀN (ROLE) ---
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard"; // Admin về Dashboard
            } else if (user.getRole() == Role.DOCTOR) {
                return "redirect:/doctor/pending-appointments"; // Bác sĩ về Hàng chờ
            } else {
                return "redirect:patient/dashboard"; // Bệnh nhân về Lịch sử khám
            }
        }

        model.addAttribute("loginError", "Sai tài khoản hoặc mật khẩu!");
        model.addAttribute("oldUsername", username);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}