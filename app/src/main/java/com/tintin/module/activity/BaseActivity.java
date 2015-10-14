package com.tintin.module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tintin.module.modulebase.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Created by alive on 2015/10/9.
 */

public class BaseActivity extends FinalActivity {
    //无需调用findViewById和setOnclickListener等
    @ViewInject(id=R.id.button,click="btnClick")
    Button button;
    @ViewInject(id= R.id.textView)
    TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void btnClick(View v){
        textView.setText("text set form button");
    }
}
