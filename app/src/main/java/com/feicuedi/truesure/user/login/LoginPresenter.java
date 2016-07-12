package com.feicuedi.truesure.user.login;

/**
 * Created by Administrator on 2016/7/12 0012.
 *
 * 登陆视图业务
 */
public class LoginPresenter {
    private LoginView loginView;

    public LoginPresenter(LoginView loginView){
        this.loginView = loginView;
    }

    public void login(){
        loginView.showProgress();
        执行网络连接;
        如果出错{
            loginView.hideProgress();
            loginView.showMessage("");
            return;
        }
        网络连接，数据读取
        如果出错{
            loginView.hideProgress();
            loginView.showMessage("");
            return;
        }
        if(成功){
            loginView.hideProgress();
            loginView.navigateToHome();
        }
    }
}