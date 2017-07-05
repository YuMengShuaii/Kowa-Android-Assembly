package com.nat.android.javashoplib.imageutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import com.nat.android.javashoplib.R;

import static java.security.AccessController.getContext;

/**
 * Created by LDD on 17/3/2.
 */

public class GlideUtils {


    /**
     * 图片加载失败，点击重新加载方法 该方法使用默认站位图
     * @param paramContext          用户使用上下文       必传参数
     * @param paramImageView        显示图片的Imageview  必传参数
     * @param paramString           图片Url              必传参数
     * @param type                  图片裁剪方式，FIT_CENCER 图片全部显示，CROP_CENCER 图片裁剪中间显示
     */
    public static void setImageForErrorDefult(final Context paramContext, final ImageView paramImageView, final String paramString,@GildeType.Type int type){

        setImage(paramContext,paramImageView,paramString,null,null,-1,-1,type);

    }

    /**
     * 图片加载（引擎使用Gilde，功能：加载失败，点击重新加 加载成功为Imageview绑定onClick事件 可自定义站位图）
     * @param context                   用户使用上下文       必传参数
     * @param paramImageView            显示图片的Imageview  必传参数
     * @param paramString               图片Url              必传参数
     * @param listener                  加载成功Onclick事件 不使用该功能时 传null即可
     * @param arrayOfTransformation     用户增加特效的Transformation数组  不使用该功能时 传null即可  使用该功能时，无法使用裁剪功能
     * @param placeholder               加载中站位图    使用默认图片传参 -1
     * @param error                     加载失败站位图  使用默认图片传参 -1
     * @param type                      图片裁剪方式，FIT_CENCER 图片全部显示，CROP_CENCER 图片裁剪中间显示 使用该功能时无法使用图片处理功能
     */
    public static void setImage(final Context context, final ImageView paramImageView, final String paramString, final View.OnClickListener listener, final Transformation[] arrayOfTransformation, final int placeholder, final int error, @GildeType.Type final int type)
    {
        DrawableRequestBuilder localDrawableRequestBuilder = Glide.with(context).load(paramString).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                paramImageView.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        setImage(context,paramImageView, paramString,listener,arrayOfTransformation,placeholder,error,type);
                    }
                });
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (listener!=null){
                    paramImageView.setOnClickListener(listener);
                }
                return false;
            }
        });
        if (arrayOfTransformation!=null){
            localDrawableRequestBuilder.bitmapTransform(arrayOfTransformation);
        }
        if (type == GildeType.FIT_CENCER){
            localDrawableRequestBuilder.fitCenter();
        }
        if (type == GildeType.CROP_CENCER){
            localDrawableRequestBuilder.centerCrop();
        }
        int errorDraw = R.drawable.image_error;
        int placeDraw = R.drawable.image_loading;
        if (error!=-1){
            errorDraw = error;
        }
        if (placeholder!=-1){
            placeDraw = placeholder;
        }
            localDrawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeDraw).error(errorDraw).into(paramImageView);
    }
}
