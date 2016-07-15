package com.feicuiedu.treasure.user.account;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * 作者：yuanchao on 2016/7/15 0015 11:15
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface AccoutView extends MvpView{

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    /** 更新头像*/
    void updatePhoto(String url);
}
