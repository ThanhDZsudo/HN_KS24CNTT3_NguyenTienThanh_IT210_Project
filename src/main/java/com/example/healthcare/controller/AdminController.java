package com.example.healthcare.controller;

import com.example.healthcare.model.dto.DoctorCreateDTO;
import com.example.healthcare.model.dto.RevenueDTO;
import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.repository.AppointmentRepository;
import com.example.healthcare.repository.SpecialtyRepository;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.service.ExaminationService;
import com.example.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final SpecialtyRepository specialtyRepo;
    private final AppointmentRepository appointmentRepo;
    private final ExaminationService examService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/admin-dashboard";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("totalPatients", userRepo.findByRole(Role.PATIENT).size());
        model.addAttribute("totalDoctors", userRepo.findByRole(Role.DOCTOR).size());
        model.addAttribute("totalAppointments", appointmentRepo.count());

        List<RevenueDTO> revenueData = appointmentRepo.getMonthlyRevenue();

        // Lọc bỏ các khoản doanh thu bị NULL
        Double totalRevenue = revenueData.stream()
                .filter(r -> r.getTotal() != null)
                .mapToDouble(RevenueDTO::getTotal)
                .sum();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("revenueData", revenueData);

        model.addAttribute("topDoctors", appointmentRepo.getTopDoctors(PageRequest.of(0, 5)));

        return "admin/admin-statistics";
    }

    // --- QUẢN LÝ BÁC SĨ ---
    @GetMapping("/doctors")
    public String listDoctors(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("doctorPage", userRepo.findByRole(Role.DOCTOR, PageRequest.of(page, 5)));
        return "admin/doctor-manage";
    }

    @GetMapping("/doctor/create")
    public String showCreateDoctorForm(Model model) {
        model.addAttribute("dto", new DoctorCreateDTO());
        model.addAttribute("specialties", specialtyRepo.findAll());
        return "admin/create-doctor";
    }

    @PostMapping("/doctor/create")
    public String createDoctor(@ModelAttribute("dto") DoctorCreateDTO dto, Model model) {
        boolean hasError = false;
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            model.addAttribute("nameError", "Họ và tên không được để trống!"); hasError = true;
        }
        if (dto.getSpecialtyId() == null) {
            model.addAttribute("specialtyError", "Vui lòng chọn chuyên khoa!"); hasError = true;
        }
        if (hasError) {
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "admin/create-doctor";
        }
        try {
            userService.createDoctor(dto);
            return "redirect:/admin/doctors?success=created";
        } catch (Exception e) {
            model.addAttribute("usernameError", e.getMessage());
            model.addAttribute("specialties", specialtyRepo.findAll());
            return "admin/create-doctor";
        }
    }

    @GetMapping("/doctor/toggle-status/{id}")
    public String toggleDoctorStatus(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setEnabled(!user.isEnabled());
        userRepo.save(user);
        return "redirect:/admin/doctors";
    }

    // --- QUẢN LÝ BỆNH NHÂN ---
    @GetMapping("/patients")
    public String listPatients(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("patientPage", userRepo.findByRole(Role.PATIENT, PageRequest.of(page, 5)));
        return "admin/patient-manage";
    }

    @GetMapping("/patients/{id}")
    public String viewPatientDetails(@PathVariable Long id, Model model) {
        User patient = userRepo.findById(id).orElseThrow();
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointmentRepo.findByPatientIdOrderByAppointmentDateDesc(id));
        return "admin/patient-details";
    }

    // --- QUẢN LÝ PHÁT THUỐC ---
    @GetMapping("/prescriptions")
    public String listPrescriptions(Model model) {
        model.addAttribute("pendingRecords", examService.getPendingPrescriptions());
        model.addAttribute("historyRecords", examService.getDispensedPrescriptions());
        return "admin/dispense-manage";
    }

    @GetMapping("/prescriptions/dispense/{id}")
    public String dispenseMedicine(@PathVariable Long id) {
        examService.dispenseMedicine(id);
        return "redirect:/admin/prescriptions?success=dispensed";
    }
}