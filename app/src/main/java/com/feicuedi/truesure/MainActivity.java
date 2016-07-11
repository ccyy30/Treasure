package com.feicuedi.truesure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feicuedi.truesure.commons.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityUtils = new ActivityUtils(this);
    }
}