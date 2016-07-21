package com.feicuiedu.treasure.treasure.home.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.commons.LogUtils;
import com.feicuiedu.treasure.components.TreasureView;
import com.feicuiedu.treasure.treasure.Treasure;
import com.feicuiedu.treasure.treasure.home.map.MapFragment;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 宝藏详情
 */
public class TreasureDetailActivity extends MvpActivity<TreasureDetailView, TreasureDetailPresenter> implements TreasureDetailView {

    private ActivityUtils activityUtils;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.treasureView) TreasureView treasureView;
    @Bind(R.id.frameLayout) FrameLayout frameLayout;
    @Bind(R.id.tv_detail_description) TextView tvDetailDescription;

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
        // 执行业务(获取宝藏详情数据)
        TreasureDetail td = new TreasureDetail(treasure.getId());
        getPresenter().getTreasureDetail(td);
    }

//    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_treasure_info, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 开始导航
    @OnClick(R.id.iv_navigation)
    public void showPopupMenu(View view) {
        // 弹出菜单
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
        // menu项
        popupMenu.inflate(R.menu.menu_navigation);
        popupMenu.show();
    }

    // PopupMenu弹出菜单的监听
    private final PopupMenu.OnMenuItemClickListener menuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            LatLng startPt = MapFragment.getMyLocation();
            String startAdr = MapFragment.getMyAddress();
            LogUtils.d("startAdr:" + startAdr);
            LatLng endPt = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            String endAdr = treasure.getLocation();
            LogUtils.d("endAdr:" + endAdr);
            switch (item.getItemId()) {
                case R.id.walking_navi:
                    startWalkingNavi(startPt,startAdr,endPt,endAdr);
                    break;
                case R.id.biking_navi:
                    startBikingNavi(startPt, startAdr,endPt,endAdr);
                    break;
            }
            return false;
        }
    };

    @NonNull @Override public TreasureDetailPresenter createPresenter() {
        return new TreasureDetailPresenter();
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
                .compassEnabled(false)// compass
                .scrollGesturesEnabled(false)   // scroll
                .zoomControlsEnabled(false)// zoom
                .zoomGesturesEnabled(false)// zoom
                .rotateGesturesEnabled(false)//rotate
                .scaleControlEnabled(false);//scale
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

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override public void setData(List<TreasureDetailResult> results) {
        if (results.size() >= 1) {
            TreasureDetailResult result = results.get(0);
            tvDetailDescription.setText(result.description);
            return;
        }
        activityUtils.showToast("没有记录");
    }

    // -----------------------------
    /**
     * 启动百度地图步行导航(Native)
     */
    public void startWalkingNavi(LatLng startPt, String startAdr, LatLng endPt, String endAdr) {
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(startPt).endPoint(endPt)
                .startName(startAdr).endName(endAdr);
        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 启动百度地图骑行导航(Native)
     */
    public void startBikingNavi(LatLng startPt, String startAdr, LatLng endPt, String endAdr) {
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(startPt).endPoint(endPt)
                .startName(startAdr).endName(endAdr);

        try {
            BaiduMapNavigation.openBaiduMapBikeNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
