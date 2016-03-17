package com.tintin.module.util.file;

/**
 * Created by Administrator on 2015/12/2.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import com.tintin.module.util.ToastUtil;

/**
 * SD卡相关的辅助类
 */
public class SDCardUtils
{
    private SDCardUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 保存文件
     * @param fileName 文件名称
     * @param content  文件内容
     * @throws IOException
     */
    public static void saveToSDCard(String fileName, String content)
            throws IOException
    {
        //File file = new File(new File("/mnt/sdcard"),fileName);
        //考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
        File file = new File(SDCardUtils.getSDCardPath(), fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath)
    {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        }
        else
        {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public static void selectSystemPhone(final int photoCode, Activity activity)
    {

        if (!isSDCardEnable())
        {
            ToastUtil.show(activity, "没有SD卡", 0);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, photoCode);
    }
}
