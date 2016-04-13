package com.tintin.module.util.graphic;

/*
* [自定义获取图片Task]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/1/20] 
*/

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap>
{
    ImageView imageView;

    Bitmap bitmap;

    public ImageDownloadTask(ImageView imageView)
    {
        this.imageView = imageView;
    }

    public ImageDownloadTask(Bitmap imageView)
    {
        this.bitmap = imageView;
    }

    protected Bitmap doInBackground(String... addresses)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try
        {
            // 建立URL连接
            URL url = new URL(addresses[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 打开输入流
            conn.connect();
            in = conn.getInputStream();
            // 编码输入流
            bitmap = BitmapFactory.decodeStream(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return bitmap;
    }

    // Task执行完毕，返回bitmap
    @Override
    protected void onPostExecute(Bitmap result)
    {
        // Set bitmap image for the result
        imageView.setImageBitmap(result);
        bitmap = result;
    }
}
