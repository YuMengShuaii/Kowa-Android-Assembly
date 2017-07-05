package com.nat.android.javashoplib.netutils;

import android.content.Context;

import okhttp3.ResponseBody;

/**
 * Created by LDD on 17/3/31.
 */

public interface BaseDownLoadManager {
    boolean writeResponseBodyToDisk(Context context, ResponseBody body);
}
