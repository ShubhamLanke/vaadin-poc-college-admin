package com.dss.view;

import com.dss.common.base.BaseView;
import com.dss.form.CollegeForm;
import com.dss.form.StudentForm;
import com.dss.model.College;
import com.dss.model.Student;
import com.dss.presenter.AdminPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

        VerticalLayout collegeSection = new VerticalLayout(collegeButtonsLayout(), collegeGrid);
        collegeSection.setSizeFull();
        collegeSection.setPadding(false);

        VerticalLayout studentSection = new VerticalLayout(studentButtonsLayout(), studentGrid);
        studentSection.setSizeFull();
        studentSection.setPadding(false);

        HorizontalLayout layout = new HorizontalLayout(collegeSection, studentSection);
        layout.setSizeFull();
        layout.setFlexGrow(1, collegeSection, studentSection);

        add(layout);
        getPresenter().loadColleges();
    }

    private void setupCollegeGrid() {
        collegeGrid.addColumn(College::getName).setHeader("Name");
        collegeGrid.addColumn(College::getCity).setHeader("City");
        collegeGrid.addColumn(College::getPhoneNo).setHeader("Phone");
        collegeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        collegeGrid.asSingleSelect().addValueChangeListener(e -> {
            selectedCollege = e.getValue();
            if (selectedCollege != null) {
                getPresenter().loadStudentsByCollege(selectedCollege);
            }
        });

        add(collegeGrid);
    }

    private void setupStudentGrid() {
        studentGrid.addColumn(Student::getName).setHeader("Name");
        studentGrid.addColumn(Student::getEmail).setHeader("Email");
        add(studentGrid);
    }

    private HorizontalLayout collegeButtonsLayout() {
        Button add = new Button("Add College", e -> openCollegeForm(new College()));
        Button edit = new Button("Edit College", e -> {
            College selected = collegeGrid.asSingleSelect().getValue();
            if (selected != null) {
                openCollegeForm(selected);
            } else {
                Notification.show("Select a college to edit");
            }
        });
        Button delete = new Button("Delete College", e -> {
            College selected = collegeGrid.asSingleSelect().getValue();
            if (selected != null) {
                getPresenter().deleteCollege(selected);
            }
        });

        add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return new HorizontalLayout(add, edit, delete);
    }

    private HorizontalLayout studentButtonsLayout() {
        Button add = new Button("Add Student", e -> {
            if (selectedCollege == null) {
                Notification.show("Please select a college first.");
                return;
            }
            Student newStudent = new Student();
            newStudent.setCollege(selectedCollege);
            openStudentForm(newStudent);
        });

        Button edit = new Button("Edit Student", e -> {
            Student selected = studentGrid.asSingleSelect().getValue();
            if (selected != null) {
                openStudentForm(selected);
            } else {
                Notification.show("Select a student to edit");
            }
        });

        Button delete = new Button("Delete Student", e -> {
            Student selected = studentGrid.asSingleSelect().getValue();
            if (selected != null) {
                getPresenter().deleteStudent(selected);
            }
        });

        add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return new HorizontalLayout(add, edit, delete);
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
            form.showProgress(true);
            form.setSaveEnabled(false);

            getUI().ifPresent(ui -> ui.access(() -> {
                getPresenter().saveStudent(saved);
                form.showProgress(false);
                form.setSaveEnabled(true);
                dialog.close();
            }));
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
