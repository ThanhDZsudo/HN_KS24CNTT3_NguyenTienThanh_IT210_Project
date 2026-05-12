package com.example.healthcare.service.impl;

import com.example.healthcare.model.entity.Medicine;
import com.example.healthcare.repository.MedicineRepository;
import com.example.healthcare.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepo;

    @Override
    public List<Medicine> getAll() {
        return medicineRepo.findAll();
    }

    @Override
    public void save(Medicine medicine) {
        medicineRepo.save(medicine);
    }

    @Override
    public void delete(Long id) {
        medicineRepo.deleteById(id);
    }
}