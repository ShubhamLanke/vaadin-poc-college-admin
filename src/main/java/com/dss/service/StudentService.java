package com.dss.service;

import com.dss.model.College;
import com.dss.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> getAllStudents();

    Optional<Student> getStudentById(Long id);

    List<Student> getStudentsByCollege(College college);

    Student saveStudent(Student student);

    void deleteStudent(Long id);
}
