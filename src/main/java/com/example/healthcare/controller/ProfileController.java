package com.example.healthcare.controller;

import com.example.healthcare.model.entity.User;
import com.example.healthcare.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;

    // 1. HIỂN THỊ TRANG PROFILE (Cái này cậu đang có và chạy OK)
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Chưa đăng nhập thì đuổi về trang login
        }

        // Lấy dữ liệu mới nhất từ Database
        User currentUser = userRepository.findById(loggedInUser.getId()).orElse(null);
        model.addAttribute("user", currentUser);
        return "profile-view";
    }

    // 2. HỨNG DỮ LIỆU CẬP NHẬT (PHẦN CẬU ĐANG THIẾU GÂY RA LỖI)
    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            @RequestParam(value = "dateOfBirth", required = false) LocalDate dateOfBirth,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Lấy User từ DB ra để ghi đè thông tin mới
        User currentUser = userRepository.findById(loggedInUser.getId()).orElse(null);

        if (currentUser != null && currentUser.getProfile() != null) {
            currentUser.getProfile().setFullName(fullName);
            currentUser.getProfile().setPhone(phone);
            currentUser.getProfile().setEmail(email);
            currentUser.getProfile().setAddress(address);
            currentUser.getProfile().setGender(gender);
            currentUser.getProfile().setDateOfBirth(dateOfBirth);

            // Lưu vào DB
            userRepository.save(currentUser);

            // Cập nhật lại session cho đồng bộ
            session.setAttribute("loggedInUser", currentUser);
        }

        // Bắn thông báo xanh lá cây ra màn hình
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "redirect:/profile";
    }
}