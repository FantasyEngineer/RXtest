package com.example.rxtest.biz;


import com.example.rxtest.bean.Translation;
import com.example.rxtest.bean.Translation2;
import com.example.rxtest.http.BaseBiz;
import com.example.rxtest.http.HttpBase;
import com.example.rxtest.inteface.RequestInterface;
import com.example.rxtest.mvp.bean.MovieInfo;

import io.reactivex.Observable;


/**
 * 请求体
 */

public class RequestBiz extends BaseBiz {
    private RequestBiz() {
        super();
    }

    public static RequestBiz getRequestBiz() {
        return SingletonHolder.biz;
    }

    private static class SingletonHolder {
        private static final RequestBiz biz = new RequestBiz();
    }

    public Observable<Translation> getCall() {
        return getRxGsonApi(RequestInterface.class).getCall().compose(HttpBase.IO_UI);
    }

    public Observable<Translation> register() {
        return getRxGsonApi(RequestInterface.class).register().compose(HttpBase.IO_UI);
    }

    public Observable<Translation2> login() {
        return getRxGsonApi(RequestInterface.class).login().compose(HttpBase.IO_UI);
    }


    public Observable<MovieInfo> getMovieInfo(int start, int count) {
        return getRxGsonApi(RequestInterface.class).getMovieInfo(start, count).compose(HttpBase.IO_UI);
    }


}
