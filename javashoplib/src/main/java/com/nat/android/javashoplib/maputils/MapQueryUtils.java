package com.nat.android.javashoplib.maputils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;

/**
 * Created by LDD on 17/2/24.
 */

public class MapQueryUtils {

    /**
     * 查询商家
     * @param context         上下文
     * @param location        中心坐标
     * @param pageNum         分页数
     * @param condition       关键字
     * @param city            搜索的城市
     * @param findBound       搜索的范围
     * @param filterKey       过滤属性名
     * @param filterValue     过滤属性值
     * @param mapQueryLisener 监听
     */
    public static void Query(final Context context, AMapLocation location, int pageNum, String condition,String city,int findBound,String filterKey,String filterValue,final Listener.DataLisener<CloudResult> mapQueryLisener){
        if (findBound==0){
            findBound=5000;
        }
        CloudSearch cloudSearch = new CloudSearch(context);
        CloudSearch.SearchBound bound;
        if (city!=null&&!city.equals("")){
            bound = new CloudSearch.SearchBound(city);
        }else{
            bound = new CloudSearch.SearchBound(new LatLonPoint(location.getLatitude(),location.getLongitude()), findBound);
        }
        cloudSearch.setOnCloudSearchListener(new CloudSearch.OnCloudSearchListener() {
            @Override
            public void onCloudSearched(CloudResult cloudResult, int i) {
                if (cloudResult!=null&&cloudResult.getClouds().size()>0){
                    mapQueryLisener.sussce(cloudResult);
                }else{
                    mapQueryLisener.faild("查询数据为空!");
                }
            }

            @Override
            public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

            }
        });
        try {
            CloudSearch.Query query =  new CloudSearch.Query(MapConst.TABLEID, condition, bound);
            if (filterKey!=null&&!filterKey.equals("")){
                query.addFilterString(filterKey,filterValue);
            }
            query.setSortingrules(new CloudSearch.Sortingrules("_distance",false));
            query.setPageSize(MapConst.pageSize);
            query.setPageNum(pageNum);
            cloudSearch.searchCloudAsyn(query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地查询
     * @param context         上下文
     * @param pageNum         分页页数
     * @param condition       KeyWord
     * @param city            搜索城市
     * @param filterKey       过滤属性名
     * @param filterValue     过滤属性值
     * @param mapQueryLisener 监听
     */
    public static void QueryLocal(final Context context,int pageNum, String condition,String city,String filterKey,String filterValue,final Listener.DataLisener<CloudResult> mapQueryLisener){
            Query(context,null,pageNum,condition,city,0,filterKey,filterValue,mapQueryLisener);
    }

    /**
     * 周边查询
     * @param context           上下文
     * @param location          中心坐标
     * @param pageNum           分页页数
     * @param condition         关键字
     * @param findBound         搜搜范围 默认5000米
     * @param filterKey         过滤属性名
     * @param filterValue       过滤属性值
     * @param mapQueryLisener   监听
     */
    public static void QueryBound(final Context context, AMapLocation location,int pageNum,String condition,int findBound,String filterKey,String filterValue,final Listener.DataLisener<CloudResult> mapQueryLisener){
           Query(context,location,pageNum,condition,null,findBound,filterKey,filterValue,mapQueryLisener);
    }
}
