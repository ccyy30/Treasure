package com.feicuiedu.treasure.user.login;

import android.os.AsyncTask;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

/**
 * Created by Administrator on 2016/7/12 0012.
 *
 * 登陆视图业务
 *
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    /** 本类核心业务*/
    public void login(){
        new LoginTask().execute();
    }

    private final class LoginTask extends AsyncTask<Void,Void,Integer>{
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