package com.example.healthcare.config;

import com.example.healthcare.model.entity.Medicine;
import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.Specialty;
import com.example.healthcare.model.entity.User;
import com.example.healthcare.model.entity.UserProfile;
import com.example.healthcare.repository.MedicineRepository;
import com.example.healthcare.repository.SpecialtyRepository;
import com.example.healthcare.repository.UserRepository;
import com.example.healthcare.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepository;
    private final MedicineRepository medicineRepository; // Tiêm thêm Repo Thuốc
    private final UserRepository userRepository;         // Tiêm thêm Repo User

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Kiểm tra và bơm dữ liệu Chuyên khoa (Giữ nguyên code của cậu)
        if (specialtyRepository.count() == 0) {
            Specialty s1 = new Specialty(); s1.setName("Nội tổng quát"); s1.setDescription("Khám và điều trị các bệnh lý nội khoa chung.");
            Specialty s2 = new Specialty(); s2.setName("Nhi khoa"); s2.setDescription("Khám, điều trị bệnh và tư vấn dinh dưỡng cho trẻ em.");
            Specialty s3 = new Specialty(); s3.setName("Tai Mũi Họng"); s3.setDescription("Khám và điều trị các bệnh lý về tai, mũi, họng.");
            Specialty s4 = new Specialty(); s4.setName("Răng Hàm Mặt"); s4.setDescription("Tư vấn, khám và điều trị các bệnh lý về răng miệng.");
            Specialty s5 = new Specialty(); s5.setName("Da liễu"); s5.setDescription("Khám và điều trị các bệnh lý về da.");
            specialtyRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5));
        }

        // 2. Bơm 10 dữ liệu Thuốc
        if (medicineRepository.count() == 0) {
            medicineRepository.saveAll(Arrays.asList(
                    createMedicine("Paracetamol 500mg", "Viên", "Giảm đau, hạ sốt"),
                    createMedicine("Amoxicillin 250mg", "Viên", "Kháng sinh"),
                    createMedicine("Vitamin C 1000mg", "Tuýp", "Tăng đề kháng"),
                    createMedicine("Ibuprofen 400mg", "Viên", "Kháng viêm"),
                    createMedicine("Omeprazole 20mg", "Vỉ", "Dạ dày"),
                    createMedicine("Oresol", "Gói", "Bù nước"),
                    createMedicine("Panadol Extra", "Vỉ", "Giảm đau đầu"),
                    createMedicine("Salbutamol 2mg", "Chai", "Xịt hen suyễn"),
                    createMedicine("Metformin 500mg", "Hộp", "Tiểu đường"),
                    createMedicine("Losartan 50mg", "Hộp", "Huyết áp")
            ));
        }

        // 3. Bơm 10 Tài khoản (Tất cả đều dùng chung mật khẩu 123456)
        if (userRepository.count() == 0) {
            // Dùng hàm băm chuẩn của project để băm mật khẩu 123456
            String defaultPass = HashUtil.hashPassword("123456");
            List<Specialty> specs = specialtyRepository.findAll();

            // --- ADMIN ---
            createUser("admin", defaultPass, Role.ADMIN, "Quản Trị Viên", null);

            // --- ĐỘI NGŨ BÁC SĨ (Gắn sẵn vào các chuyên khoa đầu tiên) ---
            createUser("bs_luc", defaultPass, Role.DOCTOR, "BS. Trần Văn Lực", specs.size() > 0 ? specs.get(0) : null);
            createUser("bs_mai", defaultPass, Role.DOCTOR, "BS. Lê Thị Mai", specs.size() > 1 ? specs.get(1) : null);
            createUser("bs_thach", defaultPass, Role.DOCTOR, "BS. Phạm Ngọc Thạch", specs.size() > 2 ? specs.get(2) : null);

            // --- BỆNH NHÂN (Toàn bộ anh em trong nhóm) ---
            createUser("tienthanh", defaultPass, Role.PATIENT, "Nguyễn Tiến Thành", null);
            createUser("baokhanh", defaultPass, Role.PATIENT, "Nguyễn Trần Bảo Khánh", null);
            createUser("xuanhoang", defaultPass, Role.PATIENT, "Ngô Xuân Hoàng", null);
            createUser("trongtu", defaultPass, Role.PATIENT, "Bàng Trọng Tú", null);
            createUser("truongan", defaultPass, Role.PATIENT, "Nguyễn Trường An", null);
            createUser("phuocanh", defaultPass, Role.PATIENT, "Phan Phước Anh", null);

            System.out.println("=========================================================");
            System.out.println("🚀 ĐÃ KHỞI TẠO XONG 10 TÀI KHOẢN (MẬT KHẨU CHUNG: 123456)");
            System.out.println("=========================================================");
        }
    }

    // --- HÀM HỖ TRỢ (Giúp code bên trên ngắn gọn, dễ nhìn) ---

    private Medicine createMedicine(String name, String unit, String desc) {
        Medicine m = new Medicine();
        m.setName(name);
        m.setUnit(unit);
        m.setDescription(desc);
        return m;
    }

    private void createUser(String username, String pass, Role role, String fullName, Specialty spec) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(pass);
        user.setRole(role);
        user.setEnabled(true);
        user.setSpecialty(spec);

        UserProfile profile = new UserProfile();
        profile.setFullName(fullName);
        profile.setUser(user);
        user.setProfile(profile); // Liên kết Profile với User

        userRepository.save(user); // Lưu User (Profile sẽ tự động lưu theo nhờ CascadeType.ALL)
    }
}