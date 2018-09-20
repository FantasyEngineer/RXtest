package com.example.rxtest.mvp.model;

import com.example.rxtest.mvp.bean.MovieInfo;

/**
 * Created by Administrator on 2018/8/27.
 */

public interface OperatorInterface {
    void requset(int start, int count, OnRequestListener onRequestListener);

    interface OnRequestListener {
        void success(MovieInfo movieInfo);

        void failed(Throwable s);
    }

}
