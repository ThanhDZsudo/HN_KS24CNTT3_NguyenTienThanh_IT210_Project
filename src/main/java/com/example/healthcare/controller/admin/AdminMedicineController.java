package com.example.healthcare.controller.admin;

import com.example.healthcare.model.dto.MedicineDTO;
import com.example.healthcare.model.entity.Medicine;
import com.example.healthcare.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/medicines")
@RequiredArgsConstructor
public class AdminMedicineController {

    private final MedicineRepository medicineRepo;

    // 1. HIỂN THỊ DANH SÁCH THUỐC (CÓ PHÂN TRANG 5 BẢN GHI/TRANG)
    @GetMapping
    public String manageMedicines(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Medicine> medicinePage = medicineRepo.findAll(PageRequest.of(page, 5));
        model.addAttribute("medicinePage", medicinePage);
        model.addAttribute("dto", new MedicineDTO());
        return "admin/medicine-manage";
    }

    // 2. LƯU HOẶC CẬP NHẬT THUỐC
    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute("dto") MedicineDTO dto, Model model) {
        boolean hasError = false;

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            model.addAttribute("errorName", "Vui lòng nhập tên thuốc!");
            hasError = true;
        }
        if (dto.getUnit() == null || dto.getUnit().trim().isEmpty()) {
            model.addAttribute("errorUnit", "Vui lòng nhập đơn vị!");
            hasError = true;
        }

        // NẾU CÓ LỖI NHẬP LIỆU -> Vẫn phải load lại trang 1 để bảng không bị trắng
        if (hasError) {
            model.addAttribute("medicinePage", medicineRepo.findAll(PageRequest.of(0, 5)));
            return "admin/medicine-manage";
        }

        Medicine medicine;
        if (dto.getId() != null) {
            medicine = medicineRepo.findById(dto.getId()).orElse(new Medicine());
        } else {
            medicine = new Medicine();
        }
        medicine.setName(dto.getName());
        medicine.setUnit(dto.getUnit());
        medicine.setDescription(dto.getDescription());

        medicineRepo.save(medicine);
        return "redirect:/admin/medicines?success";
    }

    // 3. XÓA THUỐC
    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineRepo.deleteById(id);
        return "redirect:/admin/medicines?deleted";
    }
}