package com.feicuiedu.treasure.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * 头像上传响应结果
 * 作者：yuanchao on 2016/7/15 0015 11:27
 * 邮箱：yuanchao@feicuiedu.com
 */
public class UploadResult {
//    {
//        errcode : '文件系统上传成功！',                  //返回信息
//        urlcount: 1,                                   //返回值
//            imgUrl:
//        '/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0.png',
//                smallImgUrl:
//        '/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0_1.png'  //头像地址
//    }

    @SerializedName("errcode")
    private String msg;

    @SerializedName("urlcount")
    private int count;

    @SerializedName("smallImgUrl")
    private String url;

    public String getMsg() {
        return msg;
    }

    public int getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }
}
