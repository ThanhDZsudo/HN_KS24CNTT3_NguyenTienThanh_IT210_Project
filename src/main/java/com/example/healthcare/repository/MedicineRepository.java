package com.example.healthcare.repository;

import com.example.healthcare.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // THÊM DÒNG NÀY ĐỂ TÌM THUỐC BẰNG TÊN
    Optional<Medicine> findByName(String name);

}