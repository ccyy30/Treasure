package com.feicuiedu.treasure.treasure.home.map;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.treasure.R;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class CurtomClusterItem implements ClusterItem {

    private final LatLng latLng;
    private final int id;
    private static final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.treasure_dot);

    public CurtomClusterItem(LatLng latLng, int id) {
        this.latLng = latLng;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override public LatLng getPosition() {
        return latLng;
    }

    @Override public BitmapDescriptor getBitmapDescriptor() {
        return icon;
    }
}
