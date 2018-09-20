package com.example.rxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/*结合操作 And  / StartWith  / Join  / Merge  / Switch  / Zip*/
public class CombiningActivity extends AppCompatActivity {

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combining);
        content = (TextView) findViewById(R.id.content);
    }


    public void Merge(View view) {
        content.setText("将多个Observables的输出合并，就好像它们是一个单个的Observable一样。  但是它可能是无序的");
        Observable<Integer> data = Observable.just(10, 20, 30, 40);
        Observable<Integer> addData = Observable.just(0, 1, 2, 3);
        Observable.merge(data, addData).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("CombiningActivity", "d.isDisposed():" + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                Log.d("CombiningActivity", "value:" + value);
                content.setText(content.getText() + "\n" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void StartWith(View view) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        content.setText("如果你想要一个Observable在发射数据之前先发射" +
                "一个指定的数据序列，可以使用 StartWith  操作符。" +
                "（如果你想一个Observable发射的数据末尾追加一个数据序列可以使 用 Concat  操作符。");
        Observable<Integer> data = Observable.just(10, 20, 30, 40);
        Observable<Integer> addData = Observable.just(999);
        /*头部追加多组数据 (最多接受九个参数)*/
//        data.startWithArray(1, 2, 3, 4, 5, 6).subscribe(new Observer<Integer>() {
        /*头部追加一个数据*/
//        data.startWith(1).subscribe(new Observer<Integer>() {
        /*头部追加一个list数据*/
//        data.startWith(list).subscribe(new Observer<Integer>() {
        /*头部追加一个observable*/
        data.startWith(addData).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                content.setText(content.getText() + "\n" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void Switch(View view) {
        content.setText("将一个发射Observables的Observable转换成另一个Observable，" +
                "后者发射这些Observables最近发射的数据");

        /*每隔2s返回一个Observable，这个Observable每隔1s发射一条数据，
        在第二个Observable产生之前有4s时间，产生了四条数据。当新的Observable产生时，之前的Observable就被丢弃了*/
    }

    public void Zip(View view) {
        content.setText("通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发" +
                "射单个数据项。(严格的按照对应的次序进行的相加，单一方Observable有的 不处理，不发射，" +
                "只发射结合体,----RXJAVA2沒有zipwith)");


        String[] itemsOne = {"1", "2", "3", "4", "5"};
        Integer[] itemsTwo = {6, 7, 8, 9, 10};

        Observable<String> one = Observable.fromArray(itemsOne);
        Observable<Integer> two = Observable.fromArray(itemsTwo);

        /*最多可以有九个Observables参数*/
        Observable myObservable = Observable.zip(one, two, new BiFunction<String, Integer, String>() {

            @Override
            public String apply(String o, Integer o2) throws Exception {
                return "第一观察者的第一个数据=" + o + "，二观察者的第一个数据=" + o2 + "";
            }
        });

        /*接收数据*/
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                content.setText(content.getText() + "\n" + value + "");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        myObservable.subscribe(observer);
    }

}
