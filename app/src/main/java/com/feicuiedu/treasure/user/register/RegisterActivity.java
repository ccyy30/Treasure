package com.feicuiedu.treasure.user.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.commons.ActivityUtils;
import com.feicuiedu.treasure.commons.RegexUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册视图
 *
 * 1. 完成注册的视图
 * 2. 考虑注册相关业务及视图 (接口)
 *    要做什么功能？
 *    --> 注册
 *       --> 网络连接，验证   ------- 视图表现 (显示一个loading)
 *       --> 出错了 --- 视图表现(........)
 *       --> 通过了
 *          --> 读取，解析数据
 *              --> 出错了 --- 视图表现(........)
 *              --> 通过了（得到数据了） -- 视图表现 (进入HOME页面)
 */
public class RegisterActivity extends AppCompatActivity implements RegisterView{

    @Bind(R.id.et_Username) EditText etUsername;
    @Bind(R.id.et_Password) EditText etPassword;
    @Bind(R.id.et_Confirm) EditText etConfirm;
    @Bind(R.id.btn_Register) Button btnRegister;

    private String username; // 用来保存编辑框内的用户名
    private String password; // 用来保存编辑框内的密码

    private ActivityUtils activityUtils;// Activity常用工具集

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_register);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        etConfirm.addTextChangedListener(mTextWatcher); // EditText监听
        etPassword.addTextChangedListener(mTextWatcher); // EditText监听
        etUsername.addTextChangedListener(mTextWatcher); // EditText监听
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();
            boolean canRegister = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                    && password.equals(confirm);
            btnRegister.setEnabled(canRegister);// 注意：在布局内注册按钮默认是不可用的
        }
    };

    @OnClick(R.id.btn_Register)
    public void register() {
        // 正则进行判断输入的用户名是否有效
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.username_rules);
            return;
        }
        // 正则进行判断输入的密码是否有效
        if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.username_rules);
            return;
        }
        // 执行注册业务逻辑
        new RegisterPresenter(this).regiser();
    }

    @Override public void navigateToHome() {

    }

    @Override public void showProgress() {

    }

    @Override public void hideProgress() {

    }

    @Override public void showMessage(String msg) {

    }
}
