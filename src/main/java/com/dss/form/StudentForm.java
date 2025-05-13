package com.dss.form;

import com.dss.model.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;
import java.util.function.Consumer;

public class StudentForm extends FormLayout {

    private final TextField name = new TextField("Name");
    private final TextField phoneNo = new TextField("Phone No");
    private final EmailField email = new EmailField("Email");
    private final RadioButtonGroup<String> gender = new RadioButtonGroup<>();
    private final DatePicker dob = new DatePicker("Date of Birth");
    private final TextArea address = new TextArea("Address");

    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");
    private final ProgressBar progressBar = new ProgressBar();

    private final Binder<Student> binder = new Binder<>(Student.class);
    private Consumer<Student> onSave;
    private Runnable onCancel;

    public StudentForm() {
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        gender.setLabel("Gender");
        gender.setItems("Male", "Female", "Other");

        address.setMaxLength(250);
        address.setPlaceholder("Enter full address...");

        configureValidation();
        configureButtons();

        add(name, phoneNo, email, gender, dob, address, progressBar, new HorizontalLayout(save, cancel));
        progressBar.setVisible(false);
    }

    private void configureValidation() {
        binder.forField(name)
                .asRequired("Name is required")
                .bind(Student::getName, Student::setName);

        binder.forField(phoneNo)
                .asRequired("Phone is required")
                .withValidator(p -> p.matches("\\d{10}"), "Must be 10 digits")
                .bind(Student::getPhoneNo, Student::setPhoneNo);

        binder.forField(email)
                .asRequired("Email required")
                .withValidator(e -> e.contains("@"), "Invalid email")
                .bind(Student::getEmail, Student::setEmail);

        binder.forField(gender)
                .asRequired("Gender is required")
                .bind(Student::getGender, Student::setGender);

        binder.forField(dob)
                .asRequired("Date of birth is required")
                .withValidator(new DateRangeValidator("Date must be in the past", null, LocalDate.now()))
                .bind(Student::getDob, Student::setDob);

        binder.forField(address)
                .asRequired("Address is required")
                .bind(Student::getAddress, Student::setAddress);
    }

    private void configureButtons() {
        save.addClickListener(event -> {
            if (binder.validate().isOk()) {
                progressBar.setIndeterminate(true);
                progressBar.setVisible(true);

                Student student = binder.getBean();
                if (onSave != null) {
                    onSave.accept(student);
                }

                Notification.show("Student saved!")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                progressBar.setVisible(false);
            }
        });

        cancel.addClickListener(event -> {
            if (onCancel != null) onCancel.run();
        });
    }

    public void setStudent(Student student) {
        binder.setBean(student);
    }

    public void setOnSave(Consumer<Student> listener) {
        this.onSave = listener;
    }

    public void setOnCancel(Runnable listener) {
        this.onCancel = listener;
    }
}
