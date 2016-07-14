package com.feicuiedu.treasure.user.register;

import android.os.Handler;
import android.os.Looper;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.user.User;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/12 0012.
 *
 * 注册相关业务, 怎么和视图结合 ????
 *
 *
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView>{

    private Handler handler = new Handler(Looper.getMainLooper());

    private Call call;
    private Gson gson;

    public RegisterPresenter(){
        gson = new Gson();
    }

    /** 本类核心业务*/
    public void regiser(User user) {
        OkHttpClient client = NetClient.getInstance().getClient();

        RequestBody body = RequestBody.create(null, gson.toJson(user));

        Request request = new Request.Builder()
                .url("http://admin.syfeicuiedu.com/Handler/UserHandler.ashx?action=register")
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(callback);
    }

    private Callback callback = new Callback() {
        @Override public void onFailure(Call call, IOException e) {
            failure(e.getMessage());
        }

        @Override public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){
                String jsonStr = response.body().string();
                RegisterResult result = gson.fromJson(jsonStr, RegisterResult.class);
                if(result.getCode() == 1){
                    success(result.getMsg());
                    return;
                }
                failure(result.getMsg());
                return;
            }
            failure("unknown error");
        }
    };

    private void failure(final String msg){
        handler.post(new Runnable() {
            @Override public void run() {
                getView().hideProgress();
                getView().showMessage(msg);
            }
        });
    }

    private void success(final String msg){
        handler.post(new Runnable() {
            @Override public void run() {
                getView().hideProgress();
                getView().showMessage(msg);
                getView().navigateToHome();
            }
        });
    }

}
