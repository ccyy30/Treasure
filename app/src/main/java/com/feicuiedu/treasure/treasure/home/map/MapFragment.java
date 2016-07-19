package com.feicuiedu.treasure.treasure.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.treasure.Area;
import com.feicuiedu.treasure.treasure.Treasure;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 第100行待更新
 * <p/>
 * 1. MapView基本操作
 * 2. 定位图层(当前我的位置,请先确认你已集成定位sdk包)
 * 2.1 开启定位图层 (?, 地图是图层有概念)
 * 2.2 设置你当前的位置在哪里 ()
 * 2.2.1 定位SDK初始化
 * 2.2.2 开始定位，及监听处理
 * 2.2.3 成功定位 -经纬度- 设置2.2
 * 2.3 将当前地图状态设置到这里去(将地图移动到你当前所在位置)
 * <p/>
 * 作者：yuanchao on 2016/7/18 0018 14:05
 * 邮箱：yuanchao@feicuiedu.com
 */
public class MapFragment extends MvpFragment<MapMvpView, MapPresenter> implements MapMvpView {

    private ActivityUtils activityUtils;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityUtils = new ActivityUtils(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override public MapPresenter createPresenter() {
        return new MapPresenter();
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // 初始百度地度
        initBaiduMap();
        // 初始定位相关
        initLocation();
    }

    @Bind(R.id.map_frame) FrameLayout mapFrame;
    private MapView mapView; // 地图视图
    private BaiduMap baiduMap; // 地图视图操作类

    private void initBaiduMap() {
        // 状态
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(15)//3~21
                .overlook(0) // (0) - (-45)
                .build();
        // 设置
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus) // 地图相关状态
                .overlookingGesturesEnabled(false) // 俯仰关闭
                .zoomControlsEnabled(false); // 缩放(因为我们自己的UI上有)
        // 地图视图
        mapView = new MapView(getActivity(), options);
        // 在当前Layout上添加MapView
        mapFrame.addView(mapView, 0);
        // 拿到当前MapView的控制器
        baiduMap = mapView.getMap();

        // 对Marker的监听
        baiduMap.setOnMarkerClickListener(markerClickListener);
        // 对地图状态进行监听
        baiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
    }

    // 定位核心API
    private LocationClient locationClient;
    // 我的位置(通过定位得到的当前位置经纬度)
    private LatLng myLocation;

    private void initLocation() {
        // 激活定位图层
        baiduMap.setMyLocationEnabled(true);
        // 定位实例化
        locationClient = new LocationClient(getActivity().getApplicationContext());
        // 进行一些定位的一般常规性设置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setScanSpan(60000);// 扫描周期
        option.setCoorType("bd09ll");// 百度坐标类型
        locationClient.setLocOption(option);
        // 注册定位监听
        locationClient.registerLocationListener(locationListener);
        // 开始定位
        locationClient.start();
        locationClient.requestLocation(); // 请求位置(解决部分机器,初始定位不成功问题)
    }

    private final BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private final BitmapDescriptor iconExpanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);
    // 定位监听
    private final BDLocationListener locationListener = new BDLocationListener() {
        @Override public void onReceiveLocation(BDLocation bdLocation) {
            // 定位不成功 -- 最好UI上有表现
            if (bdLocation == null) {
                locationClient.requestLocation();
                return;
            }
            double lng = bdLocation.getLongitude();// 经度
            double lat = bdLocation.getLatitude();// 纬度
            myLocation = new LatLng(lat, lng);
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .longitude(lng)
                    .latitude(lat)
                    .accuracy(100f) // 精度（圈大小）
                    .build();
            // 设置定位图层“我的位置”
            baiduMap.setMyLocationData(myLocationData);
            // 移动到我的位置上去
            animateMoveToMyLocation();
        }
    };

    @OnClick(R.id.tv_located)
    public void animateMoveToMyLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(myLocation)// 当前位置
                .rotate(0)// 地图摆正
                .zoom(19)
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(update);
    }

    // 地图缩放操作
    @OnClick({R.id.iv_scaleDown, R.id.iv_scaleUp})
    public void scaleMap(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    // 地图类别更新
    @OnClick(R.id.tv_satellite)
    public void switchMapType() {
        int type = baiduMap.getMapType();
        type = type == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        baiduMap.setMapType(type);
    }

    // 指南针更新
    @OnClick(R.id.tv_compass)
    public void switchCompass() {
        boolean isCompass = baiduMap.getUiSettings().isCompassEnabled();
        baiduMap.getUiSettings().setCompassEnabled(!isCompass);
    }

    // 对地图状态进行监听(缩放?移动等等)
    private final BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override public void onMapStatusChangeFinish(MapStatus mapStatus) {
            updateMapArea();
        }
    };

    /**
     * 更新地图区域，重新执行业务获取宝藏(目前是只要移动，就会重新获取,会N次获取同区域数据)
     */
    private void updateMapArea() {
        // 先得到你当前所在位置
        MapStatus mapStatus = baiduMap.getMapStatus();
        double lng = mapStatus.target.longitude;
        double lat = mapStatus.target.latitude;
        // 计算出你的Area  23.999  15.130
        //              24,23  ,  16,15去确定Area
        Area area = new Area();
        area.setMaxLat(Math.ceil(lat));  // lat向上取整
        area.setMaxLng(Math.ceil(lng));  // lng向上取速
        area.setMinLat(Math.floor(lat));  // lat向下取整
        area.setMinLng(Math.floor(lng));  // lng向下取整
        // 执行业务,根据Area去获取宝藏
        getPresenter().getTreasure(area);
    }

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override public void setData(List<Treasure> datas) {
        // 将业务获取到的“附近宝藏”添加为一个个的Marker
        for (Treasure treasure : datas) {
            LatLng position = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            addMarker(position);
        }
    }

    private void addMarker(final LatLng position) {
        // 测试代码(添加Marker)-------------------------------------------
        // 显示出一个Marker(标记)
        MarkerOptions options = new MarkerOptions();
        options.position(position);// 设置Marker位置
        options.icon(dot);// 设置Marker图标
        options.anchor(0.5f, 0.5f);// 设置Marker的锚点(中)
        baiduMap.addOverlay(options); // 添加孚盖物
    }

    private Marker currentMarker;
    // 对Marker的监听
    private final BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override public boolean onMarkerClick(Marker marker) {
            currentMarker = marker;
            // 设置Marker不可见
            currentMarker.setVisible(false);
            InfoWindow infoWindow = new InfoWindow(iconExpanded, marker.getPosition(), 0, infoWindowClickListener);
            // 显示一个信息窗口(icon,位置,Y,监听)
            baiduMap.showInfoWindow(infoWindow);
            return false;
        }
    };

    // 对InfoWindow的监听
    private final InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        @Override public void onInfoWindowClick() {
            currentMarker.setVisible(true);
            baiduMap.hideInfoWindow();
        }
    };
}
