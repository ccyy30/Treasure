package com.feicuiedu.treasure.treasure.home.hide;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.treasure.TreasureApi;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：yuanchao on 2016/7/20 0020 17:19
 * 邮箱：yuanchao@feicuiedu.com
 */
public class HideTreasurePresenter extends MvpNullObjectBasePresenter<HideTreasureView>{

    private Call<HideTreasureResult> hideCall;

    /**
     * 本类核心业务
     */
    public void hideTreasure(HideTreasure hideTreasure) {
        getView().showProgress();
        TreasureApi treasureApi = NetClient.getInstance().getTreasureApi();
        if(hideCall != null)hideCall.cancel();
        hideCall = treasureApi.hideTreasure(hideTreasure);
        hideCall.enqueue(callBack);
    }

    private final Callback<HideTreasureResult> callBack = new Callback<HideTreasureResult>() {
        @Override public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
            getView().hideProgress();
            if(response != null && response.isSuccessful()){
                // 得到响应结束
                HideTreasureResult result = response.body();
                if (result == null) {
                    getView().showMessage("unknown erro");
                    return ;
                }
                getView().showMessage(result.getMsg());
                // 成功
                if(result.code == 1){
                    getView().navigateToHome();
                }
            }
        }

        @Override public void onFailure(Call<HideTreasureResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (hideCall != null) {
            hideCall.cancel();
        }
    }
}
