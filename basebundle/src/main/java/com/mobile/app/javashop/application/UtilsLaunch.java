package com.mobile.app.javashop.application;

import android.app.Application;
import android.util.Log;
import com.mobile.app.javashop.ApiService.apiservice;
import com.mobile.app.javashop.Config;
import com.nat.android.javashoplib.netutils.NetServiceFactory;

/**
 * UtilsBundle 启动配置
 */

public class UtilsLaunch extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("UtilsBundel","Launch");
        Config.apiservice = NetServiceFactory.getInstance().createService(apiservice.class,"http://yumengshuai.cn/");
    }
}
