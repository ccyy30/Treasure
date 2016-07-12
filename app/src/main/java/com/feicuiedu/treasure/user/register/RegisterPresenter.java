package com.feicuiedu.treasure.user.register;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2016/7/12 0012.
 *
 * 注册相关业务, 怎么和视图结合 ????
 *
 *
 */
public class RegisterPresenter {

    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView){
        this.registerView = registerView;
    }

    /** 本类核心业务*/
    public void regiser() {
        new RegisterTask().execute();
    }

    private final class RegisterTask extends AsyncTask<Void,Void,Integer> {
        // 在doInBackground之前,UI线程来调用
        @Override protected void onPreExecute() {
            super.onPreExecute();
            registerView.showProgress();
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
                registerView.showMessage("未知错误");
                registerView.hideProgress();
                return;
            }
            registerView.navigateToHome();
            registerView.hideProgress();
        }
    }
}
