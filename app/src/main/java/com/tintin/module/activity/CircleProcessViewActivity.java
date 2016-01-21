package com.tintin.module.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tintin.module.modulebase.R;
import com.tintin.viewmod.circle.CircleProcessView;

public class CircleProcessViewActivity extends AppCompatActivity
{

    private CircleProcessView circleProcessView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_process_view);
        circleProcessView = (CircleProcessView) findViewById(R.id.circleProcessView);
        circleProcessView.setProgress(50);
    }
}
