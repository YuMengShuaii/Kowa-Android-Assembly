package com.nat.android.javashoplib.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nat.android.javashoplib.R;
import com.nat.android.javashoplib.weight.yms_pickerview.BasePickerModel;
import com.nat.android.javashoplib.weight.yms_pickerview.PickerType;

/**
 * Created by LDD on 17/1/16.
 */

public class MyDialog extends Dialog {
    private Animation hyperspaceJumpAnimation;
    private ImageView view;

    public MyDialog(Context paramContext, int paramInt,ImageView view)
    {
        super(paramContext, paramInt);
        this.hyperspaceJumpAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.loading_animation);
        this.view = view;
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
    }

    public void show()
    {
        super.show();
        if (this.view != null||!isShowing())
            this.view.startAnimation(this.hyperspaceJumpAnimation);
    }

    public void Mydismiss(){
        if (this.isShowing()){
            this.dismiss();
        }
    }

}
