package com.enation.android.firstbundle.presenter;

import com.enation.android.firstbundle.contra.FirstContra;
import com.mobile.app.javashop.base.RxPresenter;
import com.mobile.app.javashop.model.catModel;
import com.nat.android.javashoplib.netutils.RxDataUtils;

/**
 * Created by LDD on 17/4/28.
 */

public class firstPresenter extends RxPresenter<FirstContra.view> implements FirstContra.Presenter {
    @Override
    public void loadFirst() {
        RxDataUtils.RxGet(api.getCat(), new RxDataUtils.RxDataListener<catModel>() {
            @Override
            public void success(catModel data) {
                mView.showFirst();
                mView.showError("");
            }

            @Override
            public void failed(Throwable e) {
                mView.showError("");
            }

            @Override
            public void start() {
                mView.start();
            }
        });
    }
}
