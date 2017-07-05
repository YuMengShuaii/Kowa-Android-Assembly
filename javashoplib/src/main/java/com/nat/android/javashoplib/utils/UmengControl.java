package com.nat.android.javashoplib.utils;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by LDD on 17/5/3.
 */

public interface UmengControl {

    interface UmengWebInit{
        UmengControl.UmengConfig setUrl(String url);
    }

    interface UmengConfig{
        UmengControl.UmengConfig setText(String text);
        UmengControl.UmengConfig setImage(Bitmap bitmap);
        void go();
    }
}
