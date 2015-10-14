package com.tintin.module.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tintin.viewmod.MyClass;
import com.tintin.module.modulebase.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyClass myClass = new MyClass();
    }
}
