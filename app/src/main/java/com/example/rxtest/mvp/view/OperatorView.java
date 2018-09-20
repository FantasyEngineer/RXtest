package com.example.rxtest.mvp.view;

import com.example.rxtest.mvp.bean.MovieInfo;

/**
 * Created by Administrator on 2018/8/27.
 */

public interface OperatorView {
    int getCount();

    int getStart();

    void showRequestMsg(MovieInfo info);

    void showRequestFailMsg(Throwable throwable);
}
