package com.feicuiedu.treasure.temp;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * 作者：yuanchao on 2016/7/12 0012 17:38
 * 邮箱：yuanchao@feicuiedu.com
 */
public class TempPresenter {
    private TempView tempView;

    public TempPresenter(TempView tempView) {
        this.tempView = tempView;
    }

    public void loadData() {
        tempView.showProgess();
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                ArrayList<String> datas = new ArrayList<String>();
                for (int i = 0; i < 20; i++) {
                    if (i != 2) {
                        datas.add("大家好 " + i);
                    }
                }
                // 仅仅为了处理UI更新时线程问题
                post(datas);
            }
        }).start();

    }

    private final void post(final ArrayList<String> datas){
        handler.post(new Runnable() {
            @Override public void run() {
                // .......
                tempView.hideProgess();
                tempView.setData(datas);
            }
        });
    }
    private Handler handler = new Handler(Looper.getMainLooper());
}