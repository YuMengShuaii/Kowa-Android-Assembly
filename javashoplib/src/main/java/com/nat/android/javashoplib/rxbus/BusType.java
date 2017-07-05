package com.nat.android.javashoplib.rxbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LDD on 17/4/24.
 */

public class BusType {
    public static final int MAIN = 0;
    public static final int BACKGROUND = 1;
    @IntDef({MAIN, BACKGROUND})
    @Retention(RetentionPolicy.SOURCE)

    public @interface Type {
    }
}
