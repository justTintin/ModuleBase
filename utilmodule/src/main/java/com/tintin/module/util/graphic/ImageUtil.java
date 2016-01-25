/*
 * 文件名: ImageUtil.java
 * 描    述: [该类的简要描述]
 * 创建人: n003829
 * 创建时间:2015年8月7日
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.tintin.module.util.graphic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.tintin.module.util.L;

/**
 * [图处转码工具类]<BR>
 * [功能详细描述]
 * @author n003829
 * @version [MobileAssistant, 2015年8月7日]
 */
public class ImageUtil
{
    private static final String TAG = ImageUtil.class.getSimpleName();

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * [将字符串转换成Bitmap类型]<BR>
     * [功能详细描述]
     * @param string 图片的base64字符串
     * @return bitmap类型图片
     */
    public static Bitmap stringtoBitmap(String string)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray,
                    0,
                    bitmapArray.length);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap urlToBitmap(String url)
    {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try
        {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            //            conn.setConnectTimeout(5000);
            //            conn.setRequestMethod("GET");
            //            if(conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            //            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * bitmap转drawable
     *
     * @param resChecked   选中时显示图片
     * @param resUnChecked 未选中时显示图片
     * @return
     */
    public static Drawable getDrawable(Activity activity, Boolean ifAutoRate,
            Bitmap resChecked, Bitmap resUnChecked)
    {
        StateListDrawable drawable = new StateListDrawable();
        BitmapDrawable bitmapDrawable1 = new BitmapDrawable(
                activity.getResources(), resChecked);
        BitmapDrawable bitmapDrawable2 = new BitmapDrawable(
                activity.getResources(), resUnChecked);
        int height = bitmapDrawable2.getIntrinsicHeight();
        int width = bitmapDrawable2.getIntrinsicWidth();
        drawable.addState(new int[] { android.R.attr.state_checked },
                bitmapDrawable1);
        drawable.addState(new int[] { -android.R.attr.state_checked },
                bitmapDrawable2);

        if (ifAutoRate)
        {
            drawable.setBounds(0,
                    getPadRate(activity),
                    getRate(activity, width),
                    getRate(activity, height) + getPadRate(activity));
        }
        else
        {
            drawable.setBounds(0, getPadRate(activity), width, height
                    + getPadRate(activity));
        }

        return drawable;
    }

    private static int getRate(Activity activity, int base)
    {
        int rate = 0;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        if (widthPixels < 700)
        {
            rate = base * 40 / 100;
        }
        else if (1000 < widthPixels)
        {
            rate = base;
        }
        else
        {
            rate = base * 70 / 100;
        }
        L.d(TAG, widthPixels + "==rate==" + rate);
        return rate;
    }

    private static int getPadRate(Activity activity)
    {
        int pad = 10;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        if (widthPixels < 500)
        {
            pad = 0;
        }
        else if (1100 < widthPixels)
        {
            pad = 25;
        }
        else if (widthPixels > 500 && widthPixels < 800)
        {
            pad = 5;
        }
        else if (widthPixels > 800 && widthPixels < 1000)
        {
            pad = 10;
        }
        else
        {
            pad = 3;
        }
        L.d(TAG, widthPixels + "==pad==" + pad);
        return pad;
    }

    /**
     * bitmap放大与缩小
     * @param bitmap
     * @param value
     * @return
     */
    public static Bitmap bitMapScale(Bitmap bitmap, float value)
    {

        Bitmap tempBitmapBig;
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        int newWidthBig = (int) (width * value);
        int newHeightBig = (int) (height * value);
        // 计算缩放比例
        float scaleWidthBig = ((float) newWidthBig) / width;
        float scaleHeightBig = ((float) newHeightBig) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthBig, scaleHeightBig);
        // 得到新的图片
        tempBitmapBig = Bitmap.createBitmap(bitmap,
                0,
                0,
                width,
                height,
                matrix,
                true);

        return tempBitmapBig;

    }

}
