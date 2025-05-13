package com.dss.presenter;

import com.dss.common.base.BasePresenter;
import com.dss.model.College;
import com.dss.model.Student;
import com.dss.service.CollegeService;
import com.dss.service.StudentService;
import com.dss.view.OverviewView;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UIScope
@SpringComponent
public class OverviewPresenter extends BasePresenter<OverviewView> {

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private StudentService studentService;

    public void loadCollegeHierarchy() {
        List<College> colleges = collegeService.getAllCollege();
        Map<College, List<Student>> collegeMap = new HashMap<>();

        for (College college : colleges) {
            List<Student> students = studentService.getStudentsByCollege(college);
            collegeMap.put(college, students);
        }

        getView().showCollegeTree(collegeMap);
    }
}
