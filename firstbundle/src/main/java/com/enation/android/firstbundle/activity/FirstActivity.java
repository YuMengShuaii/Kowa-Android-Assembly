package com.enation.android.firstbundle.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.enation.android.firstbundle.R;
import com.enation.android.firstbundle.contra.FirstContra;
import com.enation.android.firstbundle.presenter.firstPresenter;
import com.mobile.app.javashop.Config;
import com.mobile.app.javashop.base.BaseActivity;
import com.nat.android.javashoplib.utils.Router;
import com.nat.android.javashoplib.utils.Utils;

public class FirstActivity extends BaseActivity<firstPresenter> implements FirstContra.view {

    @Override
    protected View getLay() {
        Log.e("FirstBundel","INIT");
        Utils.ToastL("App-Version-1.0.12313213！");
        return  LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_first,null);
    }

    @Override
    protected firstPresenter initPresenter() {
        return new firstPresenter();
    }

    @Override
    protected void init() {
       findViewById(R.id.ca).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               presenter.loadFirst();
               Router.init(activity,Config.DETAIL).go();
           }
       });
    }

    @Override
    protected void BindEvent() {

    }

    @Override
    protected void destory() {

    }

    @Override
    public void showFirst() {
        Router.init(activity, Config.DETAIL).go();
    }

    @Override
    public void showError(String messgae) {
        //dismissDialog();
    }

    @Override
    public void complete(String message) {
       // dismissDialog();
    }

    @Override
    public void start() {
      //showDialog();
    }
}
