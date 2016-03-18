package com.tintin.module.util.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.tintin.module.util.app.AppUtils;
import com.tintin.module.util.file.FileUtils;

import java.io.File;

/*
* [清除Webview相关数据]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/3/17] 
*/public class WebviewUtil
{

    private static final String TAG = WebviewUtil.class.getSimpleName();

    /**
     * 清除cookie
     *
     * @param context
     */
    private static void clearCookie(Context context)
    {

        //清空所有Cookie
        CookieSyncManager.createInstance(context); //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

    }

    /**
     * 清除webview缓存
     *
     * @param webView
     */
    private static void clearWebViewCache(WebView webView)
    {
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
    }

    /**
     * 根据删除文件的方法来删除
     *
     * @param dir
     * @param numDays
     * @return
     */
    private static int clearCacheFolderAll(Context context, File dir,
            long numDays)
    {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory())
        {
            clearCookie(context);
            try
            {
                for (File child : dir.listFiles())
                {
                    if (child.isDirectory())
                    {
                        deletedFiles += clearCacheFolderAll(context,
                                child,
                                numDays);
                    }
                    if (child.lastModified() < numDays)
                    {
                        if (child.delete())
                        {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch (Exception e)
            {

                e.printStackTrace();
            }

        }
        else
        {
            Log.d(TAG, "dir is null");
        }
        return deletedFiles;
    }

    /**
     * 清除webview缓存及app_webview目录下的所有文件
     * @param context
     */
    public static void clearWebviewdata(Context context)
    {
        String packageName = AppUtils.getPackageName(context).packageName;
        String pathAppWebView = "/data/data/" + packageName + "/app_webview/";
        int flag = clearCacheFolderAll(context,
                context.getCacheDir(),
                System.currentTimeMillis());
        if (flag > 0)
        {
            context.getApplicationContext().deleteDatabase("webview.db");
            context.getApplicationContext().deleteDatabase("webviewCache.db");
            FileUtils.DeleteFolder(pathAppWebView);
            Toast.makeText(context, "清除webview数据与缓存成功", Toast.LENGTH_SHORT)
                    .show();
        }
        else
        {
            Toast.makeText(context, "清除webview数据与缓存失败", Toast.LENGTH_SHORT)
                    .show();
        }

    }

}
