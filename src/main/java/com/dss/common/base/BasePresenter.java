package com.dss.common.base;

public abstract class BasePresenter<T extends View> implements Presenter<T> {

    T view;

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }
}
