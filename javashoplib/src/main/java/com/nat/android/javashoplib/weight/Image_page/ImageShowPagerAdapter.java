package com.nat.android.javashoplib.weight.Image_page;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import com.nat.android.javashoplib.R;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ImageShowPagerAdapter<T extends BaseImageModel> extends PagerAdapter {


    Context mContext;
    List<T> urls;
    PinchImageView.ScrollListener listener ;
    public ImageShowPagerAdapter(Context mContext, List<T> urls, PinchImageView.ScrollListener scrollListener) {
        this.mContext = mContext;
        this.urls = urls;
        this.listener = scrollListener;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_show_item,null);
        PinchImageView iv = (PinchImageView) view.findViewById(R.id.iv_image_show);
        iv.setScrollLisener(listener);
        Glide.with(mContext).load(urls.get(position).getUrl()).into(iv);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}

