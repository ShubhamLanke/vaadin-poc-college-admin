package com.dss.common.base;

public interface View<T extends Presenter> {

    T getPresenter();
}
