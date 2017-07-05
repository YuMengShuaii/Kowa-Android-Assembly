package com.enation.android.firstbundle.contra;

import com.mobile.app.javashop.base.BaseContract;

/**
 * Created by LDD on 17/4/28.
 */

public interface FirstContra {

    interface view extends BaseContract.BaseView{
        void showFirst();
    }
    interface Presenter extends BaseContract.BasePresenter<view>{
        void loadFirst();
    }

}
