package com.nat.android.javashoplib.maputils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.model.LatLng;

/**
 * Created by LDD on 17/3/1.
 */

public class MapConst {
    /**
     * 自己的坐标位置
     */
    public static AMapLocation mylocation;

    /**
     * 查询分页的大小
     */
    public static int pageSize = 10;

    /**
     * 云图ID
     */
    public static final String TABLEID = "58abae3cafdf520ea88fc350" ;

    /**
     * "公里"的Ascii编码
     */
    public static final String Kilometer = "\u516c\u91cc";// "公里";

    /**
     * "米"Ascii编码
     */
    public static final String Meter = "\u7c73";// "米";
}
