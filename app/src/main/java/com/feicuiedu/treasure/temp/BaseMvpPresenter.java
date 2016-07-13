package com.feicuiedu.treasure.temp;

/**
 * 作者：yuanchao on 2016/7/13 0013 09:55
 * 邮箱：yuanchao@feicuiedu.com
 *
 *
 * 明确，当前Presenter中的View是谁
 * 如果不明确，那这个View就是MvpView
 * MvpView是所有mvp架构方式中的View的父类
 */
public class BaseMvpPresenter<T extends BaseMvpView> {
    private T mvpView;
}