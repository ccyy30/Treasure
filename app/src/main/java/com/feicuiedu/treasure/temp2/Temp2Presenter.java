package com.feicuiedu.treasure.temp2;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

/**
 * 作者：yuanchao on 2016/7/13 0013 10:21
 * 邮箱：yuanchao@feicuiedu.com
 */
public class Temp2Presenter extends MvpNullObjectBasePresenter<Temp2View>{

    public void loadData(){
        getView().showProgess();
        // ,,,,,,
        getView().hideProgess();
        getView().setData(null);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        // 网络连接关闭
        // 资源回收处理
    }
}
