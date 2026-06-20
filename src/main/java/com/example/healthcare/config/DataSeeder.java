package com.example.healthcare.config;

import com.example.healthcare.model.entity.*;
import com.example.healthcare.repository.*;
import com.example.healthcare.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepo;
    private final MedicineRepository medicineRepo;
    private final UserRepository userRepo;
    private final AppointmentRepository appRepo;
    private final MedicalRecordRepository recordRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // =======================================================
        // 1. BƠM DỮ LIỆU CHUYÊN KHOA (8 Khoa - Có giá tiền)
        // =======================================================
        if (specialtyRepo.count() == 0) {
            specialtyRepo.saveAll(Arrays.asList(
                    createSpecialty("Nội tổng quát", "Khám và điều trị các bệnh lý nội khoa chung.", 150000.0),
                    createSpecialty("Nhi khoa", "Khám, điều trị bệnh và tư vấn dinh dưỡng cho trẻ em.", 200000.0),
                    createSpecialty("Tai Mũi Họng", "Khám và điều trị các bệnh lý về tai, mũi, họng.", 180000.0),
                    createSpecialty("Răng Hàm Mặt", "Tư vấn, khám và điều trị các bệnh lý về răng miệng.", 250000.0),
                    createSpecialty("Da liễu", "Khám và điều trị các bệnh lý về da.", 150000.0),
                    createSpecialty("Nhãn khoa", "Khám và điều trị các bệnh lý về mắt.", 200000.0),
                    createSpecialty("Thần kinh", "Khám và điều trị thần kinh, tâm lý.", 300000.0),
                    createSpecialty("Tim mạch", "Khám và điều trị các bệnh lý tim mạch, huyết áp.", 350000.0)
            ));
        }

        // =======================================================
        // 2. BƠM DỮ LIỆU THUỐC (12 Loại - Có Số lượng tồn kho)
        // =======================================================
        if (medicineRepo.count() == 0) {
            medicineRepo.saveAll(Arrays.asList(
                    createMedicine("Paracetamol 500mg", "Viên", "Giảm đau, hạ sốt", 500),
                    createMedicine("Amoxicillin 250mg", "Viên", "Kháng sinh phổ rộng", 300),
                    createMedicine("Vitamin C 1000mg", "Tuýp", "Tăng sức đề kháng", 150),
                    createMedicine("Ibuprofen 400mg", "Viên", "Kháng viêm, giảm đau", 400),
                    createMedicine("Omeprazole 20mg", "Vỉ", "Trị trào ngược dạ dày", 200),
                    createMedicine("Oresol", "Gói", "Bù nước và điện giải", 1000),
                    createMedicine("Panadol Extra", "Vỉ", "Giảm đau đầu dữ dội", 250),
                    createMedicine("Salbutamol 2mg", "Chai", "Xịt hen suyễn, mở khí quản", 50),
                    createMedicine("Metformin 500mg", "Hộp", "Kiểm soát tiểu đường", 100),
                    createMedicine("Losartan 50mg", "Hộp", "Kiểm soát huyết áp cao", 120),
                    createMedicine("Loratadine 10mg", "Vỉ", "Thuốc chống dị ứng", 350),
                    createMedicine("Smecta", "Gói", "Điều trị tiêu chảy", 450)
            ));
        }

        // =======================================================
        // 3. BƠM DỮ LIỆU USER (15 Tài khoản: 1 Admin, 4 Bác sĩ, 10 Bệnh nhân)
        // =======================================================
        if (userRepo.count() == 0) {
            String defaultPass = HashUtil.hashPassword("123456");
            List<Specialty> specs = specialtyRepo.findAll();

            // Admin
            createUser("admin", defaultPass, Role.ADMIN, "Quản Trị Viên", "Nam", "0999999999", null);

            // 4 Bác sĩ
            createUser("bs_luc", defaultPass, Role.DOCTOR, "BS. Trần Văn Lực", "Nam", "0987654321", specs.get(0));
            createUser("bs_mai", defaultPass, Role.DOCTOR, "BS. Lê Thị Mai", "Nữ", "0912345678", specs.get(1));
            createUser("bs_thach", defaultPass, Role.DOCTOR, "BS. Phạm Ngọc Thạch", "Nam", "0901234567", specs.get(2));
            createUser("bs_hoa", defaultPass, Role.DOCTOR, "BS. Nguyễn Thị Hoa", "Nữ", "0923456789", specs.get(3));

            // 10 Bệnh nhân
            createUser("tienthanh", defaultPass, Role.PATIENT, "Nguyễn Tiến Thành", "Nam", "0368318206", null);
            createUser("baokhanh", defaultPass, Role.PATIENT, "Nguyễn Trần Bảo Khánh", "Nam", "0321112223", null);
            createUser("xuanhoang", defaultPass, Role.PATIENT, "Ngô Xuân Hoàng", "Nam", "0332223334", null);
            createUser("trongtu", defaultPass, Role.PATIENT, "Bàng Trọng Tú", "Nam", "0343334445", null);
            createUser("truongan", defaultPass, Role.PATIENT, "Nguyễn Trường An", "Nam", "0354445556", null);
            createUser("phuocanh", defaultPass, Role.PATIENT, "Phan Phước Anh", "Nam", "0365556667", null);
            createUser("minhtuan", defaultPass, Role.PATIENT, "Lê Minh Tuấn", "Nam", "0376667778", null);
            createUser("thuha", defaultPass, Role.PATIENT, "Phạm Thu Hà", "Nữ", "0387778889", null);
            createUser("bichngoc", defaultPass, Role.PATIENT, "Trần Bích Ngọc", "Nữ", "0398889990", null);
            createUser("hoanghai", defaultPass, Role.PATIENT, "Vũ Hoàng Hải", "Nam", "0319990001", null);
        }

        // =======================================================
        // 4. BƠM DỮ LIỆU LỊCH KHÁM & BỆNH ÁN (Tạo Biểu đồ Doanh thu & Hàng chờ)
        // =======================================================
        if (appRepo.count() == 0) {
            List<User> doctors = userRepo.findByRole(Role.DOCTOR);
            List<User> patients = userRepo.findByRole(Role.PATIENT);
            LocalDate today = LocalDate.now();

            // A. TẠO 5 LỊCH HẸN HÔM NAY (Trạng thái: PENDING) -> Để Bác sĩ có lịch chờ khám
            createAppointment(patients.get(0), doctors.get(0), today, LocalTime.of(8, 30), "PENDING", null);
            createAppointment(patients.get(1), doctors.get(0), today, LocalTime.of(9, 30), "PENDING", null);
            createAppointment(patients.get(2), doctors.get(1), today, LocalTime.of(10, 0), "PENDING", null);
            createAppointment(patients.get(3), doctors.get(2), today, LocalTime.of(14, 0), "PENDING", null);
            createAppointment(patients.get(4), doctors.get(3), today, LocalTime.of(15, 30), "PENDING", null);

            // B. TẠO LỊCH ĐÃ HỦY
            createAppointment(patients.get(5), doctors.get(0), today.minusDays(2), LocalTime.of(9, 0), "CANCELLED", null);
            createAppointment(patients.get(6), doctors.get(1), today.plusDays(1), LocalTime.of(10, 0), "CANCELLED", null);

            // C. TẠO 8 LỊCH ĐÃ KHÁM XONG (Vào tháng trước & tháng này để vẽ Biểu đồ doanh thu)
            // Kèm theo đó là tạo luôn MedicalRecord (Bệnh án) để test Quầy phát thuốc
            createCompletedRecord(patients.get(0), doctors.get(0), today.minusMonths(1).withDayOfMonth(5), LocalTime.of(8, 0), "Đau đầu dữ dội, sốt nhẹ", "Viêm xoang cấp tính", "- Paracetamol 500mg (SL: 10) | HD: Ngày 2 viên sáng tối\n- Amoxicillin 250mg (SL: 15) | HD: Ngày 3 viên", "DISPENSED");
            createCompletedRecord(patients.get(1), doctors.get(1), today.minusMonths(1).withDayOfMonth(15), LocalTime.of(9, 0), "Ho khan, đau họng, có đờm", "Viêm họng hạt", "- Vitamin C 1000mg (SL: 1) | HD: Pha 1 viên/ngày\n- Omeprazole 20mg (SL: 10) | HD: Ngày 1 viên", "DISPENSED");
            createCompletedRecord(patients.get(2), doctors.get(2), today.minusMonths(1).withDayOfMonth(25), LocalTime.of(10, 0), "Ù tai, chảy dịch tai trái", "Viêm tai giữa", "- Ibuprofen 400mg (SL: 10) | HD: Ngày 2 viên\n- Amoxicillin 250mg (SL: 20) | HD: Ngày 2 viên", "DISPENSED");
            createCompletedRecord(patients.get(3), doctors.get(3), today.minusDays(15), LocalTime.of(14, 0), "Đau răng số 8, sưng má", "Viêm nướu răng khôn", "- Paracetamol 500mg (SL: 15) | HD: Ngày 2 viên\n- Smecta (SL: 5) | HD: Uống khi đau", "DISPENSED");
            createCompletedRecord(patients.get(4), doctors.get(0), today.minusDays(10), LocalTime.of(15, 0), "Nổi mẩn đỏ ngứa khắp người", "Dị ứng thời tiết", "- Loratadine 10mg (SL: 10) | HD: Ngày 1 viên tối\n- Vitamin C 1000mg (SL: 1) | HD: Ngày 1 viên", "DISPENSED");

            // 3 Đơn thuốc vừa khám xong HÔM QUA (Trạng thái PENDING để Quầy phát thuốc có việc làm)
            createCompletedRecord(patients.get(7), doctors.get(1), today.minusDays(1), LocalTime.of(8, 30), "Tiêu chảy liên tục, buồn nôn", "Ngộ độc thực phẩm nhẹ", "- Oresol (SL: 10) | HD: Pha uống thay nước\n- Smecta (SL: 10) | HD: Ngày 2 gói", "PENDING");
            createCompletedRecord(patients.get(8), doctors.get(2), today.minusDays(1), LocalTime.of(10, 30), "Huyết áp tăng cao, chóng mặt", "Cao huyết áp vô căn", "- Losartan 50mg (SL: 30) | HD: Ngày 1 viên sáng\n- Panadol Extra (SL: 10) | HD: Uống khi đau đầu", "PENDING");
            createCompletedRecord(patients.get(9), doctors.get(3), today.minusDays(1), LocalTime.of(16, 0), "Khó thở, thở khò khè về đêm", "Hen phế quản", "- Salbutamol 2mg (SL: 1) | HD: Xịt khi khó thở\n- Vitamin C 1000mg (SL: 1) | HD: Tăng đề kháng", "PENDING");

            System.out.println("=========================================================");
            System.out.println("🚀 ĐÃ KHỞI TẠO XONG TOÀN BỘ DỮ LIỆU MẪU (FULL CHỨC NĂNG)");
            System.out.println("=========================================================");
        }
    }

    // --- CÁC HÀM HỖ TRỢ BÊN DƯỚI CHỈ ĐỂ CODE GỌN GÀNG HƠN ---

    private Specialty createSpecialty(String name, String desc, Double price) {
        Specialty s = new Specialty();
        s.setName(name);
        s.setDescription(desc);
        s.setPrice(price);
        return s;
    }

    private Medicine createMedicine(String name, String unit, String desc, Integer quantity) {
        Medicine m = new Medicine();
        m.setName(name);
        m.setUnit(unit);
        m.setDescription(desc);
        m.setQuantity(quantity);
        return m;
    }

    private void createUser(String username, String pass, Role role, String fullName, String gender, String phone, Specialty spec) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(pass);
        user.setRole(role);
        user.setEnabled(true);
        user.setSpecialty(spec);

        UserProfile profile = new UserProfile();
        profile.setFullName(fullName);
        profile.setGender(gender);
        profile.setPhone(phone);
        profile.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Mặc định sinh năm 90 cho lẹ
        profile.setUser(user);

        user.setProfile(profile);
        userRepo.save(user);
    }

    private Appointment createAppointment(User patient, User doctor, LocalDate date, LocalTime time, String status, String note) {
        Appointment app = new Appointment();
        app.setPatient(patient);
        app.setDoctor(doctor);
        app.setSpecialty(doctor.getSpecialty());
        app.setAppointmentDate(date);
        app.setAppointmentTime(time);
        app.setStatus(status);
        return appRepo.save(app);
    }

    private void createCompletedRecord(User patient, User doctor, LocalDate date, LocalTime time, String symp, String diag, String pres, String dispenseStatus) {
        // 1. Tạo lịch hẹn đã hoàn thành
        Appointment app = createAppointment(patient, doctor, date, time, "COMPLETED", null);

        // 2. Tạo bệnh án đính kèm
        MedicalRecord record = new MedicalRecord();
        record.setAppointment(app);
        record.setSymptoms(symp);
        record.setDiagnosis(diag);
        record.setPrescription(pres);
        record.setDispenseStatus(dispenseStatus);
        record.setCreatedAt(LocalDateTime.of(date, time.plusMinutes(30))); // Giả lập thời gian khám xong là 30p sau khi hẹn

        recordRepo.save(record);
    }
}