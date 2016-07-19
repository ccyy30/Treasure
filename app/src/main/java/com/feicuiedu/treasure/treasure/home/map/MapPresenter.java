package com.feicuiedu.treasure.treasure.home.map;

import com.feicuiedu.treasure.treasure.Area;
import com.feicuiedu.treasure.treasure.Treasure;
import com.feicuiedu.treasure.treasure.TreasureApi;
import com.feicuiedu.treasure.net.NetClient;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：yuanchao on 2016/7/19 0019 10:23
 * 邮箱：yuanchao@feicuiedu.com
 */
public class MapPresenter extends MvpNullObjectBasePresenter<MapMvpView>{

    private Call<List<Treasure>> call;

    /** 核心业务,获取宝藏,指定区域*/
    public void getTreasure(Area area) {
        TreasureApi treasureApi = NetClient.getInstance().getTreasureApi();
        if(call !=null)call.cancel();
        call = treasureApi.getTreasureInArea(area);
        call.enqueue(callback);
    }

    private final Callback<List<Treasure>> callback = new Callback<List<Treasure>>() {
        @Override public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            if(response != null && response.isSuccessful()){
                List<Treasure> datas = response.body();
                if (datas == null) {
                    getView().showMessage("unknown error");
                    return;
                }
                // 通知视图进行视图工作
                getView().setData(datas);
            }
        }

        @Override public void onFailure(Call<List<Treasure>> call, Throwable t) {
            getView().showMessage(t.getMessage());
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(call != null){
            call.cancel();
        }
    }
}
