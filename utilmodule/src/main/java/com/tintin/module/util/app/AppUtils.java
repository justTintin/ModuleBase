package com.tintin.module.util.app;

/**
 * Created by Administrator on 2015/12/2.
 */

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

/**
 * 跟App相关的辅助类
 *
 *
 *
 */
public class AppUtils
{
    private static final String TAG = AppUtils.class.getSimpleName();

    private AppUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            return packageInfo.versionName;

        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isWeixinAvilible(Context context)
    {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null)
        {
            for (int i = 0; i < pinfo.size(); i++)
            {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm"))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
    *
            * @param context
    * @return
            */
    public static boolean isQQClientAvailable(Context context)
    {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null)
        {
            for (int i = 0; i < pinfo.size(); i++)
            {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeiboClientAvailable(Context context)
    {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null)
        {
            for (int i = 0; i < pinfo.size(); i++)
            {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean installApk(Context context, String filePath)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile())
        {
            i.setDataAndType(Uri.parse("file://" + filePath),
                    "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        else
        {
            Log.e(TAG, "" + file.isFile() + file.exists() + file.length());
            return false;
        }

    }

    /**
     * 取得包信息
     * @param context
     * @return
     */
    public static PackageInfo getPackageName(Context context)
    {
        PackageInfo info = null;
        try
        {
            info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的版本号
            int versionCode = info.versionCode;
            // 当前版本的包名
            String packageNames = info.packageName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return info;
    }



}