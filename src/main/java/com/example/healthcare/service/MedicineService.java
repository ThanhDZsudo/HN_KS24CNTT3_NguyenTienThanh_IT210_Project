package com.example.healthcare.service;

import com.example.healthcare.model.entity.Medicine;
import java.util.List;

public interface MedicineService {
    List<Medicine> getAll();
    void save(Medicine medicine);
    void delete(Long id);
}