package com.mobile.app.javashop.utils;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import com.nat.android.javashoplib.utils.ScreenUtils;

/**
 * Created by LDD on 17/4/1.
 */

public class StutasBarUtils {
    public static void FullByCollLay(AppCompatActivity activity, Toolbar toolbar,CollapsingToolbarLayout collapsing_toolbar_layout,Toolbar.OnMenuItemClickListener listener){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setToolbar(activity,toolbar,listener);
        final CollapsingToolbarLayout.LayoutParams param = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        param.setMargins(0, ScreenUtils.getStatusHeight(activity), 0, 0);
        toolbar.setLayoutParams(param);
    }
    public static void FullByCollLay(AppCompatActivity activity, Toolbar toolbar,CollapsingToolbarLayout collapsing_toolbar_layout){
        FullByCollLay(activity,toolbar,collapsing_toolbar_layout,null);
    }
    private static void setToolbar(final AppCompatActivity activity, Toolbar toolbar,Toolbar.OnMenuItemClickListener listener){
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        if (listener!=null){
            toolbar.setOnMenuItemClickListener(listener);
        }
        activity.getSupportActionBar().setHomeButtonEnabled(true); //显示小箭头
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
