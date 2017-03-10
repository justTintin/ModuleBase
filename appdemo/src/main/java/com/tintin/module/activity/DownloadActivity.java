package com.tintin.module.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tintin.module.app.R;

import java.io.IOException;

import me.tintin.module.util.Logger;
import me.tintin.module.util.ToastUtil;
import me.tintin.module.util.data.JacksonMapper;
import me.tintin.module.util.file.CommonDownloadTask;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 工具箱下载文件
 */
public class DownloadActivity extends AppCompatActivity
{

    private Button mBtn;

    private TextView mTv;
    private static final String TAG = DownloadActivity.class.getSimpleName();

    private String mFileUrl = "";
    private static final int MSG_DOWN = 1000;
    private static final String url = "http://cloud.bmob.cn/3ca0ab97c8ffdd2d/authorizeApp";

    private static final String ur2= "http://cloud.bmob.cn/3ca0ab97c8ffdd2d/authorizeApp";
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
        setContentView(R.layout.activity_download);
        mBtn = (Button) findViewById(R.id.button2);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getapp();
            }
        });
        mTv = (TextView) findViewById(R.id.textView2);
        //
//        getBillVoices(mHandler, MSG_DOWN, mFileUrl, "xx.txt");

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

    private void getapp(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GameScore gameScore = new GameScore();
                    gameScore.setAppId("2");

                    String respon= post(url, JacksonMapper.getInstance().writeValueAsString(gameScore));
                    Log.e(TAG,"respon=="+respon);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


}
