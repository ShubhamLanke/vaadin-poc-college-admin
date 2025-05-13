package com.dss.view;

import com.dss.common.base.BaseView;
import com.dss.form.CollegeForm;
import com.dss.form.StudentForm;
import com.dss.model.College;
import com.dss.model.Student;
import com.dss.presenter.AdminPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin Panel")
public class AdminView extends BaseView<AdminPresenter> {

    private final Grid<College> collegeGrid = new Grid<>(College.class, false);
    private final Grid<Student> studentGrid = new Grid<>(Student.class, false);
    private College selectedCollege;

    @Override
    public void init() {
        setSizeFull();
        add(new H2("Admin Panel"));

        setupCollegeGrid();
        setupStudentGrid();

        HorizontalLayout layout = new HorizontalLayout(collegeGrid, studentGrid);
        layout.setSizeFull();
        layout.setFlexGrow(1, collegeGrid, studentGrid);

        add(layout);
        getPresenter().loadColleges();
    }

    private void setupCollegeGrid() {
        collegeGrid.addColumn(College::getName).setHeader("Name");
        collegeGrid.addColumn(College::getCity).setHeader("City");
        collegeGrid.addColumn(College::getPhoneNo).setHeader("Phone");
        collegeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Button add = new Button("Add College");
        Button delete = new Button("Delete College");

        add.addClickListener(e -> openCollegeForm(new College()));

        delete.addClickListener(e -> {
            College selected = collegeGrid.asSingleSelect().getValue();
            if (selected != null) {
                getPresenter().deleteCollege(selected);
            }
        });

        collegeGrid.asSingleSelect().addValueChangeListener(e -> {
            selectedCollege = e.getValue();
            if (selectedCollege != null) {
                getPresenter().loadStudentsByCollege(selectedCollege);
            }
        });

        add(new Div(add, delete));
    }

    private void setupStudentGrid() {
        studentGrid.addColumn(Student::getName).setHeader("Name");
        studentGrid.addColumn(Student::getEmail).setHeader("Email");

        Button add = new Button("Add Student");
        Button delete = new Button("Delete Student");

        add.addClickListener(e -> {
            if (selectedCollege == null) {
                Notification.show("Please select a college first.");
                return;
            }
            Student newStudent = new Student();
            newStudent.setCollege(selectedCollege);
            openStudentForm(newStudent);
        });

        delete.addClickListener(e -> {
            Student selected = studentGrid.asSingleSelect().getValue();
            if (selected != null) {
                getPresenter().deleteStudent(selected);
            }
        });

        add(new Div(add, delete));
    }

    private void openCollegeForm(College college) {
        Dialog dialog = new Dialog();
        CollegeForm form = new CollegeForm();
        form.setCollege(college);

        form.setOnSave(saved -> {
            getPresenter().saveCollege(saved);
            dialog.close();
        });

        form.setOnCancel(dialog::close);

        dialog.add(form);
        dialog.setModal(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle(college.getId() == null ? "Add College" : "Edit College");
        dialog.open();
    }

    private void openStudentForm(Student student) {
        Dialog dialog = new Dialog();
        StudentForm form = new StudentForm();
        form.setStudent(student);

        form.setOnSave(saved -> {
            getPresenter().saveStudent(saved);
            dialog.close();
        });

        form.setOnCancel(dialog::close);

        dialog.add(form);
        dialog.setModal(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle(student.getId() == null ? "Add Student" : "Edit Student");
        dialog.open();
    }

    public void showColleges(List<College> colleges) {
        collegeGrid.setItems(colleges);
    }

    public void showStudents(List<Student> students) {
        studentGrid.setItems(students);
    }
}
