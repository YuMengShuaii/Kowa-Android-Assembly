package com.nat.android.javashoplib.weight.yms_pickerview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LDD on 17/3/3.
 */

public class PickerType {

    public static final int First = 0;
    public static final int Second = 1;
    public static final int Third = 2;
    public static final int Fourth = 3;

    @IntDef({First, Second, Third, Fourth})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }
}
