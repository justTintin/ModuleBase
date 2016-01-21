package com.tintin.module.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tintin.module.modulebase.R;
import com.tintin.viewmod.circle.CircleSeekBar;

public class CircleSeekBarUnionActivity extends AppCompatActivity {
    public static final String TAG = CircleSeekBarUnionActivity.class.getSimpleName();

    private CircleSeekBar mHourSeekbar;

    private CircleSeekBar mMinuteSeekbar;

    private TextView mTextView;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    start();
                    break;
            }
        }
    };

    private void start() {
        mHourSeekbar.setEndProcessByAnim(160, 3000);
        mMinuteSeekbar.setEndProcessByAnim(300, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_seek_bar_union);

        mHourSeekbar = (CircleSeekBar) findViewById(R.id.seek_hour);
        mMinuteSeekbar = (CircleSeekBar) findViewById(R.id.seek_minute);
        mTextView = (TextView) findViewById(R.id.textview);

        mHourSeekbar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekbar, int maxValue,
                                  int curValue) {
                changeText(curValue, mMinuteSeekbar.getCurProcess());
            }
        });

        mMinuteSeekbar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekbar, int maxValue,
                                  int curValue) {
                changeText(mHourSeekbar.getCurProcess(), curValue);
            }
        });

        mHourSeekbar.setCurProcess(0);

        mMinuteSeekbar.setCurProcess(0);
        mMinuteSeekbar.setPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon1));
        mMinuteSeekbar.setPointBitmapClick(BitmapFactory.decodeResource(getResources(), R.mipmap.icon3));
        mHandler.sendEmptyMessageDelayed(1, 500);
//        start();
    }

    private void changeText(int hour, int minute) {
        String hourStr = hour > 9 ? hour + "" : "0" + hour;
        String minuteStr = minute > 9 ? minute + "" : "0" + minute;
        mTextView.setText(hourStr + ":" + minuteStr);
    }
}
