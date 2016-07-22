package com.feicuiedu.treasure.treasure.home.map;

import android.content.Context;
import android.os.Bundle;

import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.clusterutil.clustering.view.DefaultClusterRenderer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MarkerOptions;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class CurtomClusterRenderer extends DefaultClusterRenderer<CurtomClusterItem> {

    public CurtomClusterRenderer(Context context, BaiduMap map, ClusterManager<CurtomClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override protected void onBeforeClusterItemRendered(CurtomClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        markerOptions.anchor(0.5f, 0.5f).extraInfo(bundle);
    }
}
