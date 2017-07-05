package com.nat.android.javashoplib.maputils;

import android.content.Context;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.ArrayList;

/**
 * Created by LDD on 17/2/24.
 */

public class GetPathUtils {
    /**
     * 公交路线
     */
    public static final int BUS = 1;
    /**
     * 自驾路线
     */
    public static final int DRIVER = 2;
    /**
     * 步行路线
     */
    public static final int WALK = 3;
    /**
     * 路线搜索对象
     */
    private  RouteSearch routeSearch;
    /**
     * Location类单例实现
     */
    private volatile static GetPathUtils getPathUtils;

    /**
     * 防止外部New该类
     */
    private GetPathUtils (){}

    /**
     * 双重校验加锁单例
     * @return 单例对象
     */
    public static GetPathUtils getPathUtils(Context context) {
        if (getPathUtils == null) {
            synchronized (RouteSearch.class) {
                if (getPathUtils == null) {
                    getPathUtils = new GetPathUtils(context);
                }
            }
        }
        return getPathUtils;
    }
    private GetPathUtils(Context context) {
        routeSearch = new RouteSearch(context);
    }

    /**
     * 去商店
     * @param end
     * @param type
     * @param getBus
     * @param getDruver
     * @param getFood
     */
    public void  getPathDataShop(LatLng end, int type, Listener.DataLisener<PathModel> getBus, Listener.DataLisener<PathModel> getDruver, Listener.DataLisener<PathModel> getFood){
        getPathData(end, type,getBus,getDruver,getFood,true);
    }

    /**
     * 回家
     * @param end
     * @param type
     * @param getBus
     * @param getDruver
     * @param getFood
     */
    public void  getPathDataHome(LatLng end, int type, Listener.DataLisener<PathModel> getBus, Listener.DataLisener<PathModel> getDruver, Listener.DataLisener<PathModel> getFood){
        getPathData(end, type,getBus,getDruver,getFood,false);
    }
    /**
     * 获取路线信息
     * @param type 类型 BUS:公交车 FOOD：步行 DIRVER：驾车
     * @param isToShop 是否去商店
     */
    public void getPathData(LatLng end, int type, final Listener.DataLisener<PathModel> getBus, final Listener.DataLisener<PathModel> getDruver, final Listener.DataLisener<PathModel> getFood, boolean isToShop){
        //注册监听函数
        initListener(getBus,getDruver,getFood);
        //声明起始坐标
        LatLonPoint mStartLatlng;
        //声明目的地坐标
        LatLonPoint mEndLatlng;
        if (isToShop){
            //转换起始坐标
            mStartLatlng = new LatLonPoint(Double.valueOf(MapConst.mylocation.getLatitude()), Double.valueOf(MapConst.mylocation.getLongitude()));
            //转换目的地坐标
            mEndLatlng = new LatLonPoint(Double.valueOf(end.latitude), Double.valueOf(end.longitude));
        }else{
            //转换起始坐标
            mEndLatlng  = new LatLonPoint(Double.valueOf(MapConst.mylocation.getLatitude()), Double.valueOf(MapConst.mylocation.getLongitude()));
            //转换目的地坐标
            mStartLatlng = new LatLonPoint(Double.valueOf(end.latitude), Double.valueOf(end.longitude));
        }
        //设置起始，目的地
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartLatlng, mEndLatlng);
        //根据Type检索相应值
        switch (type){
            case BUS:
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo,RouteSearch.BUS_DEFAULT,MapConst.mylocation.getCityCode(),0);
                routeSearch.calculateBusRouteAsyn(query); // 异步路径规划驾车模式查询
                break;
            case WALK:
                RouteSearch.WalkRouteQuery query3 = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
                routeSearch.calculateWalkRouteAsyn(query3);// 异步路径规划步行模式查询
                break;
            case DRIVER:
                RouteSearch.DriveRouteQuery query2 = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
                // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                routeSearch.calculateDriveRouteAsyn(query2);// 异步路径规划驾车模式查询
                break;
        }
    }
    /**
     * 注册数据监听事件
     * @param getBus    公交监听
     * @param getDruver 自驾监听
     * @param getFood   步行监听
     */
    private void initListener(final Listener.DataLisener<PathModel> getBus, final Listener.DataLisener<PathModel> getDruver, final Listener.DataLisener<PathModel> getFood){
        //设置监听
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            //获取公交数据
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

                if (i == 1000) {
                    //当i==1000时为成功
                    //初始化模型
                    PathModel pathModel = new PathModel();
                    //放入开始坐标
                    pathModel.setStart(busRouteResult.getStartPos());
                    //放入目的地坐标
                    pathModel.setEnd(busRouteResult.getTargetPos());
                    //路径信息数据集合
                    ArrayList<PathModel.Path> paths = new ArrayList<PathModel.Path>();
                    //循环获取路径信息
                    for (BusPath busPath : busRouteResult.getPaths()) {
                        //初始化路径信息模型
                        PathModel.Path path = new PathModel.Path();
                        //放入路径对象
                        path.setPath(busPath);
                        //获取耗费时间
                        int dur = (int) busPath.getDuration();
                        //获取步行距离
                        int dis = (int) busPath.getWalkDistance();
                        //生成字符串信息
                        String busRotueTitle = SchemeUtil.getBusRouteTitle(dur, dis);
                        //放入公交信息title
                        path.setBusRotueTitle(busRotueTitle);
                        //初始化公交名称数据集合
                        ArrayList<String> busname = new ArrayList<String>();
                        //初始化步骤数据集合
                        ArrayList<PathModel.Path.BusStep> Steps  = new ArrayList<PathModel.Path.BusStep>();
                        //循环获取步骤信息
                        for (BusStep busStep : busPath.getSteps()) {
                            //初始化步骤对象
                            PathModel.Path.BusStep busStep1 = new PathModel.Path.BusStep();
                            //放入步行起始点
                            busStep1.setWalkStartPoint(busStep.getWalk().getOrigin());
                            //放入步行结束点
                            busStep1.setWalkEndPoint(busStep.getWalk().getDestination());
                            //初始化详细数据信息集合
                            ArrayList<PathModel.Path.BusStep.Info> infos = new ArrayList<PathModel.Path.BusStep.Info>();
                            //循环获取步行信息
                            for (WalkStep walkStep : busStep.getWalk().getSteps()) {
                                //初始化详细信息集合
                                PathModel.Path.BusStep.Info info = new PathModel.Path.BusStep.Info();
                                //放入步行起始点
                                info.setWalkStartPoint(busStep.getWalk().getOrigin());
                                //放入步行结束点
                                info.setWalkEndPoint(busStep.getWalk().getDestination());
                                //放入详细文字描述
                                info.setInfo(walkStep.getInstruction());
                                //添加到数据集合
                                infos.add(info);
                            }
                            //设置bus名称
                            String busn="";
                            //初始化Flag
                            int flag =0;
                            //如果该公交步骤的公交线路总数大于0则执行以下代码
                            if (busStep.getBusLines().size()>0){
                                //初始化详细信息对象
                                PathModel.Path.BusStep.Info info = new PathModel.Path.BusStep.Info();
                                //循环获取公交信息
                                for (RouteBusLineItem routeBusLineItem : busStep.getBusLines()) {
                                    flag=flag+1;
                                    //当info信息不等于为空时执行一下代码
                                    if (!info.getInfo().equals("")){
                                        info.setInfo(info.getInfo()+"或者"+routeBusLineItem.getBusLineName());
                                    }else{
                                        info.setInfo(routeBusLineItem.getBusLineName());
                                    }
                                    /**
                                     * 放入公交起始站
                                     */
                                    info.setStartStation(routeBusLineItem.getDepartureBusStation().getBusStationName());
                                    /**
                                     * 放入公交结束站
                                     */
                                    info.setEndStartion(routeBusLineItem.getArrivalBusStation().getBusStationName());
                                    //放入公交总站数
                                    info.setStationNum(routeBusLineItem.getPassStationNum()+"");
                                    //放入公交起始Location
                                    info.setBusStartPoint(routeBusLineItem.getDepartureBusStation().getLatLonPoint());
                                    //放入公交结束Location
                                    info.setBusEndPoint(routeBusLineItem.getArrivalBusStation().getLatLonPoint());
                                    //当falg大于等于1并且flag小于该步骤的公交线路总数时，执行以下代码
                                    if (flag>=1&&flag<busStep.getBusLines().size()){
                                        if (routeBusLineItem.getBusLineName().contains("(")){
                                            busn+=(routeBusLineItem.getBusLineName().split("\\(")[0]+"/");
                                        }else{
                                            busn+=(routeBusLineItem.getBusLineName()+"/");
                                        }
                                    }else{
                                        if (routeBusLineItem.getBusLineName().contains("(")){
                                            busn+=(routeBusLineItem.getBusLineName().split("\\(")[0]);
                                        }else{
                                            busn+=(routeBusLineItem.getBusLineName());
                                        }
                                    }
                                }
                                //加入info集合
                                infos.add(info);
                            }
                            //放入步骤模型
                            busStep1.setInfo(infos);
                            //添加步骤集合
                            Steps.add(busStep1);
                            //添加公交名称集合
                            if (!busn.equals("")){
                                busname.add(busn);
                            }
                        }
                        //放入公交名称集合
                        path.setBusNames(busname);
                        //放入步骤集合
                        path.setSteps(Steps);
                        //加入路线集合
                        paths.add(path);
                    }
                    //放入数据模型
                    pathModel.setPaths(paths);
                    //通知接口数据获取成功
                    getBus.sussce(pathModel);
                }else{
                    getBus.faild("数据获取失败，ErrorCode:"+i);
                }
            }
            //获取驾驶数据
            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                if (i!=1000){
                    getDruver.faild("数据获取失败，ErrorCode:"+i);
                    return;
                }
                //初始化数据模型
                PathModel pathModel = new PathModel();
                //放入开始坐标
                pathModel.setStart(driveRouteResult.getStartPos());
                //放入结束坐标
                pathModel.setEnd(driveRouteResult.getTargetPos());
                //初始化路径集合
                ArrayList<PathModel.Path> paths = new ArrayList<PathModel.Path>();
                //循环获取路径
                for (DrivePath drivePath : driveRouteResult.getPaths()) {
                    //初始化路径对象
                    PathModel.Path path = new PathModel.Path();
                    //放入路径Path
                    path.setDrivePath(drivePath);
                    //初始化距离 时间信息
                    int dur = (int) drivePath.getDuration();
                    int dis = (int) drivePath.getDistance();
                    String busRotueTitle = SchemeUtil.getDriverPathDes(drivePath);
                    path.setDriverTitle(busRotueTitle);
                    //初始化步骤集合
                    ArrayList<PathModel.Path.BusStep> Steps  = new ArrayList<PathModel.Path.BusStep>();
                    //循环获取详细info
                    for (DriveStep driveStep : drivePath.getSteps()) {
                        //初始化步骤对象
                        PathModel.Path.BusStep busStep1 = new PathModel.Path.BusStep();
                        //初始化对象信息
                        PathModel.Path.BusStep.Info info = new PathModel.Path.BusStep.Info();
                        //放入文字描述
                        info.setInfo(driveStep.getInstruction());
                        //路名
                        info.setRoad(driveStep.getRoad());
                        //行驶做标集合
                        info.setDirverpoint(driveStep.getPolyline());
                        //开始坐标
                        info.setCarStartPoint(driveStep.getPolyline().get(0));
                        //初始化详细信息集合
                        ArrayList<PathModel.Path.BusStep.Info> infos = new ArrayList<PathModel.Path.BusStep.Info>();
                        //存入信息集合
                        infos.add(info);
                        //放入步骤对象
                        busStep1.setInfo(infos);
                        //放如步骤集合
                        Steps.add(busStep1);
                    }
                    path.setSteps(Steps);
                    path.setDrivePath(drivePath);
                    path.setDriver("途径"+drivePath.getSteps().get(0).getRoad()+" "+busRotueTitle);
                    paths.add(path);
                }
                pathModel.setPaths(paths);
                getDruver.sussce(pathModel);
            }

            /**
             * 获取步行数据
             * @param walkRouteResult
             * @param i
             */
            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                if (i!=1000){
                    getFood.faild("数据获取失败，ErrorCode:"+i);
                    return;
                }
                //初始化数据模型
                PathModel pathModel = new PathModel();
                //开始坐标
                pathModel.setStart(walkRouteResult.getStartPos());
                //结束坐标
                pathModel.setEnd(walkRouteResult.getTargetPos());
                //初始化路经集合
                ArrayList<PathModel.Path> paths = new ArrayList<PathModel.Path>();
                //循环获取路径
                for (WalkPath walkPath : walkRouteResult.getPaths()) {
                    //初始化路径对象
                    PathModel.Path path = new PathModel.Path();
                    //放入walkpath
                    path.setWalkPath(walkPath);
                    ///初始化title
                    int dur = (int) walkPath.getDuration();
                    int dis = (int) walkPath.getDistance();
                    String busRotueTitle = SchemeUtil.getBusRouteTitle(dur,dis);
                    path.setWalkTitle(busRotueTitle);
                    //初始化步骤集合
                    ArrayList<PathModel.Path.BusStep> Steps  = new ArrayList<PathModel.Path.BusStep>();
                    //循环获取详细信息
                    for (WalkStep walkStep : walkPath.getSteps()) {
                        //初始化步骤集合
                        PathModel.Path.BusStep busStep1 = new PathModel.Path.BusStep();
                        //初始化info
                        PathModel.Path.BusStep.Info info = new PathModel.Path.BusStep.Info();
                        //详细文字描述
                        info.setInfo(walkStep.getInstruction());
                        //步行开始坐标
                        info.setWalkStartPoint(walkStep.getPolyline().get(0));
                        //路名
                        info.setRoad(walkStep.getRoad());
                        //详细集合
                        ArrayList<PathModel.Path.BusStep.Info> infos = new ArrayList<PathModel.Path.BusStep.Info>();
                        //加入信息集合
                        infos.add(info);
                        //放入对象
                        busStep1.setInfo(infos);
                        //添加入步骤集合
                        Steps.add(busStep1);
                    }
                    path.setSteps(Steps);
                    paths.add(path);
                }
                pathModel.setPaths(paths);
                getFood.sussce(pathModel);
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }
}

