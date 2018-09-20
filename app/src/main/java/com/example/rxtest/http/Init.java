package com.example.rxtest.http;

import android.app.Application;
import android.util.Log;

/**
 * Created by Administrator on 2018/8/17.
 */

public class Init {
    private static Application activity;
    private static String BaseUrl;


    public static Application getActivity() {
        if (activity == null) {
            Log.d("init", "请调用setActivity进行初始化");
            throw new NullPointerException();
        }
        return activity;
    }

    public static void setActivity(Application activity) {
        Init.activity = activity;
    }

    public static String getBaseUrl() {
        if (activity == null) {
            Log.d("init", "请调用setBaseUrl设置基地址");
            throw new NullPointerException();
        }
        return BaseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
    }
}
