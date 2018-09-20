package com.example.rxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;


/*Rx Binding*/
public class RxBindingActivity extends AppCompatActivity {

    private EditText et_name, et_age, et_sex, et_search;
    private Button btn_pull, btn_response, btn_longclick, btn_other, btn_continueClick;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_binding);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_sex = findViewById(R.id.et_sex);
        btn_pull = findViewById(R.id.btn_pull);
        btn_pull.setEnabled(false);

        btn_response = findViewById(R.id.btn_response);
        btn_longclick = findViewById(R.id.btn_longclick);
        btn_other = findViewById(R.id.btn_other);
        btn_continueClick = findViewById(R.id.btn_continueClick);
        et_search = findViewById(R.id.et_search);


        Observable<CharSequence> nameObservable = RxTextView.textChanges(et_name).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(et_age).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(et_sex).skip(1);

        Observable.combineLatest(nameObservable, ageObservable, jobObservable, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) throws Exception {
                Log.d("RxBindingActivity", charSequence.toString());
                Log.d("RxBindingActivity", "charSequence2:" + charSequence2);
                Log.d("RxBindingActivity", "charSequence3:" + charSequence3);

                boolean isUserNameValid = !TextUtils.isEmpty(et_name.getText());
                // 2. 年龄信息
                boolean isUserAgeValid = !TextUtils.isEmpty(et_age.getText());
                // 3. 职业信息
                boolean isUserJobValid = !TextUtils.isEmpty(et_sex.getText());

                return isUserNameValid && isUserAgeValid && isUserJobValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                btn_pull.setEnabled(aBoolean);
            }
        });


        RxView.clicks(btn_response).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        Log.d("RxBindingActivity", "点击了按钮");
                        Toast.makeText(RxBindingActivity.this, "点击了按钮", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        /*1s后联想输入的，防止请求过多*/
        RxTextView.textChanges(et_search).skip(1).debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        Toast.makeText(RxBindingActivity.this, "charSequence:" + charSequence, Toast.LENGTH_SHORT).show();
                    }
                });

        RxView.longClicks(btn_longclick, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (i < 3) {
                    i++;
                    return true;
                } else {
                    return false;
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(RxBindingActivity.this, "长按", Toast.LENGTH_SHORT).show();
            }
        });


        /*other*/
        try {
            /*是否可点击*/
            RxView.enabled(btn_other).accept(false);
            /*是否展示*/
            RxView.visibility(btn_other).accept(true);

        } catch (Exception e) {
            e.printStackTrace();
        }



        /*连续点击*/
        Observable observable = RxView.clicks(btn_continueClick).share();
        observable.debounce(1, TimeUnit.SECONDS).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d("RxBindingActivity", o.toString());
            }
        });

    }

}
