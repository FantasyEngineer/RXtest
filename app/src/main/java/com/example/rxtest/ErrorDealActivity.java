package com.example.rxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*错误处理*/
public class ErrorDealActivity extends AppCompatActivity {

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_deal);
        content = (TextView) findViewById(R.id.content);

    }

    public void retryWhen(View view) {
        content.setText("指示Observable遇到错误时，将错误传递给另一个Observable来决定" +
                "是否要重新给订阅这个Observable");
    }

    public void retry(View view) {
        content.setText("指示Observable遇到错误时重试");

    }

    public void onExceptionResumeNext(View view) {
        content.setText("指示Observable遇到错误" +
                "时继续发射数据");

    }

    public void onErrorReturn(View view) {
        content.setText("指示Observable在遇到错误时发射一个特定的数据");

    }

    public void onErrorResumeNext(View view) {
        content.setText("指示Observable在遇到错误时发射一个数据序列");

    }
}
