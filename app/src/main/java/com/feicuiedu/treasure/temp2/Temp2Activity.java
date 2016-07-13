package com.feicuiedu.treasure.temp2;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

/**
 * 作者：yuanchao on 2016/7/13 0013 10:26
 * 邮箱：yuanchao@feicuiedu.com
 */
public class Temp2Activity extends MvpActivity<Temp2View,Temp2Presenter> implements Temp2View{


    public void loginClick(){
        getPresenter().loadData();
    }

    @NonNull @Override public Temp2Presenter createPresenter() {
        return new Temp2Presenter();
    }

    @Override public void showProgess() {

    }

    @Override public void hideProgess() {

    }

    @Override public void setData(List<String> datas) {

    }
}
