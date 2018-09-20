package com.example.rxtest.http;

/**
 * Created by Administrator on 2018/8/16.
 */

public class BaseBiz {

    protected HttpRequester httpRequester;

    public BaseBiz() {
        httpRequester = HttpRequester.get();
    }

    /**
     * 以Gson的解析方式，通过Observable返回
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getRxGsonApi(Class<T> tClass) {
        return httpRequester.rxGson().create(tClass);
    }

}
