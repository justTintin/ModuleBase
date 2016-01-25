package com.tintin.module.util.graphic;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/3] 
*/
public class LruBitmapCache extends LruCache<String, Bitmap>
{

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public LruBitmapCache()
    {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes)
    {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value)
    {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
}