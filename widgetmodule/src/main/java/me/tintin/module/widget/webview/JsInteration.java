package me.tintin.module.widget.webview;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

/*
* [JS调用的获取数据方法]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/4] 
*/
public class JsInteration
{
    private static final String TAG = JsInteration.class.getSimpleName();
    
    private Handler mHandler;
    
    private Activity mActivity;
    
    public static final int MSG_JS_STR = 999;
    
    /**
     * 绑定的object对象
     */
    // private Context mContext;
    
    public JsInteration(Activity activity, Handler handler)
    {
        this.mActivity = activity;
        this.mHandler = handler;
    }
    
    /*
     * JS调用android的方法
     * 
     * @JavascriptInterface仍然必不可少
     *
     */
    @JavascriptInterface
    public void postMessage(String jsonStr)
    {
        try
        {
            callJsMethod(jsonStr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void callJsMethod(String jsonStr)
    {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_JS_STR;
        msg.obj = jsonStr;
        mHandler.sendMessage(msg);
        
    }
}
