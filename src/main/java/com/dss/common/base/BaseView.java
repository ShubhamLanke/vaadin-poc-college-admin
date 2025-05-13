package com.dss.common.base;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class BaseView<T extends Presenter> extends VerticalLayout implements View<T> {

    @Autowired
    private T presenter;

    @PostConstruct
    private void postConstruct() {
        if (!Objects.isNull(presenter)) {
            this.presenter.setView(this);
            this.init();
        }
    }

    @Override
    public T getPresenter() {
        return presenter;
    }

    public abstract void init();
}
