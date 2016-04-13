package com.tintin.module.util.file;

/*
* [获取下载的Task]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/1/20] 
*/

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CommonDownloadTask extends AsyncTask<String, Integer, Long>
{

    private static  final String TAG = CommonDownloadTask.class.getSimpleName();
    private Handler mHandler = null;
    private String mUrl = null;
    private int mWhat = 0;

    private Context mContext;
    public CommonDownloadTask(Context context,Handler handler, String url, int what) {

       mContext = context;
        mHandler = handler;
        mUrl = url;
        mWhat = what;
    }


    @Override
    protected Long doInBackground(String... params) {
        OutputStream output = null;
        try {
            URL url =  new URL(mUrl);
            // 创建一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            InputStream input = urlConn.getInputStream();
            //文件夹
            File dir =mContext.getExternalFilesDir(null);
            if(!dir.exists())
            {
                dir.mkdir();
            }
            //本地文件
            File file = new File(mContext.getExternalFilesDir(null).getPath() + params[0]);
            if(!file.exists()){
                file.createNewFile();
                //写入本地
                output = new FileOutputStream(file);
                byte buffer [] = new byte[1024];
                int inputSize = -1;
                while((inputSize = input.read(buffer)) != -1) {
                    output.write(buffer, 0, inputSize);
                }
                output.flush();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                output.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        //发送处理
        if (mHandler != null) {
            sendResponse(mHandler, mContext.getExternalFilesDir(null).getPath() + params[0], mWhat);
        } else {
            Log.w(TAG, "mHandler == null, what = " + mWhat);
        }
        return null;
    }

    /**
     * 下面是通过 Handler 发送消息操作主线程
     */
    private void sendResponse(Handler handler, String response, int what)
    {
        if (handler != null) {
            if (null == response) {
                handler.sendEmptyMessage(what);
            } else {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("response", response);
                msg.setData(bundle);
                msg.what = what;
                handler.sendMessage(msg);
            }
            mContext = null;
        }
    }

}
