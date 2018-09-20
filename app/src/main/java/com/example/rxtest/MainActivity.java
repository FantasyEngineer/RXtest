package com.example.rxtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rxtest.bean.Translation;
import com.example.rxtest.bean.Translation2;
import com.example.rxtest.biz.RequestBiz;
import com.example.rxtest.mvp.SomeOperatorActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rxjava";

    // 设置变量
    // 可重试次数
    private int maxConnectCount = 10;
    // 当前已重试次数
    private int currentRetryCount = 0;
    // 重试等待时间
    private int waitRetryTime = 0;
    private RequestBiz requestBiz;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestBiz = RequestBiz.getRequestBiz();
        startActivity(new Intent(this, TestActivity.class));
    }

    public void doubleRequestDependenceFirstRequestSuccess(View view) {
        doubleRequestDependenceFirstRequestSuccess();
    }

    public void continueLoop(View view) {
        continueLoop();
    }

    public void retryLoop(View view) {
        retryLoop();
    }

    public void fromMemoryDiskNetToGetData(View view) {
        fromMemoryDiskNetToGetData();
    }

    String memoryData = null;
    //    String diskCache = "从磁盘缓存中获取数据";
    String diskCache = null;

    /*从内存硬盘网络获取数据*/
    public void fromMemoryDiskNetToGetData() {
        /*
         * 设置第1个Observable：检查内存缓存是否有该数据的缓存
         **/
        Observable<String> memoryObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (memoryData == null) {
                    e.onComplete();
                } else {
                    e.onNext(memoryData);
                }
            }
        });

        /*
         * 设置第2个Observable：检查磁盘缓存是否有该数据的缓存
         **/
        Observable<String> diskObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (diskCache == null) {
                    e.onComplete();
                } else {
                    e.onNext(diskCache);
                }
            }
        });

        /*
         * 设置第3个Observable：通过网络获取数据
         **/
        Observable<String> network = Observable.just("从网络中获取数据");
        // 此处仅作网络请求的模拟


        /*
         * 通过concat（） 和 firstElement（）操作符实现缓存功能
         **/

        Observable.concat(memoryObservable, diskObservable, network)
                // 2. 通过firstElement()，从串联队列中取出并发送第1个有效事件（Next事件），即依次判断检查memory、disk、network
                .firstElement()
                // 即本例的逻辑为：
                // a. firstElement()取出第1个事件 = memory，即先判断内存缓存中有无数据缓存；由于memoryCache = null，即内存缓存中无数据，所以发送结束事件（视为无效事件）
                // b. firstElement()继续取出第2个事件 = disk，即判断磁盘缓存中有无数据缓存：由于diskCache ≠ null，即磁盘缓存中有数据，所以发送Next事件（有效事件）
                // c. 即firstElement()已发出第1个有效事件（disk事件），所以停止判断。

                // 3. 观察者订阅
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this, "最终获取的数据来源 =  " + s, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "最终获取的数据来源 =  " + s);
                    }
                });

    }

    /*两次请求取决于第一次请求成功*/
    private void doubleRequestDependenceFirstRequestSuccess() {
        Observable<Translation> registerObservable = requestBiz.register();
        final Observable<Translation2> loginObservable = requestBiz.login();
        registerObservable.doOnNext(new Consumer<Translation>() {
            @Override
            public void accept(Translation result) throws Exception {
                Toast.makeText(MainActivity.this, "第1次网络请求成功", Toast.LENGTH_SHORT).show();
                result.show();
                // 对第1次网络请求返回的结果进行操作 = 显示翻译结果
            }
        })
                // 但对于初始观察者，它则是新的被观察者
                .flatMap(new Function<Translation, ObservableSource<Translation2>>() { // 作变换，即作嵌套网络请求
                    @Override
                    public ObservableSource<Translation2> apply(Translation result) throws Exception {
                        // 将网络请求1转换成网络请求2，即发送网络请求2
                        return loginObservable;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())  // （初始观察者）切换到主线程 处理网络请求2的结果
                .subscribe(new Consumer<Translation2>() {
                    @Override
                    public void accept(Translation2 result) throws Exception {
                        Log.d(TAG, "第2次网络请求成功");
                        Toast.makeText(MainActivity.this, "第2次网络请求成功", Toast.LENGTH_SHORT).show();
                        result.show();
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /*当网络异常时，重新发送请求*/
    private void retryLoop() {
        requestBiz.getCall().retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                        /**
                         * 需求1：根据异常类型选择是否重试
                         * 即，当发生的异常 = 网络异常 = IO异常 才选择重试
                         */
                        if (throwable instanceof IOException) {
                            Log.d(TAG, "属于IO异常，需重试");

                            /**
                             * 需求2：限制重试次数
                             * 即，当已重试次数 < 设置的重试次数，才选择重试
                             */
                            if (currentRetryCount < maxConnectCount) {
                                // 记录重试次数
                                currentRetryCount++;
                                Log.d(TAG, "重试次数 = " + currentRetryCount);

                                /**
                                 * 需求2：实现重试
                                 * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
                                 *
                                 * 需求3：延迟1段时间再重试
                                 * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                                 *
                                 * 需求4：遇到的异常越多，时间越长
                                 * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
                                 */
                                // 设置等待时间
                                waitRetryTime = 1000 + currentRetryCount * 1000;
                                Log.d(TAG, "等待时间 =" + waitRetryTime);
                                return Observable.just(4).delay(waitRetryTime, TimeUnit.MILLISECONDS);


                            } else {
                                // 若重试次数已 > 设置重试次数，则不重试
                                // 通过发送error来停止重试（可在观察者的onError（）中获取信息）
                                return Observable.error(new Throwable("重试次数已超过设置次数 = " + currentRetryCount + "，即 不再重试"));

                            }
                        }

                        // 若发生的异常不属于I/O异常，则不重试
                        // 通过返回的Observable发送的事件 = Error事件 实现（可在观察者的onError（）中获取信息）
                        else {
                            return Observable.error(new Throwable("发生了非网络异常（非I/O异常）"));
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation value) {
                        value.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "e:" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    /*无限循环 和 有限次循环*/
    private void continueLoop() {
         /*
         * 步骤1：采用interval（）延迟发送
         * 注：此处主要展示无限次轮询，若要实现有限次轮询，仅需将interval（）改成intervalRange（）即可
         **/
//        Observable.intervalRange( 2, 1, TimeUnit.SECONDS)

        Observable.intervalRange(3, 5, 2, 1, TimeUnit.SECONDS)
                // 参数说明：
                // 参数1 = 第1次延迟时间；
                // 参数2 = 间隔时间数字；
                // 参数3 = 时间单位；
                // 该例子发送的事件特点：延迟2s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）

                 /*
                  * 步骤2：每次发送数字前发送1次网络请求（doOnNext（）在执行Next事件前调用）
                  * 即每隔1秒产生1个数字前，就发送1次网络请求，从而实现轮询需求
                  **/
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Exception {
                        Log.d(TAG, "第 " + integer + " 次轮询");

                        // d. 通过线程切换发送网络请求
                        requestBiz.getCall().subscribe(new Observer<Translation>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Translation result) {
                                // e.接收服务器返回的数据
                                result.show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "请求失败");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }


    public void jumpCombining(View view) {
        startActivity(new Intent(this, CombiningActivity.class));
    }

    public void jumpErrorDeal(View view) {
        startActivity(new Intent(this, ErrorDealActivity.class));
    }

    public void jumpCreating(View view) {
        startActivity(new Intent(this, CreatActivity.class));
    }

    public void jumpRxBinding(View view) {
        startActivity(new Intent(this, RxBindingActivity.class));
    }


    /*背压*/
    public void backPressure(View view) {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onComplete();
            }
        }, BackpressureStrategy.MISSING).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(2);
                        /*指定接收所有事件时可以使用如下代码*/
//                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        Toast.makeText(MainActivity.this, o.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        Flowable.interval(1, TimeUnit.MICROSECONDS)
//                .onBackpressureDrop()  //加上背压策略
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "onNext: " + aLong);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });

    }

    public void jumpMvp(View view) {
        startActivity(new Intent(this, SomeOperatorActivity.class));
    }
}
