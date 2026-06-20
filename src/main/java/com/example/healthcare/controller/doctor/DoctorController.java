package com.example.healthcare.controller.doctor;

import com.example.healthcare.model.dto.ExaminationSubmitDTO;
import com.example.healthcare.model.entity.Appointment;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.MedicineRepository;
import com.example.healthcare.service.ExaminationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final AppointmentRepository appRepo;
    private final ExaminationService examService;
    private final MedicineRepository medicineRepo;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "doctor/doctor-dashboard";
    }

    @GetMapping("/pending-appointments")
    public String pendingAppointments(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("loggedInUser");
        model.addAttribute("appointments", appRepo.findByDoctorIdAndStatus(doctor.getId(), "PENDING"));
        return "doctor/pending-appointments";
    }

    @GetMapping("/examine/{id}")
    public String showExamineForm(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appRepo.findById(id).orElseThrow());
        model.addAttribute("dto", new ExaminationSubmitDTO());
        model.addAttribute("medicines", medicineRepo.findAll());
        return "doctor/examine-patient";
    }

    @PostMapping("/examine/{id}")
    public String submitExamination(@PathVariable Long id, @ModelAttribute("dto") ExaminationSubmitDTO dto, Model model) {
        boolean hasError = false;

        if (dto.getSymptoms() == null || dto.getSymptoms().trim().isEmpty()) {
            model.addAttribute("errorSymptoms", "Vui lòng nhập triệu chứng lâm sàng!");
            hasError = true;
        }
        if (dto.getDiagnosis() == null || dto.getDiagnosis().trim().isEmpty()) {
            model.addAttribute("errorDiagnosis", "Vui lòng nhập chẩn đoán bệnh!");
            hasError = true;
        }

        if (hasError) {
            // ĐÃ FIX: Nạp lại đầy đủ dữ liệu khi bị lỗi trống trường, tránh lỗi sập 500 Thymeleaf
            model.addAttribute("appointment", appRepo.findById(id).orElseThrow());
            model.addAttribute("medicines", medicineRepo.findAll());
            return "doctor/examine-patient";
        }

        examService.submitExamination(id, dto.getSymptoms(), dto.getDiagnosis(), dto.getPrescription());
        return "redirect:/doctor/pending-appointments?success";
    }

    // ĐÃ FIX LUỒNG: Endpoint tra cứu lịch sử bệnh án cho Bác sĩ không còn bị lỗi 404/500 tĩnh nữa
    @GetMapping("/history")
    public String viewHistory(HttpSession session, Model model) {
        User doctor = (User) session.getAttribute("loggedInUser");
        if (doctor == null) {
            return "redirect:/login";
        }
        List<Appointment> history = appRepo.findByDoctorIdAndStatusOrderByAppointmentDateDescAppointmentTimeDesc(doctor.getId(), "COMPLETED");
        model.addAttribute("history", history);
        return "doctor/doctor-history";
    }
}