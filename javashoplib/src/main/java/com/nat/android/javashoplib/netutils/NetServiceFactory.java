package com.nat.android.javashoplib.netutils;

import android.util.Log;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.nat.android.javashoplib.init.FrameInit;

/**
 * Retrofit网络服务构造类
 */

public class NetServiceFactory {
    /**
     * 默认超时时间
     */
    private final static long DEFAULT_TIMEOUT = 10;
    /**
     * Gson对象
     */
    private final Gson mGsonDateFormat;

    /**
     * 构造方法，初始化Gson对象
     */
    public NetServiceFactory() {
        mGsonDateFormat = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

    /**
     * 静态化生成单例对象
     */
    private static class SingletonHolder {
        private static final NetServiceFactory INSTANCE = new NetServiceFactory();
    }

    /**
     * 获取队里对象
     * @return 网络服务对象
     */
    public static NetServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 配置Logger
     * @return
     */
    private HttpLoggingInterceptor getLog(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("Net_JavaShop",message);
            }
        });
        return logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 构造网络服务Service 此方法需要配置下载监听接口，如不需要请调另外的重载方法！
     * @param serviceClass  API接口
     * @param baseurl       服务器BaseUrl
     * @param lisener       下载进度监听
     * @param <S>           返回服务对象
     * @return   服务接口
     */
    public <S> S createService(Class<S> serviceClass,String baseurl,ProgressLisener lisener) {
        //创建CallBack线程池，最大值为5
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //构建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                //服务器BaseUrl
                .baseUrl(baseurl)
                //配置OkHttpClint
                .client(getOkHttpClient(lisener))
                //配置回调线程池
                .callbackExecutor(executorService)
                //添加Gson解析
                .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                //添加RxJava函数式变成
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //构建
                .build();
                //把接口注册到Retrofit中
        return retrofit.create(serviceClass);
    }

    /**
     * 构造网络服务Service
     * @param serviceClass  API接口
     * @param baseurl       服务器BaseUrl
     * @param <S>           返回服务对象
     * @return  API服务接口
     */
    public <S> S createService(Class<S> serviceClass,String baseurl) {
        return createService(serviceClass,baseurl,null);
    }

    /**
     * 配置OkHttpClint 此方法需要配置下载进度监听接口 如不需要 请调用其他重载方法
     * @param lisener 下载进度1监听接口
     * @return  okHttpClint对象
     */
    private OkHttpClient getOkHttpClient(ProgressLisener lisener) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(FrameInit.Mcontext.getApplicationContext()));
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //添加拦截器，设置下载进度监听监听器
        //httpClientBuilder.addInterceptor(new ProgressInterceptor(lisener));
        //添加Logger
        httpClientBuilder.addInterceptor(getLog());
        //维护Cookies
        httpClientBuilder.cookieJar(cookieJar);
        //设置连接超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置写超时时间
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置读超时时间
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存路径 名称
        File httpCacheDirectory = new File(FrameInit.Mcontext.getApplicationContext().getCacheDir(), "OkHttpCache");
        //设置缓存大小
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));

        return httpClientBuilder.build();
    }

    /**
     * 配置OkHttp 此方法不用配置下载监听接口
     * @return OkHttpClint对象
     */
    private OkHttpClient getOkHttpClient() {

        return getOkHttpClient(null);

    }

    /**
     * 下载监听接口
     */
    public interface ProgressLisener{
       void prigress(long read,long count,boolean isOver);
    }
}
