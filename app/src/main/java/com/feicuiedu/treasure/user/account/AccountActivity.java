package com.feicuiedu.treasure.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.components.IconSelectWindow;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人用户信息页面
 */
public class AccountActivity extends MvpActivity<AccoutView,AccountPresenter> implements AccoutView{

    @Bind(R.id.toolbar) Toolbar toolbar;

    private ActivityUtils activityUtils;
    private IconSelectWindow iconSelectWindow; // 按下icon，弹出的POPUOWINDOW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_account);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());
    }

    @NonNull @Override public AccountPresenter createPresenter() {
        return new AccountPresenter();
    }

    /**
     * 当在当前个人用户中心页面，按下icon，弹出POPUOWINDOW
     */
    @OnClick(R.id.iv_userIcon)
    public void onClick() {
        if (iconSelectWindow == null) iconSelectWindow = new IconSelectWindow(this, listener);
        if (iconSelectWindow.isShowing()) {
            iconSelectWindow.dismiss();
            return;
        }
        iconSelectWindow.show();
    }

    private CropHandler cropHandler = new CropHandler() {
        // 剪切完成
        @Override public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            // 执行头像上传业务
            getPresenter().uploadPhoto(file);
        }
        // 剪切取消
        @Override public void onCropCancel() {
            activityUtils.showToast("onCropCancel");
        }
        // 剪切失败
        @Override public void onCropFailed(String message) {
            activityUtils.showToast("onCropFailed");
        }

        @Override public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 300;
            cropParams.aspectY = 300;
            return cropParams;
        }

        @Override public Activity getContext() {
            return AccountActivity.this;
        }
    };

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler,requestCode, resultCode, data);
    }

    private final IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        // 到相册进行头像选择
        @Override public void toGallery() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }
        // 到相机进行头像选择
        @Override public void toCamera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    private ProgressDialog progressDialog;
    @Override public void showProgress() {
        progressDialog = ProgressDialog.show(this,"","头像更新中,请稍后...");
    }

    @Override public void hideProgress() {
        if(progressDialog != null)progressDialog.dismiss();
    }

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override public void updatePhoto(String url) {

    }
}
