package com.feicuiedu.treasure.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 作者：yuanchao on 2016/7/13 0013 15:49
 * 邮箱：yuanchao@feicuiedu.com
 */
public class NetClient {

    private static NetClient netClient;

    private final OkHttpClient client;

    private NetClient(){
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static NetClient getInstance(){
        if(netClient == null){
            netClient = new NetClient();
        }
        return netClient;
    }

    public OkHttpClient getClient(){
        return client;
    }
}
