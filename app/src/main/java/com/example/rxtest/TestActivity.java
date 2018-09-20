package com.example.rxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView tv;
    private Button btn;
    private boolean isv = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isv) {
                    isv = false;
                    tv.setVisibility(View.GONE);
                } else {
                    isv = true;
                    tv.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
