//package com.example.rxtest.base;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.disposables.Disposable;
//
///**
// * Created by Administrator on 2018/8/21.
// */
//
//public abstract class BaseActivity extends AppCompatActivity {
//    public CompositeDisposable mCompositeDisposable;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getLayoutId());
//        mUnbinder = ButterKnife.bind(this);
//        mCompositeDisposable = new CompositeDisposable();
//        onViewCreated(savedInstanceState);
//    }
//
//    /**
//     * 添加订阅
//     */
//    public void addDisposable(Disposable mDisposable) {
//        if (mCompositeDisposable == null) {
//            mCompositeDisposable = new CompositeDisposable();
//        }
//        mCompositeDisposable.add(mDisposable);
//    }
//
//    /**
//     * 取消所有订阅
//     */
//    public void clearDisposable() {
//        if (mCompositeDisposable != null) {
//            mCompositeDisposable.clear();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        clearDisposable();
//        mUnbinder.unbind();
//    }
//
//    protected abstract int getLayoutId();
//
//    protected abstract void onViewCreated(Bundle savedInstanceState);
//}
