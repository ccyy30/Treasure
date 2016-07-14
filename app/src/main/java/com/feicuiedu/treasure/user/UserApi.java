package com.feicuiedu.treasure.user;

import com.feicuiedu.treasure.user.login.LoginResult;
import com.feicuiedu.treasure.user.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * 将用户模块API，转为Java接口
 *
 * 作者：yuanchao on 2016/7/14 0014 11:38
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);
}