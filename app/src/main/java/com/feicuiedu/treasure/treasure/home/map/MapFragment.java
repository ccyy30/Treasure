package com.feicuiedu.treasure.treasure.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.components.TreasureView;
import com.feicuiedu.treasure.treasure.Area;
import com.feicuiedu.treasure.treasure.Treasure;
import com.feicuiedu.treasure.treasure.TreasureRepo;
import com.feicuiedu.treasure.treasure.home.hide.HideTreasureActivity;
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
        // 地理编码相关
        initGeoCoder();
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
                .compassEnabled(true) // 指南针
                .zoomGesturesEnabled(true) // 设置是否允许缩放手势
                .rotateGesturesEnabled(true) // 设置是否允许旋转手势，默认允许
                .scrollGesturesEnabled(true) // 设置是否允许拖拽手势，默认允许
                .scaleControlEnabled(false) // 设置是否显示比例尺控件
                .overlookingGesturesEnabled(false) // 设置是否允许俯视手势，默认允许
                .zoomControlsEnabled(false) // 设置是否显示缩放控件
                ;
        // 地图视图
        mapView = new MapView(getActivity(), options);
        // 在当前Layout上添加MapView
        mapFrame.addView(mapView, 0);
        // 拿到当前MapView的控制器
        baiduMap = mapView.getMap();
        // 对Marker的监听
        baiduMap.setOnMarkerClickListener(markerClickListener);
        // 对地图状态进行监听 (将开始进行区域内宝藏数据的获取)
        baiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
    }

    // 定位服务的客户端。只支持在主线程中启动
    private LocationClient locationClient;
    // 我的位置(通过定位得到的当前位置经纬度)
    private static LatLng myLocation;

    private void initLocation() {
        // 激活定位图层
        baiduMap.setMyLocationEnabled(true);
        // 定位实例化
        locationClient = new LocationClient(getActivity().getApplicationContext());
        // 进行一些定位的一般常规性设置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setScanSpan(60000);// 扫描周期,设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setCoorType("bd09ll");// 百度坐标类型
        option.setLocationNotify(true);//设置是否当gps有效时按照1S1次频率输出GPS结果
        option.SetIgnoreCacheException(false);//设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);
        // 注册定位监听
        locationClient.registerLocationListener(locationListener);
        // 开始定位
        locationClient.start();
        locationClient.requestLocation(); // 请求位置(解决部分机器,初始定位不成功问题)
    }

    private GeoCoder geoCoder;
    /**
     * 在埋藏宝藏时，下方卡片上显示的埋藏位置
     */
    @Bind(R.id.tv_currentLocation) TextView tvCurrentLication;
    private String address;

    // 地理编码
    private void initGeoCoder() {
        geoCoder = GeoCoder.newInstance();
        // 监听地理编码
        geoCoder.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
    }

    // 地理编码监听
    private final OnGetGeoCoderResultListener getGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        // 地理编码 (地址 -> 经纬度)
        @Override public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        }

        // 反地理编码 (经纬度 -> 地址)
        @Override public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null) return;
            if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                address = "未知";
            }
            address = reverseGeoCodeResult.getAddress();
            tvCurrentLication.setText(address);
        }
    };

    @Override public void onDestroy() {
        super.onDestroy();
        if (locationListener != null)
            // 取消之前注册的定位监听函数
            locationClient.unRegisterLocationListener(locationListener);
    }

    public static LatLng getMyLocation() {
        return myLocation;
    }

    private final BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);
    private final BitmapDescriptor iconExpanded = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);
    private boolean isFirstLocated = true; // 是否首次定位判断
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
            // 移动到我的位置上去(只有首次定位时,以后通过click定位再移上去)
            if (isFirstLocated) {
                animateMoveToMyLocation();
                isFirstLocated = false;
            }
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

    private LatLng target;
    // 对地图状态进行监听(缩放?移动等等)
    private final BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override public void onMapStatusChange(MapStatus mapStatus) {
        }

        // 只要地图状态(缩放?移动都是)更新结束,就会来调用
        @Override public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng target = mapStatus.target;
            // 的确是位置变化了!
            if (target != MapFragment.this.target) {
                // 更新宝藏数据
                updateMapArea();
                // 在埋藏宝藏模式下
                if (uiMode == UI_MODE_HIDE) {
                    // 反地理编码
                    ReverseGeoCodeOption option = new ReverseGeoCodeOption();
                    option.location(target);
                    geoCoder.reverseGeoCode(option);
                    // 反弹动画
                    YoYo.with(Techniques.Bounce).duration(1000).playOn(btnHideHere);
                    YoYo.with(Techniques.Bounce).duration(1000).playOn(ivLocated);
                    // 淡入动画
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(btnHideHere);
                }
                // 更新当前位置,防止重复的触发
                MapFragment.this.target = target;
            }
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
            addMarker(position, treasure.getId());
        }
    }

    private void addMarker(final LatLng position, final int treasureId) {
        // 测试代码(添加Marker)-------------------------------------------
        // 显示出一个Marker(标记)
        MarkerOptions options = new MarkerOptions();
        options.position(position);// 设置Marker位置
        options.icon(dot);// 设置Marker图标
        options.anchor(0.5f, 0.5f);// 设置Marker的锚点(中)
        // 将当前宝藏的ID号存到Marker里去()
        Bundle bundle = new Bundle();
        bundle.putInt("id", treasureId);
        options.extraInfo(bundle);
        baiduMap.addOverlay(options); // 添加孚盖物
    }

    private Marker currentMarker;
    // 宝藏信息展示卡片
    @Bind(R.id.treasureView) TreasureView treasureView;
    @Bind(R.id.et_treasureTitle) EditText etTreasureTitle;// 信息录入编辑框(埋藏宝藏时)

    // 对Marker的监听
    private final BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override public boolean onMarkerClick(Marker marker) {
            if (currentMarker != null) currentMarker.setVisible(true);
            currentMarker = marker;
            // 设置Marker不可见
            currentMarker.setVisible(false);
            InfoWindow infoWindow = new InfoWindow(iconExpanded, marker.getPosition(), 0, infoWindowClickListener);
            // 显示一个信息窗口(icon,位置,Y,监听)
            baiduMap.showInfoWindow(infoWindow);
            // 从当前Marker中取出这个宝藏的id号(@see addMarker时的操作)
            int id = marker.getExtraInfo().getInt("id");
            // 从宝藏仓库中，根据id号取出宝藏
            Treasure treasure = TreasureRepo.getInstance().getTreasure(id);
            treasureView.bindTreasure(treasure);
            // 更新UI模式(进入选择模式)
            changUiMode(UI_MODE_SELECT);
            return false;
        }
    };

    // 对InfoWindow的监听
    private final InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        @Override public void onInfoWindowClick() {
            // 回到普通模式
            changUiMode(UI_MODE_NORMAL);
        }
    };

    /**
     * 按下back时来调用
     */
    public boolean clickBackPressed() {
        if (this.uiMode != UI_MODE_NORMAL) {
            changUiMode(UI_MODE_NORMAL);
            return false;
        }
        // 当前为普通浏览模式的话，返回true，表明当前fragment不要做其他操作,你可以退出
        return true;
    }

    /**
     * 进入埋藏宝藏模式(在按下藏宝时调用的)
     */
    public void switchToHideTreasure() {
        changUiMode(UI_MODE_HIDE);
    }

    /**
     * 按下宝藏信息展示卡片将进入宝藏详情页
     */
    @OnClick(R.id.treasureView)
    public void clickTreasureView() {
        activityUtils.hideSoftKeyboard();
        activityUtils.showToast("gogogo");
    }
    /**
     * 按下宝藏信息录入卡片将进入埋藏宝藏页面
     */
    @OnClick(R.id.hide_treasure)
    public void clickHideTreasure(){
        // 1. HideTreasureActivity - UI
        // 2. 视图
        // 3. 业务 -- hideTreasure()
        //     根据接口文档去实现
        activityUtils.hideSoftKeyboard();
        String title = etTreasureTitle.getText().toString();
        if(TextUtils.isEmpty(title)){
            activityUtils.showToast(R.string.please_input_title);
            return;
        }
        // 进入埋藏宝藏页面
        LatLng latLng = baiduMap.getMapStatus().target;
        HideTreasureActivity.open(getContext(),title,address,latLng,0);
    }
    /**
     * 宝藏信息提示,默认隐藏的(在屏幕下方位置,包括两种模式下的布局,选中模式时的信息展示卡片,埋藏模式时的信息录入)
     */
    @Bind(R.id.layout_bottom) FrameLayout bottomLayout;
    /**
     * 埋藏宝藏时需要(中心位置藏宝控件)
     */
    @Bind(R.id.centerLayout) RelativeLayout conterLayout;
    /**
     * 埋藏宝藏时的信息录入卡片(在屏幕下方位置)
     */
    @Bind(R.id.hide_treasure) RelativeLayout hideTreasure;
    /**
     * 埋藏宝藏时"藏在这里"的按钮
     */
    @Bind(R.id.btn_HideHere) Button btnHideHere;
    @Bind(R.id.iv_located) ImageView ivLocated;

    private static final int UI_MODE_NORMAL = 0;// 普通
    private static final int UI_MODE_SELECT = 1;// 选中
    private static final int UI_MODE_HIDE = 2; // 埋藏

    private int uiMode = UI_MODE_NORMAL;

    private void changUiMode(int uiMode) {
        if (this.uiMode == uiMode) return;
        this.uiMode = uiMode;
        switch (uiMode) {
            // 进入普通模式(下方布局不可见,藏宝操作布局不可见)
            case UI_MODE_NORMAL:
                if (currentMarker != null) currentMarker.setVisible(true);
                baiduMap.hideInfoWindow();
                bottomLayout.setVisibility(View.GONE);// 隐藏下方的宝藏信息layout
                conterLayout.setVisibility(View.GONE);// 隐藏中间位置藏宝layout
                break;
            // 进入选中模式
            case UI_MODE_SELECT:
                bottomLayout.setVisibility(View.VISIBLE);// 显示下方的宝藏信息layout
                treasureView.setVisibility(View.VISIBLE);// 显示宝藏信息卡片
                conterLayout.setVisibility(View.GONE); // 隐藏中间位置藏宝layout
                hideTreasure.setVisibility(View.GONE); // 隐藏宝藏录入信息卡片
                YoYo.with(Techniques.Bounce).duration(500).playOn(bottomLayout);
                break;
            // 进入埋藏模式
            case UI_MODE_HIDE:
                conterLayout.setVisibility(View.VISIBLE);// 显示中间位置藏宝layout
                bottomLayout.setVisibility(View.GONE);// 隐藏下方的宝藏信息layout
                // 按下藏宝时
                btnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        bottomLayout.setVisibility(View.VISIBLE);// 显示下方的宝藏信息layout
                        hideTreasure.setVisibility(View.VISIBLE);// 显示宝藏录入信息卡片
                        treasureView.setVisibility(View.GONE);// 隐藏宝藏信息卡片
                        // 下方的layout做一个动画
                        YoYo.with(Techniques.FlipInX).duration(500).playOn(bottomLayout);
                    }
                });
                break;
        }
    }
}
