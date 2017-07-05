package com.nat.android.javashoplib.maputils;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;

import java.util.ArrayList;

import com.nat.android.javashoplib.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by LDD on 17/2/21.
 */

public class MapUtils implements LocationSource {
    /**
     * 路线搜索对象
     */
    private  RouteSearch routeSearch;
    /**
     * 地图配置对象
     */
    private AMap aMap;
    /**
     * 调用Activity
     */
    private Activity context;
    /**
     * 目的地坐标
     */
    private LatLng end;
    /**
     * Location监听事件
     */
    private LocationSource.OnLocationChangedListener mListener;
    /**
     * 陀螺仪对象
     */
    private Sensor mSensor;
    /**
     * 陀螺仪控制器
     */
    private SensorManager mSM;
    /**
     * MapUtils类单例实现
     */
    private volatile static MapUtils maputils;

    /**
     * 目标点标题
     */
    private String endtitle;

    /**
     * 陀螺仪事件监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float degree = event.values[0];
                float bearing = aMap.getCameraPosition().bearing;
                if (degree + bearing >360 )
                    aMap.setMyLocationRotateAngle(degree + bearing - 360);// 设置小蓝点旋转角度
                else
                    aMap.setMyLocationRotateAngle(degree + bearing);//
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    /**
     * 防止外部实例化该类
     */
    private MapUtils(){}

    /**
     * 获取MapUtils对象
     * @param context 调用者上下文
     * @param aMap    Amap对象
     * @param end     目的地坐标
     * @return
     */
    public static MapUtils getMapUtils(Activity context,AMap aMap,LatLng end,String endTitle,boolean isVisableEnd) {
        maputils = new MapUtils(context,aMap,end,endTitle,isVisableEnd);
        return maputils;
    }
    /**
     * 防止外部实例化该类，构造方法
     */
    private MapUtils(Activity context,AMap aMap,LatLng end,String endTitle,boolean isVisableEnd) {
        this.aMap = aMap;
        this.context = context;
        this.end = end;
        this.endtitle = endTitle;
        init(isVisableEnd);
    }
    /**
     * 该类初始化相关参数
     */
    private void init(boolean isVisableEnd){
        //注册Location改变的监听事件
        aMap.setLocationSource(this);
        //设置默认定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        //设置是否显示
        aMap.setMyLocationEnabled(true);
        //通过AMap显示交通
        //aMap.setTrafficEnabled(true);
        //设置infoWindow的样式
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                TextView v =  new TextView(context);
                v.setText(marker.getTitle());
                v.setBackgroundResource(R.drawable.custom_info_bubble);
                v.setTextColor(Color.BLACK);
                v.setMaxEms(10);
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {

                return null;
            }
        });
        //初始化陀螺仪管理器
        mSM = (SensorManager)(context.getSystemService(SENSOR_SERVICE));
        //初始化陀螺仪
        mSensor = mSM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //注册陀螺仪回调接口
        mSM.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);//注册回调函数
        //连续不间断获取自身定位 间隔2秒
        Location.getLocation().getLocationDetail(context, new Listener.DataLisener<AMapLocation>() {
            @Override
            public void sussce(final AMapLocation aMapLocation) {
                if (mListener != null && aMapLocation != null) {
                    Log.e("error","Location_hint");
                    MapConst.mylocation = aMapLocation;
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                }
            }

            @Override
            public void faild(String error) {
                Toast.makeText(context,"网络原因，定位失败！",Toast.LENGTH_SHORT).show();
            }
        },false);
        //初始化检索对象
        routeSearch = new RouteSearch(context);
        //设置Marker
        setMarker(end,isVisableEnd);
    }
    /**
     * 更新目的地坐标信息
     * @param end
     */
    public void setEndLocation(LatLng end){
        this.end = end;
        //重新绘制Marker
        setMarker(end,true);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(end));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    /**
     * 绘制坐标信息（起点，目的地，方向标）
     * @param end 目的地
     */
    public void setMarker(LatLng end,boolean isVisableEnd){
        //设置Location
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置Location图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        //加入myLocation
        aMap.setMyLocationStyle(myLocationStyle);
        if (isVisableEnd){
            String title = endtitle;
            if (title==null){
                title="";
            }
            //添加目的地Marker
            aMap.addMarker(new MarkerOptions().position(end).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.adresss))).showInfoWindow();
        }
    }

    /**
     * 定位开始，坐标发生改变
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }
    /**
     *  定位结束，释放资源。
     */
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 调用该类结束，释放资源
     */
    public void finshMapUtils(){
        Location.getLocation().stopLocation();
        mSM.unregisterListener(sensorEventListener, mSensor);
        aMap=null;
        mSensor=null;
        routeSearch=null;
        mListener=null;
        sensorEventListener=null;
        end=null;
    }

    /**
     * 移动地图
     * @param start 坐标
     */
    public void moveMap(LatLonPoint start){
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(start.getLatitude(),start.getLongitude())));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    public void setPathTrajectory(@MapType.MT int mapType, Object path, LatLonPoint start, LatLonPoint end, ArrayList<PathModel.Path.BusStep.Info> infos){
        if (mapType==MapType.BUS){
            BusRouteOverlay busRouteOverlay = new BusRouteOverlay(context, aMap, (BusPath) path, start, end);
            busRouteOverlay.removeFromMap();
            busRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
            busRouteOverlay.addToMap();
            busRouteOverlay.zoomToSpan();
            setMarker(infos);
        }
        if (mapType==MapType.DRIVE){
            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(context, aMap, (DrivePath) path, start, end);
            drivingRouteOverlay.removeFromMap();
            drivingRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
            setMarker(infos);
        }
        if (mapType==MapType.WALK){
            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(context, aMap, (WalkPath) path, start,end);
            walkRouteOverlay.removeFromMap();
            walkRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
            walkRouteOverlay.addToMap();
            walkRouteOverlay.zoomToSpan();
            setMarker(infos);
        }
    }

    /**
     * 设置路径轨迹中的Marker
     * @param list
     */
    private void setMarker(ArrayList<PathModel.Path.BusStep.Info> list){
        for (PathModel.Path.BusStep.Info info : list) {
            if (info.getWalkStartPoint()!=null){
                aMap.addMarker(new MarkerOptions().position(new LatLng(info.getWalkStartPoint().getLatitude(),info.getWalkStartPoint().getLongitude())).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.exchange_walk)));
            }
            if (info.getBusStartPoint()!=null){
                aMap.addMarker(new MarkerOptions().position(new LatLng(info.getBusStartPoint().getLatitude(),info.getBusStartPoint().getLongitude())).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.logobus)));
            }
            if (info.getCarStartPoint()!=null){
                aMap.addMarker(new MarkerOptions().position(new LatLng(info.getCarStartPoint().getLatitude(),info.getCarStartPoint().getLongitude())).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));
            }
        }
    }
}
