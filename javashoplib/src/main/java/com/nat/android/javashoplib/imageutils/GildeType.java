package com.nat.android.javashoplib.imageutils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LDD on 17/3/2.
 */

public class GildeType {

    public static final int FIT_CENCER=0;

    public static final int CROP_CENCER=1;

    @IntDef({FIT_CENCER, CROP_CENCER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

}
