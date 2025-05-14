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
import lombok.Setter;

import java.util.function.Consumer;

public class CollegeForm extends FormLayout {

    private final TextField name = new TextField("Name");
    private final TextField phoneNo = new TextField("Phone No");
    private final EmailField email = new EmailField("Email");
    private final TextField city = new TextField("City");
    private final TextField address = new TextField("Address");
    private final int TOTAL_COLLEGE_FIELDS = 5;

    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");

    private final ProgressBar progressBar = new ProgressBar();

    private final Binder<College> binder = new Binder<>(College.class);
    @Setter
    private Consumer<College> onSave;
    @Setter
    private Runnable onCancel;

    public CollegeForm() {
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("1000px", 2)
        );

        configureValidation();
        configureButtons();

        progressBar.setMin(0);
        progressBar.setMax(5);
        progressBar.setValue(0);
        progressBar.setWidthFull();

        name.addValueChangeListener(event -> updateProgress());
        city.addValueChangeListener(event -> updateProgress());
        address.addValueChangeListener(event -> updateProgress());
        phoneNo.addValueChangeListener(event -> updateProgress());
        email.addValueChangeListener(event -> updateProgress());

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        add(name, city, address, phoneNo, email, progressBar, createButtonLayout());
    }

    private void triggerProgressBar() {
        progressBar.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            getUI().ifPresent(ui -> ui.access(() -> progressBar.setVisible(false)));
        }).start();
    }

    private void updateProgress() {
        int filled = 0;
        if (!name.isEmpty()) filled++;
        if (!email.isEmpty()) filled++;
        if (!phoneNo.isEmpty()) filled++;
        if (!city.isEmpty()) filled++;
        if (!address.isEmpty()) filled++;

        double progress = (double) filled / TOTAL_COLLEGE_FIELDS;
        progressBar.setValue(progress);
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

}
