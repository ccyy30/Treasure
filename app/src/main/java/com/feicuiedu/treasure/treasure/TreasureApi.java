package com.feicuiedu.treasure.treasure;

import com.feicuiedu.treasure.treasure.home.detail.TreasureDetail;
import com.feicuiedu.treasure.treasure.home.detail.TreasureDetailResult;
import com.feicuiedu.treasure.treasure.home.hide.HideTreasure;
import com.feicuiedu.treasure.treasure.home.hide.HideTreasureResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 作者：yuanchao on 2016/7/19 0019 10:46
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface TreasureApi {

    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area body);

    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure body);

    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail body);
}