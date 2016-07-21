package com.feicuiedu.treasure.treasure.home.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * 宝藏详情业务视图
 * 作者：yuanchao on 2016/7/21 0021 14:07
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface TreasureDetailView extends MvpView{

    void showMessage(String msg);

    void setData(List<TreasureDetailResult> results);
}
