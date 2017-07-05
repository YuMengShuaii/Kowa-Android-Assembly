package com.enation.app.main.launch;

import android.content.Context;
import android.taobao.atlas.runtime.AtlasPreLauncher;
import android.util.Log;

/**
 * Created by LDD on 17/6/23.
 */

public class AppLaunch implements AtlasPreLauncher {
    @Override
    public void initBeforeAtlas(Context context) {
        Log.e("AppLaunch","App壳子启动");
    }
}
