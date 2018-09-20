package com.example.rxtest.http;

import android.content.Context;
import android.util.Config;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zrh on 2016/4/14.
 */
public abstract class HttpBase {
    public static TransFormer IO_UI = new TransFormer();
    private static HttpLoggingInterceptor BODY_LOGGER = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.i("返回数据", message);
        }
    });
    private static HttpLoggingInterceptor BASIC_LOGGER = new HttpLoggingInterceptor();
    private static OkHttpClient client;
    private static OkHttpClient glideClient;
    public static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static GsonConverterFactory GSON = GsonConverterFactory.create(gson);
    public Context context;

    public HttpBase(Context context) {
        this.context = context;
        initHttp();
    }

    void initHttp() {
        setDebug(Config.DEBUG);
        initApiClient();
        initGlideClient();
    }

    public void setDebug(boolean debug) {
        if (debug) {
            BODY_LOGGER.setLevel(HttpLoggingInterceptor.Level.BODY);
            BASIC_LOGGER.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            BODY_LOGGER.setLevel(HttpLoggingInterceptor.Level.NONE);
            BASIC_LOGGER.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    void initApiClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(getConnectTimeOutSeconds(), TimeUnit.SECONDS);
        builder.readTimeout(getReadTimeOutSeconds(), TimeUnit.SECONDS);
        builder.writeTimeout(getWriteTimeOutSeconds(), TimeUnit.SECONDS);
        builder.addInterceptor(BODY_LOGGER);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });
        HttpsUtils.setUnsafe(builder);
        client = builder.build();
    }

    public static final int GLIDE_TIME_OUT_SECONDS = 10;

    void initGlideClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(GLIDE_TIME_OUT_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(GLIDE_TIME_OUT_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(GLIDE_TIME_OUT_SECONDS, TimeUnit.SECONDS);
        builder.addInterceptor(BASIC_LOGGER);
        HttpsUtils.setUnsafe(builder);
        glideClient = builder.build();
    }

    /**
     * @return 连接超时（ >= 5000ms）
     */
    public abstract int getConnectTimeOutSeconds();

    /**
     * @return 下载超时
     */
    public abstract int getReadTimeOutSeconds();

    /**
     * @return 上传超时
     */
    public abstract int getWriteTimeOutSeconds();

    /**
     * @return 默认的主机地址
     */
    public abstract String getDefaultBaseUrl();

    public static OkHttpClient getClient() {
        return client;
    }

    public static OkHttpClient getGlideClient() {
        return glideClient;
    }

}
