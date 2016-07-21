package com.feicuiedu.treasure.treasure;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.feicuiedu.treasure.treasure.home.map.MapFragment;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**

 * 获取宝藏时的响应体数据 ResponeBody
 * 作者：yuanchao on 2016/7/19 0019 10:40
 * 邮箱：yuanchao@feicuiedu.com
 */
public class Treasure implements Serializable{
//    "htid": 173,                   //宝藏id
//            "htpoi": "北京市大兴区福东街", //位置
//            "htsize": 0,                   //宝物大小
//            "htlevels": 0,                 //水平
//            "htsc": "",
//            "htxline": 116.64923659725756, //经度
//            "htyline": 39.632932212422205, //纬度
//            "htheight": 5e-324,            //海拔
//            "httid": 171,                  //埋藏id
//            "htcreatetime": "2016-04-29T15:12:11.596", //埋藏时间
//            "htuserid": 168,
//            "htisstates": false,
//            "tdid": 171,
//            "tdtname": "老虎",             //宝藏主题
//            "tdtstate": false              //宝藏是否找到

    @SerializedName("tdid")
    private int id;

    @SerializedName("tdtname")
    private String title;

    /**
     * 纬度
     */
    @SerializedName("htyline")
    private double latitude;

    /**
     * 经度
     */
    @SerializedName("htxline")
    private double longitude;

    @SerializedName("htheight")
    private double altitude;

    @SerializedName("htpoi")
    private String location;
    /**
     * 寻找难度
     */
    @SerializedName("htlevels")
    private int level;

    /**
     * 宝物大小
     */
    @SerializedName("htsize")
    private int size;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public double distanceToMyLocation(){
        // 我的当前位置
        LatLng myLocation = MapFragment.getMyLocation();
        if(myLocation == null){
            return 0.00d;
        }
        // 当前宝藏的位置
        LatLng target = new LatLng(latitude, longitude);
        return DistanceUtil.getDistance(target, myLocation);
    };
}
