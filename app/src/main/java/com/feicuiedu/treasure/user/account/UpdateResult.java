package com.feicuiedu.treasure.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：yuanchao on 2016/7/15 0015 14:42
 * 邮箱：yuanchao@feicuiedu.com
 */
public class UpdateResult {
//    {
//        "errcode":1,             //状态值
//            "errmsg":"修改成功!"     //返回信息
//    }

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
