package com.nat.android.javashoplib.netutils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 下载进度监听拦截器
 */

public class ProgressInterceptor implements Interceptor
{
    /**
     * 下载进度监听接口
     */
     private NetServiceFactory.ProgressLisener progressListener;

    /**
     * 构造方法 传递监听接口
     * @param progressListener 监听接口
     */
    public ProgressInterceptor(NetServiceFactory.ProgressLisener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * @param  chain 数据链
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException
    {
        //从数据链中获取Respose
        Response originalResponse = chain.proceed(chain.request());
        //使用集成Respone
        return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),progressListener)).build();
    }
}
