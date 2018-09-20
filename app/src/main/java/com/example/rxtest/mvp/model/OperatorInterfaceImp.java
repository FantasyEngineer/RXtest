package com.example.rxtest.mvp.model;

import com.example.rxtest.biz.RequestBiz;
import com.example.rxtest.mvp.bean.MovieInfo;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/8/27.
 */

public class OperatorInterfaceImp implements OperatorInterface {
    @Override
    public void requset(int start, int count, final OnRequestListener onRequestListene) {
        RequestBiz requestBiz = RequestBiz.getRequestBiz();
        requestBiz.getMovieInfo(start, count).subscribe(new Observer<MovieInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MovieInfo value) {
                onRequestListene.success(value);
            }

            @Override
            public void onError(Throwable e) {
                onRequestListene.failed(e);
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
