package com.feicuiedu.treasure.user.register;

import android.os.AsyncTask;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

/**
 * Created by Administrator on 2016/7/12 0012.
 *
 * 注册相关业务, 怎么和视图结合 ????
 *
 *
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView>{

    /** 本类核心业务*/
    public void regiser() {
        new RegisterTask().execute();
    }

    private final class RegisterTask extends AsyncTask<Void,Void,Integer> {
        // 在doInBackground之前,UI线程来调用
        @Override protected void onPreExecute() {
            super.onPreExecute();
            getView().showProgress();
        }
        // 在onPreExecute之后, 后台线程来调用
        @Override protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return 0;
            }
            return 1;
        }
        // 在doInBackground之后,UI线程来调用
        @Override protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid == 0) {
                getView().showMessage("未知错误");
                getView().hideProgress();
                return;
            }
            getView().navigateToHome();
            getView().hideProgress();
        }
    }
}
