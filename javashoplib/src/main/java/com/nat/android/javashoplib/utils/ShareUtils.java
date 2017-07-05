package com.nat.android.javashoplib.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by LDD on 17/3/1.
 */

public class ShareUtils {

    /**
     * 分享网页
     */
    public static void shareUrl(Activity activity, String content, String title,String dialogTitle){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT,title);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
        //创建分享的Dialog
        if (dialogTitle!=null||!dialogTitle.equals("")){
            share_intent = Intent.createChooser(share_intent, dialogTitle);
        }else{
            share_intent = Intent.createChooser(share_intent, "Javashop分享");
        }
        activity.startActivity(share_intent);
    }
}
