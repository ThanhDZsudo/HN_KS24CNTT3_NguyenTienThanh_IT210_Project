package com.example.healthcare.controller.admin;

import com.example.healthcare.model.entity.Medicine;
import com.example.healthcare.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/medicines")
@RequiredArgsConstructor
public class AdminMedicineController {

    private final MedicineRepository medicineRepo;

    // 1. HIỂN THỊ DANH SÁCH KHO THUỐC
    @GetMapping
    public String manageMedicines(@RequestParam(defaultValue = "0") int page, Model model) {
        // ĐỔI TÊN BIẾN TỪ "medicines" THÀNH "medicinePage" ĐỂ KHỚP VỚI HTML
        model.addAttribute("medicinePage", medicineRepo.findAll(PageRequest.of(page, 5)));
        return "admin/medicine-manage";
    }

    // 2. THÊM THUỐC MỚI
    @PostMapping("/create")
    public String createMedicine(@RequestParam("name") String name,
                                 @RequestParam("quantity") Integer quantity) {
        Medicine medicine = new Medicine();
        medicine.setName(name);
        medicine.setQuantity(quantity);

        medicine.setUnit("Viên/Hộp");
        medicine.setDescription("Dược phẩm nhập kho mới");

        medicineRepo.save(medicine);
        // Chỗ này là redirect về đường dẫn URL (vẫn giữ nguyên là /admin/medicines)
        return "redirect:/admin/medicines?success=created";
    }

    // 3. CẬP NHẬT SỐ LƯỢNG THUỐC
    @PostMapping("/update/{id}")
    public String updateMedicine(@PathVariable("id") Long id,
                                 @RequestParam("quantity") Integer quantity) {
        Medicine medicine = medicineRepo.findById(id).orElse(null);
        if (medicine != null) {
            medicine.setQuantity(quantity);
            medicineRepo.save(medicine);
        }
        return "redirect:/admin/medicines?success=updated";
    }

    // 4. XÓA THUỐC KHỎI KHO
    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable("id") Long id) {
        if (medicineRepo.existsById(id)) {
            medicineRepo.deleteById(id);
        }
        return "redirect:/admin/medicines?deleted=true";
    }
}