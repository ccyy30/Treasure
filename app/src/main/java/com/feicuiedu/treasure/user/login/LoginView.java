package com.feicuiedu.treasure.user.login;

/**
 * 登陆业务相关视图
 */
public interface LoginView {
    /** 显示登陆中的Loading视图 */
    void showProgress();
    /** 隐藏登陆中的Loading视图*/
    void hideProgress();
    /** 显示信息*/
    void showMessage(String msg);
    /** 导航到Home页面*/
    void navigateToHome();
}
