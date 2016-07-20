package com.feicuiedu.treasure.treasure.home.hide;

import com.google.gson.annotations.SerializedName;

/**
 * 埋藏宝藏Request数据
 * 作者：yuanchao on 2016/7/20 0020 17:22
 * 邮箱：yuanchao@feicuiedu.com
 */
public class HideTreasure {

    @SerializedName("Tokenid")
    private int tokenId;

    @SerializedName("TreasureName")
    private String title;

    @SerializedName("POI")
    private String location;

    @SerializedName("ShortContent")
    private String description;

    @SerializedName("Yline")
    private double latitude;

    @SerializedName("Xline")
    private double longitude;

    @SerializedName("Height")
    private double altitude;

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    //    {
//        "TreasureName": "哈哈",
//            "ShortContent": "假的",
//            "Bimage1": "/UpLoad/HeadPic/4a5bc495ae744ef69fa4e41bc8df43bf_0_1.png",
//            "Bimage2": "/UpLoad/HeadPic/f8dc70f7ae65479c87e7a23c450f18cf_1_1.png",
//            "Bimage3": "/UpLoad/HeadPic/6277063cb540443ba9a524648a51b9ad_2_1.png",
//            "Bimage4": "/UpLoad/HeadPic/1078ea14ab7d43c99a3da261c256714d_3_1.png",
//            "Bimage5": "/UpLoad/HeadPic/8674dcf9a5b547cd97d9d824d90c41fa_4_1.png",
//            "Bimage7": "/UpLoad/HeadPic/937820d0214949cfb442a3bddc8bb8af_6_1.png",
//            "Bimage8": "/UpLoad/HeadPic/730f3e2113c641a0b9c6f46a5453c3ae_7_1.png",
//            "Bimage9": "/UpLoad/HeadPic/eec3946b445b4d85b883dc232297b55b_8_1.png",
//            "POI": "北京市昌平区文华东路",
//            "Height": 5e-324,
//            "Yline": 40.0950205607537,
//            "Xline": 116.3559161851689,
//            "Size": 1,
//            "Levels": 2,
//            "Tokenid": 171
//    }

    @SerializedName("Size")
    private final int size = 1;

    @SerializedName("Levels")
    private final int level = 1;

    @SerializedName("Bimage1")
    private final String imageUrl1 = "";

    @SerializedName("Bimage2")
    private final String imageUrl2 = "";

    @SerializedName("Bimage3")
    private final String imageUrl3 = "";

    @SerializedName("Bimage4")
    private final String imageUrl4 = "";

    @SerializedName("Bimage5")
    private final String imageUrl5 = "";

    @SerializedName("Bimage6")
    private final String imageUrl6 = "";

    @SerializedName("Bimage7")
    private final String imageUrl7 = "";

    @SerializedName("Bimage8")
    private final String imageUrl8 = "";

    @SerializedName("Bimage9")
    private final String imageUrl9  = "";

}
