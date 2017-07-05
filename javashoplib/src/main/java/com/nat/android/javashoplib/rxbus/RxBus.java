package com.nat.android.javashoplib.rxbus;

import android.support.annotation.MainThread;
import android.util.Log;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import com.nat.android.javashoplib.netutils.ThreadFromUtils;

/**
 * RxBus
 */
public class RxBus {
    /**
     * 单例对象
     */
    private static volatile RxBus defaultInstance;

    private final Subject<Object, Object> bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 单例RxBus
    public static RxBus getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance ;
    }

    // 发送一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public  <T> Subscription register(final Class<T> cl,@BusType.Type int type, final RxBusEvent<T> rxevent) {
        return bus.ofType(cl)
                .compose(type==BusType.MAIN?ThreadFromUtils.<T>defaultSchedulers():ThreadFromUtils.<T>all_io())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxBus","订阅"+cl.getClass().getName()+"的Activity出现问题，问题原因："+e.getMessage()+"请快速定位问题！");
                    }

                    @Override
                    public void onNext(T t) {
                        rxevent.event(t);
                    }
                });
    }

    /**
     * 重写默认U线程方法
     * @param cl
     * @param rxevent
     * @param <T>
     * @return
     */
    public  <T> Subscription register(final Class<T> cl ,final RxBusEvent<T> rxevent){
           return register(cl,BusType.MAIN,rxevent);
    }
    /**
     * 事件接受接口
     * @param <T> 事件类
     */
    public interface RxBusEvent<T>{

        void event(T data);

    }
}