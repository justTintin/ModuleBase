package me.tintin.module.view.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
* [自己定义webview]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/1/27] 
*/
public class AppWebView extends WebView
{
    private static final String TAG = AppWebView.class.getSimpleName();

    //JS发起请求的关键key
    public static final String JS_OACTION_HEAD = "oAction";

    private JsInteration mJsInteration;

    private String mLoadUrl;

    private Context mContext;

    private OnWebViewInteractionListener mListener;

    private WebSettings mWebSettings;

    public AppWebView(Context context)
    {
        super(context);
    }

    public void setAppWebView(OnWebViewInteractionListener listener,
            Context context, JsInteration jsInteration, String url)
    {
        mJsInteration = jsInteration;
        mLoadUrl = url;
        mContext = context;
        mListener = listener;
        initWebViewSetting();
    }

    public AppWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private void initWebViewSetting()
    {
        mWebSettings = getSettings();
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        //        {
        //            WebView.setWebContentsDebuggingEnabled(true);
        //        }
        //设置支持JavaScript脚本

        mWebSettings.setJavaScriptEnabled(true);
        addJavascriptInterface(mJsInteration, JS_OACTION_HEAD);
        //水平不显示
        setHorizontalScrollBarEnabled(false);
        //垂直不显示
        setVerticalScrollBarEnabled(false);

        //设置可以访问文件
        mWebSettings.setAllowFileAccess(true);
        //设置支持缩放
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setBlockNetworkImage(true);
        mWebSettings.setDatabaseEnabled(true);
        //        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //        mWebSettings.setDatabasePath(dir);

        //打开使用缓存
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        //        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //使用缓存：

        if (Build.VERSION.SDK_INT >= 19)
        {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebSettings.setLoadsImagesAutomatically(true);
        }
        else
        {
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        }
        //mWebSettings.setGeolocationDatabasePath(dir);
        setWebChromeClient(new android.webkit.WebChromeClient()
        {

            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    mWebSettings.setBlockNetworkImage(false);
                    mListener.onWebViewLoadFinish();
                }
                else
                {
                    mListener.onWebViewLoadStart();
                }

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                    JsResult result)
            {
                return super.onJsAlert(view, url, message, result);

            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                    String message, JsResult result)
            {
                return super.onJsConfirm(view, url, message, result);
            }

            //关键代码，以下函数是没有API文档的，所以在Eclipse中会报错，如果添加了@Override关键字在这里的话。

            // For Android 3.0+

            public void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mListener.openFileChooser(uploadMsg);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg,
                    String acceptType)
            {
                mListener.openFileChooser(uploadMsg, acceptType);

            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                    String acceptType, String capture)
            {
                mListener.openFileChooser(uploadMsg, acceptType, capture);

            }

            //Android 5.0+
            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams)
            {

                return mListener.onShowFileChooser(webView,
                        filePathCallback,
                        fileChooserParams);
            }

        });

        //设置WebViewClient
        setWebViewClient(new WebViewClient()
        {

            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, SslError error)
            {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed(); // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url)
            {

                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);

            }

        });

        //判断当前URL是否有效
        preLoadUrl(mLoadUrl);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppWebView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AppWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 预请求
     * @param loadUrl
     */
    public void preLoadUrl(final String loadUrl)
    {
        //        //创建一个Request
        //        final Request request = new Request.Builder().url(loadUrl).build();
        //
        //        //请求加入调度
        //        OkHttpUtils.enqueue(request, new Callback()
        //        {
        //            @Override
        //            public void onFailure(Request request, IOException e)
        //            {
        //                Logger.i(TAG, "onFailure:" + loadUrl + "head:"
        //                        + request.headers().toString());
        //                mListener.onWebViewPreLoadFailed(CommonNetConstant.CODE.ERROR_CODE_SEVER);
        //            }
        //
        //            @Override
        //            public void onResponse(final Response response) throws IOException
        //            {
        //                if (response.code() == CommonNetConstant.CODE.OK_CODE_200)
        //                {
        //
        //                    mListener.onWebViewPreLoadSuccess(loadUrl);
        //                    //loadDataWithBaseURL(mLoadUrl);
        //
        //                }
        //                else
        //                {
        //                    mListener.onWebViewPreLoadFailed(response.code());
        //                }
        //
        //            }
        //        });

    }

    public interface OnWebViewInteractionListener
    {
        void onWebViewLoadFinish();

        void onWebViewLoadStart();

        void onWebViewPreLoadFailed(int errorCode);

        void onWebViewPreLoadSuccess(String url);

        void openFileChooser(ValueCallback<Uri> uploadMsg);

        void openFileChooser(ValueCallback uploadMsg, String acceptType);

        void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType,
                String capture);

        boolean onShowFileChooser(
                WebView webView,
                ValueCallback<Uri[]> filePathCallback,
                android.webkit.WebChromeClient.FileChooserParams fileChooserParams);
    }
}
