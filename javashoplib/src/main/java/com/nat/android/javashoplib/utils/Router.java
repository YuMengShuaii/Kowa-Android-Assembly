package com.nat.android.javashoplib.utils;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nat.android.javashoplib.R;

import java.io.Serializable;

/**
 * Created by LDD on 17/4/26.
 */

public class Router {
    private Intent intent;
    private static Router router;
    private AppCompatActivity context;
    private String tag;
    public static Router init(AppCompatActivity activity, String classname) {
        if (router == null) {
            router = new Router();
        }
        router.config(activity, classname);
        return router;
    }

    public Router() {

    }

    public Router setPackageName(String pName){
        intent.setPackage(pName);
        return router;
    }

    public void go() {
        Log.e("RouterTo",tag);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void config(AppCompatActivity context, String classname) {
        this.context = context;
        intent = new Intent();
        intent.setClassName(context, classname);
        tag = classname;
    }

    public Router putExtra(String key, String value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, String[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, int value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, int[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, boolean value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, boolean[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, float value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, float[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, double value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, double[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, long value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, long[] value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, Parcelable value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, Serializable value) {
        intent.putExtra(key, value);
        return router;
    }

    public Router putExtra(String key, Parcelable[] value) {
        intent.putExtra(key, value);
        return router;
    }
}
