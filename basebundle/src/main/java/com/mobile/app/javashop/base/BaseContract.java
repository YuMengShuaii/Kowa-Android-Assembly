package com.mobile.app.javashop.base;

import com.mobile.app.javashop.ApiService.apiservice;

/**
 * Created by LDD on 17/3/31.
 */

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view, apiservice api);

        void detachView();
    }

    interface BaseView {

        void showError(String messgae);

        void complete(String message);

        void start();

    }
}