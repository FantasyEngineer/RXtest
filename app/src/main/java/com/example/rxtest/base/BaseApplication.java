package com.example.rxtest.base;

import android.app.Application;

import com.example.rxtest.http.Init;

/**
 * Created by Administrator on 2018/8/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*初始化*/
        Init.setActivity(this);
        Init.setBaseUrl("http://fy.iciba.com/ajax.php/");
    }
}
