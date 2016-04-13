package com.tintin.module.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.tintin.module.app.R;
import com.tintin.module.util.Logger;
import com.tintin.module.util.ToastUtil;
import com.tintin.module.util.file.CommonDownloadTask;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = MainActivity.class.getSimpleName();

    private String mFileUrl = "";
    private static final int MSG_DOWN = 1000;

    private Handler mHandler = new Handler()
    {
        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            Logger.d(TAG, "==msg=" + msg.toString());
            switch (msg.what)
            {

                case MSG_DOWN:
                    ToastUtil.show(getApplicationContext(),
                            msg.toString(),
                            3000);
                break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        getBillVoices(mHandler, MSG_DOWN, mFileUrl, "xx.txt");

    }

    //    下面给大家看一下调用方法
    @SuppressLint("NewApi")
    public void getBillVoices(Handler mHandler, int what, String mFilePath,
            String fileName)
    {
        CommonDownloadTask task = new CommonDownloadTask(
                getApplicationContext(), mHandler, mFilePath, what);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            task.execute(fileName);
        }
        else
        {
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, fileName);
        }
    }
}
