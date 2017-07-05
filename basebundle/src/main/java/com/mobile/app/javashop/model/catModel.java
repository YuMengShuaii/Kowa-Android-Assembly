package com.mobile.app.javashop.model;

import com.nat.android.javashoplib.netutils.BaseData;

import java.util.List;

/**
 * Created by LDD on 17/3/28.
 */

public class catModel extends BaseData {

    private List<CatBean> cat;

    public List<CatBean> getCat() {
        return cat;
    }

    public void setCat(List<CatBean> cat) {
        this.cat = cat;
    }

    @Override
    public String getMessage() {
        return cat!=null?"请求成功！":"请求失败！";
    }

    @Override
    public int getResult() {
        return cat!=null?1:0;
    }

    public static class CatBean {
        /**
         * name : LoadRunner
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
