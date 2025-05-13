package com.dss.service;

import com.dss.model.College;
import com.dss.repository.CollegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollegeServiceImpl implements CollegeService{

    private final CollegeRepository collegeRepository;

    public CollegeServiceImpl(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @Override
    public List<College> getAllCollege() {
        return collegeRepository.findAll();
    }

    @Override
    public Optional<College> getCollegeById(Long id) {
        return collegeRepository.findById(id);
    }

    @Override
    public College saveCollege(College college) {
        return collegeRepository.save(college);
    }

    @Override
    public void deleteCollege(Long id) {
        collegeRepository.deleteById(id);
    }
}
