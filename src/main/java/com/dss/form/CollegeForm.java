package com.dss.form;

import com.dss.model.College;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.function.Consumer;

public class CollegeForm extends FormLayout {

    private final TextField name = new TextField("Name");
    private final TextField phoneNo = new TextField("Phone No");
    private final EmailField email = new EmailField("Email");
    private final TextField city = new TextField("City");
    private final TextField address = new TextField("Address");

    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");

    private final ProgressBar progressBar = new ProgressBar();

    private final Binder<College> binder = new Binder<>(College.class);
    private Consumer<College> onSave;
    private Runnable onCancel;

    public CollegeForm() {
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        configureValidation();
        configureButtons();

        add(name, city, address, phoneNo, email, progressBar, createButtonLayout());
        progressBar.setVisible(false);
    }

    private Component createButtonLayout() {
        HorizontalLayout layout = new HorizontalLayout(save, cancel);
        layout.setSpacing(true);
        return layout;
    }

    private void configureValidation() {
        binder.forField(name)
                .asRequired("Name is required")
                .bind(College::getName, College::setName);

        binder.forField(phoneNo)
                .asRequired("Phone is required")
                .withValidator(phone -> phone.matches("\\d{10}"), "Must be 10 digits")
                .bind(College::getPhoneNo, College::setPhoneNo);

        binder.forField(email)
                .asRequired("Email required")
                .withValidator(e -> e.contains("@"), "Invalid email")
                .bind(College::getEmail, College::setEmail);

        binder.forField(city)
                .asRequired("City is required")
                .bind(College::getCity, College::setCity);

        binder.forField(address)
                .asRequired("Address is required")
                .bind(College::getAddress, College::setAddress);
    }

    private void configureButtons() {
        save.addClickListener(event -> {
            if (binder.validate().isOk()) {
                progressBar.setIndeterminate(true);
                progressBar.setVisible(true);

                College college = binder.getBean();
                if (onSave != null) {
                    onSave.accept(college);
                }

                Notification.show("Saved successfully!")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                progressBar.setVisible(false);
            }
        });

        cancel.addClickListener(event -> {
            if (onCancel != null) onCancel.run();
        });
    }

    public void setCollege(College college) {
        binder.setBean(college);
    }

    public void setOnSave(Consumer<College> listener) {
        this.onSave = listener;
    }

    public void setOnCancel(Runnable listener) {
        this.onCancel = listener;
    }
}
