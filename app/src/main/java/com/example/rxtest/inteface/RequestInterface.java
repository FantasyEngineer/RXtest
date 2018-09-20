package com.example.rxtest.inteface;


import com.example.rxtest.bean.Translation;
import com.example.rxtest.bean.Translation2;
import com.example.rxtest.mvp.bean.MovieInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2018/8/16.
 */

public interface RequestInterface {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<Translation> getCall();

    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
    // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
    // 采用Observable<...>接口
    // getCall()是接受网络请求数据的方法

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    Observable<Translation> register();

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    Observable<Translation2> login();

    @GET("https://api.douban.com/v2/movie/top250")
    Observable<MovieInfo> getMovieInfo(@Query("start") int start, @Query("count") int count);

}
