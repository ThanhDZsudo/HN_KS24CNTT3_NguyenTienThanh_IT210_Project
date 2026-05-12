package com.example.healthcare.controller.patient;

import com.example.healthcare.model.dto.AppointmentRequestDTO;
import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.SpecialtyRepository;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final AppointmentService appointmentService;
    private final SpecialtyRepository specialtyRepo;
    private final UserRepository userRepo;
    private final AppointmentRepository appointmentRepo;

    @GetMapping("/dashboard")
    public String dashboard() { return "patient/patient-dashboard"; }

    @GetMapping("/book-appointment")
    public String showBookForm(Model model) {
        model.addAttribute("specialties", specialtyRepo.findAll());
        model.addAttribute("dto", new AppointmentRequestDTO());
        return "patient/book-appointment";
    }

    @GetMapping("/api/doctors-by-specialty")
    @ResponseBody
    public List<Map<String, Object>> getDoctorsBySpecialty(@RequestParam Long specialtyId) {
        return userRepo.findByRoleAndSpecialtyId(Role.DOCTOR, specialtyId).stream()
                .map(d -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", d.getId());
                    map.put("name", "BS. " + d.getProfile().getFullName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@ModelAttribute("dto") AppointmentRequestDTO dto,
                                  HttpSession session, Model model) {
        boolean hasError = false;

        if (dto.getSpecialtyId() == null) { model.addAttribute("errorSpec", "Vui lòng chọn chuyên khoa!"); hasError = true; }
        if (dto.getDoctorId() == null) { model.addAttribute("errorDoc", "Vui lòng chọn bác sĩ!"); hasError = true; }
        if (dto.getTime() == null) { model.addAttribute("errorTime", "Vui lòng chọn giờ khám!"); hasError = true; }

        if (dto.getDate() == null) {
            model.addAttribute("errorDate", "Vui lòng chọn ngày khám!"); hasError = true;
        } else if (dto.getDate().isBefore(LocalDate.now())) {
            model.addAttribute("errorDate", "Không thể đặt lịch cho ngày đã qua!"); hasError = true;
        }

        if (hasError) {
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "patient/book-appointment";
        }

        try {
            User patient = (User) session.getAttribute("loggedInUser");
            appointmentService.bookAppointment(patient.getId(), dto.getDoctorId(), dto.getSpecialtyId(), dto.getDate(), dto.getTime());
            return "redirect:/patient/medical-history?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "patient/book-appointment";
        }
    }

    @GetMapping("/medical-history")
    public String medicalHistory(HttpSession session, Model model) {
        User patient = (User) session.getAttribute("loggedInUser");
        model.addAttribute("appointments", appointmentRepo.findByPatientIdOrderByAppointmentDateDesc(patient.getId()));
        return "patient/medical-history";
    }

    @GetMapping("/cancel-appointment/{id}")
    public String cancelAppointment(@PathVariable Long id, HttpSession session) {
        User patient = (User) session.getAttribute("loggedInUser");
        try {
            appointmentService.cancelAppointment(id, patient.getId());
            return "redirect:/patient/medical-history?cancel_success";
        } catch (Exception e) {
            return "redirect:/patient/medical-history?cancel_error=" + e.getMessage();
        }
    }
}