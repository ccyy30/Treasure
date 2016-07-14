package com.feicuiedu.treasure.user.account;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.components.IconSelectWindow;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人用户信息页面
 */
public class AccountActivity extends AppCompatActivity {

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

    private final IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        @Override public void toGallery() {
            activityUtils.showToast("toGallery");
        }

        @Override public void toCamera() {
            activityUtils.showToast("toCamera");
        }
    };
}
