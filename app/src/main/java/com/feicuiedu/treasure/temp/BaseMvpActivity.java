package com.feicuiedu.treasure.temp;

import android.support.v7.app.AppCompatActivity;

/**
 * 作者：yuanchao on 2016/7/13 0013 10:02
 * 邮箱：yuanchao@feicuiedu.com
 */
public abstract class BaseMvpActivity<v extends BaseMvpView, p extends BaseMvpPresenter<v>> extends AppCompatActivity {

    public abstract p createPresenter();
}