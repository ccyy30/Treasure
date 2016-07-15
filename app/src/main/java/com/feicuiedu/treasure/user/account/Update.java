package com.feicuiedu.treasure.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：yuanchao on 2016/7/15 0015 14:42
 * 邮箱：yuanchao@feicuiedu.com
 */
public class Update {
//    {
//        "Tokenid":3,"
//        "HeadPic": "05a1a7e18ab940679dbd0e506be31add.jpg"
//    }

    @SerializedName("Tokenid")
    private int tokenId;

    @SerializedName("HeadPic")
    public String photoUrl;

    public Update(int tokenId, String photoUrl) {
        this.tokenId = tokenId;
        this.photoUrl = photoUrl;
    }
}
