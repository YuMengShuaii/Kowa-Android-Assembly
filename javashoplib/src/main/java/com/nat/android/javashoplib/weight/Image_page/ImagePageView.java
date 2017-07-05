package com.nat.android.javashoplib.weight.Image_page;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.nat.android.javashoplib.R;
import com.nat.android.javashoplib.utils.Utils;

/**
 * Created by LDD on 17/3/7.
 */

public class ImagePageView<T extends BaseImageModel> {
    private  Dialog dialog;
    private ViewPager viewPager;
    private  TextView title,bottonbar;
    private  LinearLayout linearLayout;
    private  ImageShowPagerAdapter adapter;
    private List<T> urls;
    private Context context;
    private int position;

    /**
     * 多张图片
     * @param urls
     * @param context
     * @param position
     */
    public ImagePageView(List<T> urls, Context context,int position) {
        this.urls     = urls;
        this.context  = context;
        this.position = position;
        init();
    }
    /**
     * 单张图片
     * @param url
     * @param context
     */
    public ImagePageView(T url,Context context){
        urls = new ArrayList<>();
        urls.add(url);
        this.context  = context;
        this.position = -1;
        init();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        final View view =  LayoutInflater.from(context).inflate(R.layout.imageview_dialog,null);
        viewPager = (ViewPager) view.findViewById(R.id.viewpage);
        title = (TextView) view.findViewById(R.id.title);
        bottonbar = (TextView) view.findViewById(R.id.index);
        initViewPager(view);
    }
    private void initViewPager(final View view){
        adapter = new ImageShowPagerAdapter(context, urls, new PinchImageView.ScrollListener() {
            /**
             * 监听下滑事件，缩小Dialog，或者关闭Dialog
             */
            @Override
            public void down() {
                final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                if (layoutParams.width== Utils.getScreenWidth(context)/1.5){
                    dialog.dismiss();
                }
                linearLayout = (LinearLayout) view.findViewById(R.id.imagelay);
                layoutParams.width = (int) (Utils.getScreenWidth(context)/1.5);
                layoutParams.height = (int) (Utils.getScreenHeight(context)/1.5);
                bottonbar.setBackgroundResource(R.drawable.image_bar_radis);
                title.setBackgroundResource(R.drawable.image_title_radis);
                linearLayout.setBackgroundResource(R.drawable.imafe_radis);
                linearLayout.setLayoutParams(layoutParams);
            }

            /**
             * 监听上滑事件，放大Dialog
             */
            @Override
            public void up() {
                linearLayout = (LinearLayout) view.findViewById(R.id.imagelay);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                layoutParams.width = (int) (Utils.getScreenWidth(context));
                layoutParams.height = (int) (Utils.getScreenHeight(context)-Utils.getStatusBarHeight(context));
                bottonbar.setBackgroundResource(R.drawable.big_botton);
                title.setBackgroundResource(R.drawable.big_title);
                linearLayout.setBackgroundResource(R.drawable.big_back);
                linearLayout.setLayoutParams(layoutParams);
            }
        });
        viewPager.setAdapter(adapter);
        if (position!=-1){
            title.setText(urls.get(position).getTitle());
        }else{
            title.setText(urls.get(0).getTitle());
        }
        if (position!=-1){
            bottonbar.setText((position+1)+"/"+urls.size());
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottonbar.setText((position+1)+"/"+urls.size());
                title.setText(urls.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        linearLayout = (LinearLayout) view.findViewById(R.id.imagelay);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = (int) (Utils.getScreenWidth(context)/1.5);
        layoutParams.height = (int) (Utils.getScreenHeight(context)/1.5);
        linearLayout.setLayoutParams(layoutParams);
        dialog.setContentView(view);
    }
    public void setData(List<T> data){
        this.urls = data;
        adapter.notifyDataSetChanged();
    }

    public void show(){
        dialog.show();
    }

}
