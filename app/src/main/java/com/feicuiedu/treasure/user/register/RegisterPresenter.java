package com.feicuiedu.treasure.user.register;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.user.User;
import com.feicuiedu.treasure.user.UserApi;
import com.feicuiedu.treasure.user.UserPrefs;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2016/7/12 0012.
 * <p/>
 * 注册相关业务, 怎么和视图结合 ????
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private Call<RegisterResult> registerCall;

    /**
     * 本类核心业务
     */
    public void regiser(User user) {
        getView().showProgress();
        UserApi userApi = NetClient.getInstance().getUserApi();
        registerCall = userApi.register(user);
        registerCall.enqueue(callback);
    }

    private Callback<RegisterResult> callback = new Callback<RegisterResult>() {
        @Override
        public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
            getView().hideProgress();
            // 成功得到响应 (200 - 299)
            if (response != null && response.isSuccessful()) {
                final RegisterResult result = response.body();
                if (result == null) {
                    getView().showMessage("unknown error");
                    return;
                }
                // 注册成功(@see 接口文档)
                if (result.getCode() == 1) {
                    UserPrefs.getInstance().setTokenid(result.getTokenId());
                    getView().navigateToHome();
                    return;
                }
                getView().showMessage(result.getMsg());
            }
        }

        @Override
        public void onFailure(Call<RegisterResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (registerCall != null) {
            registerCall.cancel();
        }
    }
}
