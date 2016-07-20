package com.feicuiedu.treasure.treasure.home.hide;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * 作者：yuanchao on 2016/7/20 0020 17:17
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface HideTreasureView extends MvpView{

    void showProgress();
    void hideProgress();
    void showMessage(String msg);
    void navigateToHome();
}
