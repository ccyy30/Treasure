package com.feicuiedu.treasure.user;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：yuanchao on 2016/7/13 0013 16:00
 * 邮箱：yuanchao@feicuiedu.com
 */
public class User {
//    {
//        "UserName":"qjd",
//            "Password":"654321"
//    }
    @SerializedName("UserName")
    private String username;

    @SerializedName("Password")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
