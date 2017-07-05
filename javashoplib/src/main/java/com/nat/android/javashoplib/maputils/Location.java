package com.nat.android.javashoplib.maputils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.cloud.CloudResult;

import com.nat.android.javashoplib.init.FrameInit;

/**
 * Created by LDD on 17/2/21.
 */

public class Location {
    /**
     * 定位参数配置
     */
    private   AMapLocationClientOption mLocationOption = null;
    /**
     * 定位监听
     */
    private   AMapLocationClient mlocationClient = null;
    /**
     * Location类单例实现
     */
    private volatile static Location location;

    /**
     * 防止外部New该类
     */
    private Location (){}

    /**
     * 双重校验加锁单例
     * @return 单例对象
     */
    public static Location getLocation() {
            if (location == null) {
                    synchronized (Location.class) {
                        if (location == null) {
                            location = new Location();
                        }
                    }
                 }
            return location;
    }

    /**
     *  获取位置信息
     * @param context  调用页面的Activity
     * @param lis      获取位置信息监听事件
     * @param isOnce 为true时调用一次，false时每隔两秒定位一次
     */
    public void getLocationDetail(Activity context, final Listener.DataLisener<AMapLocation> lis, boolean isOnce){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("正在获取位置信息！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        //初始化定位参数
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                dialog.dismiss();
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        lis.sussce(amapLocation);
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        lis.faild("location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(isOnce);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    /**
     * 停止定位服务
     */
    public void stopLocation(){
        mlocationClient.stopLocation();
        mlocationClient.onDestroy();
    }
}


/**
 *  AMapLocation参数详解
 */
//
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (amapLocation != null) {
//            if (amapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
//                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
//                amapLocation.getCity();//城市信息
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
//                amapLocation.getAoiName();//获取当前定位点的AOI信息
//                Log.e("Location",amapLocation.getAddress()+amapLocation.getCountry()+amapLocation.getProvince()+amapLocation.getCity()+amapLocation.getDistrict()
//                        +amapLocation.getStreet()+amapLocation.getStreetNum()+amapLocation.getCityCode()+amapLocation.getAdCode()+amapLocation.getAoiName());
//            } else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError","location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo());
//            }
//        }
//    }