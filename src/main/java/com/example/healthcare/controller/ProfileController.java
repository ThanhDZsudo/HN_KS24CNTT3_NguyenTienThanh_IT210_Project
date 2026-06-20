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

    // 1. HIỂN THỊ TRANG PROFILE - ĐÃ FIX ĐỒNG BỘ TÊN BIẾN VÀ FILE HTML
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findById(loggedInUser.getId()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("profile", currentUser.getProfile());

        // SỬA TẠI ĐÂY: Trả về "profile-view" thay vì "profile"
        return "profile-view";
    }

    // 2. HỨNG DỮ LIỆU CẬP NHẬT TỪ FORM SUBMIT
    @PostMapping("/profile/update")
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

        User currentUser = userRepository.findById(loggedInUser.getId()).orElse(null);

        if (currentUser != null && currentUser.getProfile() != null) {
            currentUser.getProfile().setFullName(fullName);
            currentUser.getProfile().setPhone(phone);
            currentUser.getProfile().setEmail(email);
            currentUser.getProfile().setAddress(address);
            currentUser.getProfile().setGender(gender);
            currentUser.getProfile().setDateOfBirth(dateOfBirth);

            // Lưu dữ liệu cập nhật xuống Database
            userRepository.save(currentUser);

            // Cập nhật lại thông tin user mới vào session cho đồng bộ hệ thống
            session.setAttribute("loggedInUser", currentUser);

            // Bắn thông báo thành công xanh lá cây ra màn hình profile
            redirectAttributes.addFlashAttribute("success", "🎉 Cập nhật hồ sơ cá nhân thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy thông tin hồ sơ!");
        }

        return "redirect:/profile";
    }
}