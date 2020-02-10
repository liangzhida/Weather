package com.example.weather.custom;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    //继承什么activty都可以，根据实际需求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当一个activty创建后就将其加入到list集合中
        ((MyApplication) getApplication()).addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当被销毁后，将其移除
        ((MyApplication) getApplication()).finishSingle(this);
    }
}
