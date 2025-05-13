package com.dss.presenter;

import com.dss.common.base.BasePresenter;
import com.dss.model.College;
import com.dss.model.Student;
import com.dss.service.CollegeService;
import com.dss.service.StudentService;
import com.dss.view.AdminView;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UIScope
@SpringComponent
public class AdminPresenter extends BasePresenter<AdminView> {

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private StudentService studentService;

    public void loadColleges() {
        List<College> colleges = collegeService.getAllCollege();
        getView().showColleges(colleges);
    }

    public void loadStudentsByCollege(College college) {
        List<Student> students = studentService.getStudentsByCollege(college);
        getView().showStudents(students);
    }

    public College saveCollege(College college) {
        College saved = collegeService.saveCollege(college);
        loadColleges();
        return saved;
    }

    public Student saveStudent(Student student) {
        Student saved = studentService.saveStudent(student);
        loadStudentsByCollege(student.getCollege());
        return saved;
    }

    public void deleteCollege(College college) {
        collegeService.deleteCollege(college.getId());
        loadColleges();
    }

    public void deleteStudent(Student student) {
        studentService.deleteStudent(student.getId());
        loadStudentsByCollege(student.getCollege());
    }
}
