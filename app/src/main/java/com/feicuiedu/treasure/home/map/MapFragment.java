package com.feicuiedu.treasure.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.feicuiedu.treasure.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：yuanchao on 2016/7/18 0018 14:05
 * 邮箱：yuanchao@feicuiedu.com
 */
public class MapFragment extends Fragment {

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // 初始化百度地度
        initBaiduMap();
    }

    @Bind(R.id.map_frame)FrameLayout mapFrame;
    private MapView mapView; // 地图视图
    private BaiduMap baiduMap; // 地图视图操作类

    private void initBaiduMap() {
        // 状态
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(15)
                .overlook(-20) // (0) - (-30)
                .build();
        // 设置
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus) // 地图相关状态
                .zoomControlsEnabled(false); // 缩放(因为我们自己的UI上有)
        // 地图视图
        mapView = new MapView(getActivity(), options);
        // 拿到当前MapView的控制器
        baiduMap = mapView.getMap();
        // 在当前Layout上添加MapView
        mapFrame.addView(mapView,0);
    }
}
