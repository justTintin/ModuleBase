package com.tintin.network.netstate;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;

/**
 *注册网络改变监听
 */
public class NetStatus
{
    private Handler mHandler;

    private NetworkConnectChangedReceiver networkConnectChangedReceiver;

    public NetStatus(Handler handler)
    {
        this.mHandler = handler;
    }

    public void registerNetStatusReceiver(final Context context)
    {

        networkConnectChangedReceiver = new NetworkConnectChangedReceiver(
                mHandler);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(networkConnectChangedReceiver, filter);
            }
        }).start();
    }

    public void unRegisterNetStatusReceiver(Context context)
    {
        if (networkConnectChangedReceiver != null)
        {
            context.unregisterReceiver(networkConnectChangedReceiver);
        }
    }
}
