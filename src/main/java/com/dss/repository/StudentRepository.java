package com.dss.repository;

import com.dss.model.College;
import com.dss.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByCollege(College college);
}
