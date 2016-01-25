package com.tintin.networkmod.okhttp;

import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/3] 
*/public class OkHttpUtils
{
    private static final String TAG = OkHttpUtils.class.getSimpleName();

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static
    {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException
    {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback)
    {
        setSSL(mOkHttpClient);
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request)
    {
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {

            @Override
            public void onResponse(Response arg0) throws IOException
            {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1)
            {

            }
        });
    }

    public static String getStringFromServer(String url) throws IOException
    {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return responseUrl;
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 这里使用了HttpClinet的API。只是为了方便
     * @param params
     * @return
     */
    //    public static String formatParams(List<BasicNameValuePair> params)
    //    {
    //        return URLEncodedUtils.format(params, CHARSET_NAME);
    //    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    //    public static String attachHttpGetParams(String url,
    //            List<BasicNameValuePair> params)
    //    {
    //        return url + "?" + formatParams(params);
    //    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name,
            String value)
    {
        return url + "?" + name + "=" + value;
    }

    private static void setSSL(OkHttpClient mOkHttpClient)
    {
        try
        {
            //设置https信任所有证书
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException
                {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException
                {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            } }, new SecureRandom());
            mOkHttpClient.setSslSocketFactory(sc.getSocketFactory());
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG, "Exception");
        }

    }
}
