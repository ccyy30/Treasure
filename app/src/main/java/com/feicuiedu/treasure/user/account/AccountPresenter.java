package com.feicuiedu.treasure.user.account;

import com.feicuiedu.treasure.net.NetClient;
import com.feicuiedu.treasure.user.UserApi;
import com.feicuiedu.treasure.user.UserPrefs;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人信息页面
 * <p/>
 * 作者：yuanchao on 2016/7/15 0015 11:17
 * 邮箱：yuanchao@feicuiedu.com
 */
public class AccountPresenter extends MvpNullObjectBasePresenter<AccoutView> {

    private Call<UploadResult> uploadCall;
    private String photoUrl;

    /**
     * 上传头像
     */
    public void uploadPhoto(File file) {
        getView().showProgress();
        UserApi userApi = NetClient.getInstance().getUserApi();
        // 构建“部分”
        RequestBody body = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "photo.png", body);
        // 上传头像(我们接口其实只要一个部分(头像文件))
        if (uploadCall != null) uploadCall.cancel();
        uploadCall = userApi.upload(part);
        uploadCall.enqueue(upLoadCallback);
    }

    private Callback<UploadResult> upLoadCallback = new Callback<UploadResult>() {
        @Override public void onFailure(Call<UploadResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }

        @Override public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
            if (response != null && response.isSuccessful()) {// 成功响应
                UploadResult result = response.body();// 取得响应体内数据
                if (result == null) {
                    getView().showMessage("unknown error");
                    return;
                }
                getView().showMessage(result.getMsg());
                if (result.getCount() != 1) { // 上传不成功(@see 接口文档)
                    return;
                }
                photoUrl = result.getUrl(); // 上传后的，头像URL地址
                getView().updatePhoto(photoUrl);// 视图更新头像
                // 向服务器更新用户头像，待完成----------------------------------------------------------
                // 用户头像(在更新用户头像时要用到 @see 接口文档)
                String photoName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1, photoUrl.length());
                // 用户token(在更新用户头像时要用到 @see 接口文档)
                int tokenId = UserPrefs.getInstance().getTokenid();
                // ................
            }
        }
    };

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (uploadCall != null) uploadCall.cancel();
    }
}