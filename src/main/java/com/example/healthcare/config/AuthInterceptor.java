package com.example.healthcare.config;

import com.example.healthcare.model.entity.User;
import com.example.healthcare.model.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        if (uri.startsWith("/login") || uri.startsWith("/register") || uri.contains(".")) return true;
        if (user == null) { response.sendRedirect("/login"); return false; }
        if (uri.startsWith("/admin") && user.getRole() != Role.ADMIN) { response.sendError(403); return false; }
        if (uri.startsWith("/doctor") && user.getRole() != Role.DOCTOR) { response.sendError(403); return false; }
        return true;
    }
}