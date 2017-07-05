package com.nat.android.javashoplib.imageutils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by LDD on 17/1/17.
 */

public class MyGlideModule implements GlideModule {
    public static void setCache(GlideBuilder paramGlideBuilder, Context paramContext)
    {
        int i = (int)Runtime.getRuntime().maxMemory() / 8;
        paramGlideBuilder.setMemoryCache(new LruResourceCache(i));
        paramGlideBuilder.setDiskCache(new DiskLruCacheFactory(paramContext.getExternalCacheDir().getPath(), "Glide", 31457280));
        paramGlideBuilder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        paramGlideBuilder.setBitmapPool(new LruBitmapPool(i));
    }

    public void applyOptions(Context paramContext, GlideBuilder paramGlideBuilder)
    {
        setCache(paramGlideBuilder, paramContext);
    }

    public void registerComponents(Context paramContext, Glide paramGlide)
    {
    }
}
