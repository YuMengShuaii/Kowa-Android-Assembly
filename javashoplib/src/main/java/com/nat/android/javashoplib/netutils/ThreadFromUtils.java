package com.nat.android.javashoplib.netutils;

import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThreadFromUtils
{
    /**
     * 默认线程 处理数据在io线程 ，接收数据后的处理在主线程
     * @param <T> Gson转换处理的泛型
     * @return
     */
    public static <T> Transformer<T, T> defaultSchedulers() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 全部在Io线程
     * @param <T> Gson转换处理的泛型
     * @return
     */
    public static <T> Transformer<T, T> all_io() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(Schedulers.io()).subscribeOn(Schedulers.io());
            }
        };
    }
}