package com.feicuiedu.treasure.treasure.home.detail;

import com.google.gson.annotations.SerializedName;

/**
 * 请求数据
 * 作者：yuanchao on 2016/7/21 0021 14:04
 * 邮箱：yuanchao@feicuiedu.com
 */
public class TreasureDetail {

    @SerializedName("TreasureID")
    private final int treasureId;

    @SerializedName("PagerSize")
    private final int pageSize;

    @SerializedName("currentPage")
    private final int currentPage;

    public TreasureDetail(int treasureId) {
        this.treasureId = treasureId;
        this.pageSize = 20;
        this.currentPage = 1;
    }
}
