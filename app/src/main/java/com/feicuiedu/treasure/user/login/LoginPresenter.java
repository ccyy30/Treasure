package com.feicuiedu.treasure.user.login;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.user.User;
import com.feicuiedu.treasure.user.UserApi;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2016/7/12 0012.
 * <p/>
 * 登陆视图业务
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    private Call<LoginResult> loginCall;

    /**
     * 本类核心业务
     */
    public void login(User user) {
        UserApi userApi = NetClient.getInstance().getUserApi();
        if (loginCall != null) loginCall.cancel();
        // 执行登陆请求构建出call模型
        loginCall = userApi.login(user);
        // Call模型异步执行
        loginCall.enqueue(callback);
    }

    private Callback<LoginResult> callback = new Callback<LoginResult>() {
        @Override public void onResponse(Call<LoginResult> call, retrofit2.Response<LoginResult> response) {
            getView().hideProgress();
            // 是否成功
            if (response.isSuccessful()) {
                // 取出响应体(retrofit已加了gson转换器的,注意接口的定义)
                LoginResult result = response.body();
                if (result == null) {
                    getView().showMessage("unknown error");
                    return;
                }
                getView().showMessage(result.getMsg());
                // 登陆成功
                if (result.getCode() == 1) {
                    getView().navigateToHome();
                }
                return;
            }
        }

        @Override public void onFailure(Call<LoginResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (loginCall != null) {
            loginCall.cancel();
        }
    }
}