package com.nat.android.javashoplib.maputils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LDD on 17/2/24.
 */

public class MapType {

    public static final int BUS = 0;
    public static final int WALK = 1;
    public static final int DRIVE = 2;

    @IntDef({BUS, WALK, DRIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MT {
    }
}
