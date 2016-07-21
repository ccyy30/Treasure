package com.feicuiedu.treasure.treasure.home.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.components.TreasureView;
import com.feicuiedu.treasure.treasure.Treasure;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 宝藏详情
 */
public class TreasureDetailActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.treasureView) TreasureView treasureView;
    @Bind(R.id.frameLayout) FrameLayout frameLayout;

    private final BitmapDescriptor mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);

    private Treasure treasure;

    private static final String KEY_TREASURE = "key_treasure";

    public static void open(@NonNull Context context, @NonNull Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(KEY_TREASURE, treasure);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_treasure_detail);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        treasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);
        // ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(treasure.getTitle());
        // Treasure
        treasureView.bindTreasure(treasure);
        // MapView
        initMap();
    }

    private void initMap() {
        LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .overlook(-20)
                .zoom(18)
                .build();
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .overlookingGesturesEnabled(false)
                .rotateGesturesEnabled(false)
                .scaleControlEnabled(false);
        MapView mapView = new MapView(this, options);
        frameLayout.addView(mapView);
        // Marker
        BaiduMap baiduMap = mapView.getMap();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .icon(mBitmapDescriptor);
        baiduMap.addOverlay(markerOptions);
    }
}
