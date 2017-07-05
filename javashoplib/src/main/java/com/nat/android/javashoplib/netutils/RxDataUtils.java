package com.nat.android.javashoplib.netutils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.SocketTimeoutException;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import com.nat.android.javashoplib.init.FrameInit;
import com.nat.android.javashoplib.utils.Utils;
/**
 * Created by LDD on 17/3/29.
 */

public class RxDataUtils {

    /**
     * 处理Retrofit注册后的Observable
     * @param observable api观察者
     * @param getData    数据获取接口
     * @param OutType    自定义线程处理
     * @param <T>        Json字符串转换的类 必须继承BaseData 这样才可以进行错误处理
     */
    public static <T extends BaseData> Subscription RxGet(Observable<T> observable, final RxDataListener<T> getData, Observable.Transformer<T, T> OutType){
        //开始请求网络！
        getData.start();
        //线程处理
        return  observable.compose(OutType)
                //结果回调
                .subscribe(new Observer<T>() {

                    @Override
                    public void onCompleted() {

                    }
                    //错误处理
                    @Override
                    public void onError(Throwable paramThrowable) {
                        if ((paramThrowable instanceof SocketTimeoutException) || (paramThrowable.getMessage().contains("Failed")) || (paramThrowable.getMessage().contains("502")) || (paramThrowable.getMessage().contains("404"))){
                            Utils.ToastS("网络故障！");
                        }
                        getData.failed(paramThrowable);
                    }
                    //错误处理，返回结果！
                    @Override
                    public void onNext(T o) {
                        if (o.getResult()==1){
                            getData.success(o);
                        }else{
                            getData.failed(new Throwable("获取数据失败！原因："+o.getMessage()));
                        }
                    }
                });
    }

    /**
     * 处理Retrofit注册后的Observable 该类不需要传递线程处理参数，使用默认线程参数  observaon(Mian) subscribeOn(IO)
     * @param observable    API观察者
     * @param rxLisener     数据监听接口
     * @param <T>           Json字符串转换的类 必须继承BaseData 这样才可以进行错误处理
     */
    public static <T extends BaseData> Subscription RxGet(Observable<T> observable, final RxDataListener<T> rxLisener){
        return RxGet(observable,rxLisener,ThreadFromUtils.<T>defaultSchedulers());
    }

    /**
     * 下载文件方法，带进度监听功能
     * @param tClass    下载service接口
     * @param activity  调用者Activity
     * @param listener  进度监听
     * @param <S>       service接口
     * @return          注册成功的service接口
     */
    public static <S> S  DownFile(Class<S> tClass, String BaseUrl , final Activity activity, final DownloadListener listener){
         return  NetServiceFactory.getInstance().createService( tClass, BaseUrl, new NetServiceFactory.ProgressLisener() {
            @Override
            public void prigress(final long read, final long count, final boolean isOver) {
                //让接收数据后的操作在主线程运行
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //传递进度参数
                        listener.listener(read,count,isOver);
                    }
                });
            }
        });
    }

    /**
     * 执行下载操作 并写入本地
     * @param observable       观察者
     * @param downLoadManager  写入本地操作接口，本地有实现类，可以实现该类自定义本地操作接口！
     */
    public static Subscription RxDownLoad(Observable<ResponseBody> observable , final BaseDownLoadManager downLoadManager, final NetServiceFactory.ProgressLisener lisener){
       return observable
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return new ProgressResponseBody(responseBody,lisener);
                    }
                })
                .compose(ThreadFromUtils.<ResponseBody>all_io())

                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxUtils下载文件异常",e.getMessage());
                        Utils.ToastL("RxUtils下载文件异常"+e.getMessage());
                    }

                    @Override
                    public void onNext(final ResponseBody response) {
                                downLoadManager.writeResponseBodyToDisk(FrameInit.Mcontext.getApplicationContext(), response);
                    }
               });
    }

    /**
     *  执行下载操作 并写入本地 采用已经实现的 DownLoadManager 进行写入本地操作
     *  @param observable 观察者
     */
    public static Subscription RxDownLoad(Observable<ResponseBody> observable,NetServiceFactory.ProgressLisener listener){
        return RxDownLoad(observable,new DownLoadManager(),listener);
    }
    /**
     * 数据获取接口
     * @param <T> Gson解析类
     */
    public interface RxDataListener< T extends BaseData> extends Start{
        void success(T data);
        void failed(Throwable e);
    }

    /**
     * 下载进度传递接口
     */
    public interface DownloadListener{
        void listener(long read,long count,boolean isOver);
    }

    /**
     * 开始请求接口
     */
    public interface Start{
        void start();
    }
}
