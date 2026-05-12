package com.example.healthcare.repository;

import com.example.healthcare.model.entity.Role;
import com.example.healthcare.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm User theo tên đăng nhập
    Optional<User> findByUsername(String username);

    // Lấy danh sách theo Role (Không phân trang)
    List<User> findByRole(Role role);

    // Hỗ trợ Phân trang cho màn hình Admin
    Page<User> findByRole(Role role, Pageable pageable);

    // ĐÂY NÀY! HÀM BỊ THIẾU ĐỂ FIX LỖI CHO PATIENT CONTROLLER
    List<User> findByRoleAndSpecialtyId(Role role, Long specialtyId);
}