package com.mobile.app.javashop.base;

import android.os.Bundle;
import android.view.View;

import com.mobile.app.javashop.ApiService.apiservice;
import com.mobile.app.javashop.Config;
import com.mobile.app.javashop.R;
import com.nat.android.javashoplib.utils.UtilsActivity;

/**
 * Created by LDD on 17/4/1.
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends UtilsActivity {
    protected View view;

    protected T presenter;

    protected apiservice api = Config.apiservice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLay();
        presenter = initPresenter();
        setContentView(rootView);
        attachView();
        init();
        BindEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }
        destory();
    }

    protected abstract View  getLay();

    protected abstract T initPresenter();

    protected abstract void init();

    protected abstract void BindEvent();

    protected abstract void destory();

    private void attachView(){
        presenter.attachView(this,api);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
