package com.mobile.app.javashop.ApiService;

import com.mobile.app.javashop.model.BolgModel;
import com.mobile.app.javashop.model.catModel;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by LDD on 17/3/14.
 */

public interface apiservice {

    @GET("content.json")
    Observable<List<BolgModel.DataBean>> getBlogData();

    @GET("cat.json")
    Observable<catModel> getCat();

    @GET("Home_anim.json")
    Observable<catModel> getHomeAnim();


}
