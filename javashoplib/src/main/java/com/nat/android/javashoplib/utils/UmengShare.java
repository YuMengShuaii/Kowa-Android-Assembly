package com.nat.android.javashoplib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import rx.Observable;

/**
 * Created by LDD on 17/5/3.
 */

public class UmengShare implements UmengControl.UmengWebInit,UmengControl.UmengConfig{
    private ShareAction shareAction;

    private static  UmengShare umeng;

    private UMWeb web ;

    private  Activity activity;

    public static UmengControl.UmengWebInit init(Activity activity){
        if (umeng==null){
            umeng = new UmengShare();
        }
        umeng.config(activity);
        return umeng;
    }

    private UmengShare() {

    }

    private void config(Activity activity){
        this.activity = activity;
        shareAction = new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        onUiToast("分享中！");
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        onUiToast("分享成功！");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        onUiToast("分享失败！");
                        Log.e("UmengError","Error:"+throwable.toString()+throwable.getMessage()+throwable.getCause());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        onUiToast("分享取消！");
                    }
                });
    }
    private void onUiToast(final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.ToastL(message);
            }
        });
    }
    public UmengControl.UmengConfig setText(String text){
        web.setTitle(text);
        return umeng;
    }

    public UmengControl.UmengConfig setImage(Bitmap bitmap){
        UMImage umImage = new UMImage(activity,bitmap);
        web.setThumb(umImage);
        return umeng;
    }
    public UmengControl.UmengConfig setUrl(String url){
        web = new UMWeb(url);
        return umeng;
    }
    public void go(){
        shareAction.withMedia(web);
        shareAction.open();
    }

}
