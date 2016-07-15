package com.feicuiedu.treasure.user;

import com.feicuiedu.treasure.user.account.UploadResult;
import com.feicuiedu.treasure.user.login.LoginResult;
import com.feicuiedu.treasure.user.register.RegisterResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    // 头像上传(是一个多部分请求)
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part MultipartBody.Part part);



}