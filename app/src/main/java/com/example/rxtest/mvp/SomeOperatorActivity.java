package com.example.rxtest.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rxtest.R;
import com.example.rxtest.mvp.bean.MovieInfo;
import com.example.rxtest.mvp.presenter.GetMoviePresenter;
import com.example.rxtest.mvp.view.OperatorView;
import com.example.rxtest.widget.LoadingDialog;

/*一些操作符*/
public class SomeOperatorActivity extends AppCompatActivity implements OperatorView {

    private GetMoviePresenter getMoviePresenter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_operator);
        getMoviePresenter = new GetMoviePresenter(this);
        loadingDialog = new LoadingDialog(this, R.style.CustomProgressDialog);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getStart() {
        return 0;
    }

    @Override
    public void showRequestMsg(MovieInfo info) {
        Toast.makeText(this, "info.getTotal():" + info.getTotal(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRequestFailMsg(Throwable throwable) {

    }

    public void getMovieInfo(View view) {
        getMoviePresenter.getMovieInfo(loadingDialog);
    }
}
