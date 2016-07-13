package com.feicuiedu.treasure.temp2;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * 作者：yuanchao on 2016/7/13 0013 10:20
 * 邮箱：yuanchao@feicuiedu.com
 */
public interface Temp2View extends MvpView{

    public void showProgess();
    public void hideProgess();
    public void setData(List<String> datas);
}
