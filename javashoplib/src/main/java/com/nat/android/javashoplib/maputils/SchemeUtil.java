package com.nat.android.javashoplib.maputils;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.RouteBusLineItem;

import java.text.DecimalFormat;
import java.util.List;

public class SchemeUtil {
	public static String getBusPathTitle(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		List<BusStep> busSetps = busPath.getSteps();
		if (busSetps == null) {
			return String.valueOf("");
		}
		StringBuffer sb = new StringBuffer();
		for (BusStep busStep : busSetps) {
			RouteBusLineItem busline = busStep.getBusLine();
			if (busline == null) {
				continue;
			}
			String buslineName = getSimpleBusLineName(busline.getBusLineName());
			sb.append(buslineName);
			sb.append(">");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String getBusPathDes(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		long second = busPath.getDuration();
		String time = getFriendlyTime((int) second);
		float subDistance = busPath.getDistance();
		String subDis = getFriendlyLength((int) subDistance);
		float walkDistance = busPath.getWalkDistance();
		String walkDis = getFriendlyLength((int) walkDistance);
		return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
	}
	public static String getDriverPathDes(DrivePath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		long second = busPath.getDuration();
		String time = getFriendlyTime((int) second);
		float subDistance = busPath.getDistance();
		String subDis = getFriendlyLength((int) subDistance);
		float walkDistance = busPath.getDistance();
		String walkDis = getFriendlyLength((int) walkDistance);
		return String.valueOf(time + " | " + subDis);
	}

	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) { // 10 km
			int dis = lenMeter / 1000;
			return dis + MapConst.Kilometer;
		}
		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + MapConst.Kilometer;
		}
		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + MapConst.Meter;
		}
		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}
		return dis + MapConst.Meter;
	}

	public static String getFriendlyTime(int second) {
		if (second > 3600) {
			int hour = second / 3600;
			int miniate = (second % 3600) / 60;
			return hour + "小时" + miniate + "分钟";
		}
		if (second >= 60) {
			int miniate = second / 60;
			return miniate + "分钟";
		}
		return second + "秒";
	}

	public static String getSimpleBusLineName(String busLineName) {
		if (busLineName == null) {
			return String.valueOf("");
		}
		return busLineName.replaceAll("\\(.*?\\)", "");
	}
	public static int calculateLineDistance(LatLonPoint start, LatLonPoint end) {
		int distance = 0;
		if (start == null || end == null) {
			return distance;
		}
		double startLat = start.getLatitude();
		double startLon = start.getLongitude();
		double endLat = end.getLatitude();
		double endLon = end.getLongitude();
		LatLng amapStart = new LatLng(startLat, startLon);
		LatLng amapEnd = new LatLng(endLat, endLon);
		return (int) AMapUtils.calculateLineDistance(amapStart, amapEnd);
	}


	public static String getBusRouteTitle(int duration, int distance) {
		String dur = SchemeUtil.getFriendlyTime(duration);
		String dis = SchemeUtil.getFriendlyLength(distance);
		return dur + "(步行" + dis + ")";
	}
}
