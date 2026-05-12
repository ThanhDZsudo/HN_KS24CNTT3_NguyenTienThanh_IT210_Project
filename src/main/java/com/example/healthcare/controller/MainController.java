package com.example.healthcare.controller;

import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Chưa đăng nhập thì bắt đăng nhập
        }

        // Chia đường về nhà cho từng ông
        if (user.getRole() == Role.ADMIN) return "redirect:/admin/dashboard";
        if (user.getRole() == Role.DOCTOR) return "redirect:/doctor/dashboard";
        return "redirect:/patient/dashboard";
    }
}