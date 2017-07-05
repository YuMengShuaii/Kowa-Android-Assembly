package com.nat.android.javashoplib.maputils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.WalkPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LDD on 17/2/22.
 */

public class PathModel {
    /**
     * 路径信息集合
     */
    private ArrayList<Path> paths;
    /**
     * 路径开始的Location
     */
    private LatLonPoint start;
    /**
     * 路径结束的Locaition
     */
    private LatLonPoint end;

    public LatLonPoint getStart() {
        return start;
    }

    public void setStart(LatLonPoint start) {
        this.start = start;
    }

    public LatLonPoint getEnd() {
        return end;
    }

    public void setEnd(LatLonPoint end) {
        this.end = end;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    /**
     * 路径模型
     */
    public static class Path {
        /**
         * 公交path信息
         */
        private BusPath path;
        /**
         * 步行path信息
         */
        private WalkPath walkPath;
        /**
         * 自驾path信息
         */
        private DrivePath drivePath;
        /**
         * 驾驶标题信息
         */
        private String driver;
        /**
         * 公交标题信息
         */
        private String busRotueTitle;
        /**
         * 驾驶路径信息
         */
        private String driverTitle;
        /**
         * 步行路径标题
         */
        private String WalkTitle;
        /**
         * 公交步骤信息集合
         */
        private ArrayList<BusStep> Steps;
        /**
         * 所有公交线路信息集合
         */
        private ArrayList<String>  BusNames;

        /**
         * 获取bean中所有info信息
         * @return 所有info信息
         */
        public ArrayList<BusStep.Info> getAllInfo(){
            ArrayList<BusStep.Info> infos = new ArrayList<>();
            for (int i = 0; i < Steps.size(); i++) {
                infos.addAll(Steps.get(i).getInfo());
            }
            return infos;
        }
        public WalkPath getWalkPath() {
            return walkPath;
        }

        public void setWalkPath(WalkPath walkPath) {
            this.walkPath = walkPath;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getWalkTitle() {
            return WalkTitle;
        }

        public void setWalkTitle(String walkTitle) {
            WalkTitle = walkTitle;
        }

        public String getDriverTitle() {
            return driverTitle;
        }

        public void setDriverTitle(String driverTitle) {
            this.driverTitle = driverTitle;
        }

        public DrivePath getDrivePath() {
            return drivePath;
        }

        public void setDrivePath(DrivePath drivePath) {
            this.drivePath = drivePath;
        }

        public ArrayList<String> getBusNames() {
            return BusNames;
        }

        public void setBusNames(ArrayList<String> busNames) {
            BusNames = busNames;
        }

        public BusPath getPath() {
            return path;
        }

        public void setPath(BusPath path) {
            this.path = path;
        }

        public String getBusRotueTitle() {
            return busRotueTitle;
        }

        public void setBusRotueTitle(String busRotueTitle) {
            this.busRotueTitle = busRotueTitle;
        }

        public ArrayList<BusStep> getSteps() {
            return Steps;
        }

        public void setSteps(ArrayList<BusStep> steps) {
            Steps = steps;
        }

        /**
         * 路径步骤模型
         */
        public static class BusStep {
            /**
             * 步行开始Location
             */
            private LatLonPoint walkStartPoint;
            /**
             * 步行结束Location
             */
            private LatLonPoint walkEndPoint;
            /**
             * 该步骤中包含的信息集合
             */
            private ArrayList<Info> Info;

            public LatLonPoint getWalkStartPoint() {
                return walkStartPoint;
            }

            public void setWalkStartPoint(LatLonPoint walkStartPoint) {
                this.walkStartPoint = walkStartPoint;
            }

            public LatLonPoint getWalkEndPoint() {
                return walkEndPoint;
            }

            public void setWalkEndPoint(LatLonPoint walkEndPoint) {
                this.walkEndPoint = walkEndPoint;
            }

            public ArrayList<Info> getInfo() {
                return Info;
            }

            public void setInfo(ArrayList<Info> info) {
                Info = info;
            }

            /**
             * 路径详细信息
             */
            public static class Info {
                /**
                 * 详细文字信息
                 */
                private String info="";
                /**
                 * 公交总站数
                 */
                private String stationNum="";
                /**
                 * 公交乘坐站
                 */
                private String startStation="";
                /**
                 * 经过的路名
                 */
                private String road="";
                /**
                 * 公交下车站
                 */
                private String endStartion="";
                /**
                 * 步行起始Location
                 */
                private LatLonPoint walkStartPoint;
                /**
                 * 步行结束Location
                 */
                private LatLonPoint walkEndPoint;
                /**
                 * 公交起始Location
                 */
                private LatLonPoint busStartPoint;
                /**
                 * 公交结束Location
                 */
                private LatLonPoint busEndPoint;
                /**
                 * 自驾起始Location
                 */
                private LatLonPoint carStartPoint;

                /**
                 * 自驾Location集合
                 */
                private List<LatLonPoint> dirverpoint;



                public LatLonPoint getCarStartPoint() {
                    return carStartPoint;
                }

                public void setCarStartPoint(LatLonPoint carStartPoint) {
                    this.carStartPoint = carStartPoint;
                }


                public List<LatLonPoint> getDirverpoint() {
                    return dirverpoint;
                }

                public void setDirverpoint(List<LatLonPoint> dirverpoint) {
                    this.dirverpoint = dirverpoint;
                }


                public String getRoad() {
                    return road;
                }

                public void setRoad(String road) {
                    this.road = road;
                }

                public LatLonPoint getWalkStartPoint() {
                    return walkStartPoint;
                }

                public void setWalkStartPoint(LatLonPoint walkStartPoint) {
                    this.walkStartPoint = walkStartPoint;
                }

                public LatLonPoint getWalkEndPoint() {
                    return walkEndPoint;
                }

                public void setWalkEndPoint(LatLonPoint walkEndPoint) {
                    this.walkEndPoint = walkEndPoint;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getStationNum() {
                    return stationNum;
                }

                public void setStationNum(String stationNum) {
                    this.stationNum = stationNum;
                }

                public String getStartStation() {
                    return startStation;
                }

                public void setStartStation(String startStation) {
                    this.startStation = startStation;
                }

                public String getEndStartion() {
                    return endStartion;
                }

                public void setEndStartion(String endStartion) {
                    this.endStartion = endStartion;
                }
                public LatLonPoint getBusStartPoint() {
                    return busStartPoint;
                }

                public void setBusStartPoint(LatLonPoint busStartPoint) {
                    this.busStartPoint = busStartPoint;
                }

                public LatLonPoint getBusEndPoint() {
                    return busEndPoint;
                }

                public void setBusEndPoint(LatLonPoint busEndPoint) {
                    this.busEndPoint = busEndPoint;
                }
            }
        }
    }
}
