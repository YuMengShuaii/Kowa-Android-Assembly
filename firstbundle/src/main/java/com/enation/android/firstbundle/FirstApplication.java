package com.enation.android.firstbundle;

import android.app.Application;
import android.util.Log;

/**
 * Created by LDD on 17/6/21.
 */

public class FirstApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("FirstBundel","已经启动！");
    }
}
