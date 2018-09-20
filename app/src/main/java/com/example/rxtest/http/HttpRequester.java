package com.example.rxtest.http;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zrh on 2016/4/22.
 */
public class HttpRequester extends HttpBase {
    private static HttpRequester requester = new HttpRequester(Init.getActivity());

    private HttpRequester(Context context) {
        super(context);
    }

    public static HttpRequester get() {
        return requester;
    }

    @Override
    public String getDefaultBaseUrl() {
        return Init.getBaseUrl();
    }

    @Override
    public int getConnectTimeOutSeconds() {
        return 10;
    }

    @Override
    public int getReadTimeOutSeconds() {
        return 10;
    }

    @Override
    public int getWriteTimeOutSeconds() {
        return 10;
    }

    Retrofit retrofit;

    public Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = newRetrofit(null, null);
        }
        return retrofit;
    }

    Retrofit rxGsonRtrofit;

    public Retrofit rxGson() {
        if (rxGsonRtrofit == null) {
            rxGsonRtrofit = newRetrofit(GSON, RxJava2CallAdapterFactory.create());
        }
        return rxGsonRtrofit;
    }


    public Retrofit newRetrofit(Converter.Factory converterFactory, CallAdapter.Factory callFactory) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getClient());
        builder.baseUrl(getDefaultBaseUrl());
        if (converterFactory != null) {
            builder.addConverterFactory(converterFactory);
        }
        if (callFactory != null) {
            builder.addCallAdapterFactory(callFactory);
        }
        return builder.build();
    }

}
