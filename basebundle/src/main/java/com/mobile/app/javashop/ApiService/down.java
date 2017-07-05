package com.mobile.app.javashop.ApiService;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by LDD on 17/3/30.
 */

public interface down {
    @GET("QQ_500.apk")
    Observable<ResponseBody> DownFile();
}
