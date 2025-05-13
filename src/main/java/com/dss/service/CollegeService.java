package com.dss.service;

import com.dss.model.College;

import java.util.List;
import java.util.Optional;

public interface CollegeService{

    List<College> getAllCollege();

    Optional<College> getCollegeById(Long id);

    College saveCollege(College college);

    void deleteCollege(Long id);
}
