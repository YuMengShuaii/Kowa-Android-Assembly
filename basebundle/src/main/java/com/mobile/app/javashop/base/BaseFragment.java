package com.mobile.app.javashop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobile.app.javashop.ApiService.apiservice;
import com.nat.android.javashoplib.utils.Utilsinterface;

/**
 * Created by LDD on 17/4/1.
 */

public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends Fragment {
    private View layout = null;

    public BaseActivity activity;


    protected T presenter;

    apiservice api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (layout==null){
            layout = inflater.inflate(getLayId(),null);
        }
        activity = (BaseActivity) getActivity();

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindDagger();

        attachView();

        init();

        bindEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        destory();
    }

    protected abstract  int getLayId();

    protected abstract void bindDagger();

    protected abstract void init();

    protected abstract void bindEvent();

    protected abstract void destory();

    private void attachView(){
        presenter.attachView(this,api);
    }

    protected Utilsinterface getUtils(){

        return activity;
    }

}
