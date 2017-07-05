package com.nat.android.javashoplib.utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.nat.android.javashoplib.R;
import com.nat.android.javashoplib.init.FrameInit;
import com.nat.android.javashoplib.weight.MyDialog;
import com.nat.android.javashoplib.weight.ScrollBackView;

/**
 * Created by LDD on 17/4/13.
 */

public abstract class UtilsActivity extends AppCompatActivity implements Utilsinterface {
    protected UtilsActivity activity = null;
    private ScrollBackView scrollBackView;
    private MyDialog dialog = null;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        scrollBackView.attachToActivity(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onActivityCreate();
        activity = this;
        scrollBackView.setEdgeOrientation(ScrollBackView.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        scrollBackView.setParallaxOffset(0.5f); // （类iOS）滑动退出视觉差，默认0.3
        scrollBackView.addSwipeListener(new ScrollBackView.OnSwipeListener() {
            @Override
            public void onDragStateChange(int state) {
                // Drag state
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                // 触摸的边缘flag
            }

            @Override
            public void onDragScrolled(float scrollPercent) {
                // 滑动百分比
                if (scrollPercent>50){
                    onBackPressed();
                }
            }
        });
        if (!FrameInit.isOpenScroll()){
            scrollBackView.setEnableGesture(false);
        }else{
            Utils.LogerE("scroll",getRunningActivityName());
            if (getRunningActivityName().equals(FrameInit.getHomeActivity())){
                scrollBackView.setEnableGesture(false);
            }
        }
        initUtils();
    }

    private void initUtils(){
        dialog = Utils.createLoadingDialog(activity);
    }

    @Override
    public void showDialog(){
        dialog.show();
    }

    @Override
    public void dismissDialog(){
        if (dialog!=null){
            dialog.Mydismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (getRunningActivityName().equals(FrameInit.getHomeActivity())){
            Intent localIntent = new Intent("android.intent.action.MAIN");
            localIntent.addCategory("android.intent.category.HOME");
            startActivity(localIntent);
            return;
        }
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public String getRunningActivityName() {
        String contextString = activity.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    void onActivityCreate() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        scrollBackView = new ScrollBackView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollBackView.setLayoutParams(params);
    }
    public boolean swipeBackPriority() {
        return getSupportFragmentManager().getBackStackEntryCount() <= 1;
    }
}
