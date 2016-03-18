package com.tintin.module.util;

/**
 * Created by Administrator on 2015/12/2.
 */

import android.util.Log;

/**
 * Log统一管理类
 *
 *
 *
 */
public class Logger
{

    private Logger()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.v(tag, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg, boolean isDebugForce)
    {
        if (isDebug || isDebugForce)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg, boolean isDebugForce)
    {
        if (isDebug || isDebugForce)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg, boolean isDebugForce)
    {
        if (isDebug || isDebugForce)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg, boolean isDebugForce)
    {
        if (isDebug || isDebugForce)
            Log.v(tag, msg);
    }

    /**
     * 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制
     * 所以这里使用自己分节的方式来输出足够长度的message
     */
    private static void showMoreLog(String TAG, String str)
    {

        str = str.trim();

        int index = 0;

        int maxLength = 4000;

        String sub;

        while (index < str.length())
        {

            // java的字符不允许指定超过总的长度end

            if (str.length() <= index + maxLength)
            {

                sub = str.substring(index);

            }
            else
            {

                sub = str.substring(index, maxLength);

            }

            index += maxLength;

            Log.d(TAG, sub.trim());

        }

    }

}
