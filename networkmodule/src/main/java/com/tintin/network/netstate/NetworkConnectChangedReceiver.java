package com.tintin.network.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Parcelable;

import com.tintin.network.utils.NetUtils;

/**
 * 网络改变时的监听
 * 
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver
{

    private Handler mHandler;

    private boolean isWifiConnected = false;

    public NetworkConnectChangedReceiver(Handler handler)
    {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {

        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction()))
        {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra)
            {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                String wifiSsid = networkInfo.getExtraInfo();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                if (isConnected)
                {
                    isWifiConnected = true;
//                    mHandler.sendEmptyMessage(
//                            NetStatusFusionCode.WIFI_CONNECTED);
                    mHandler.sendMessage(mHandler.obtainMessage(
                            NetStatusFusionCode.NET_CONNECTED, wifiSsid));
                }
                else
                {
                    isWifiConnected = false;
//                    mHandler.sendEmptyMessage(
//                            NetStatusFusionCode.WIFI_DISCONNECTED);
                    mHandler.sendMessage(mHandler.obtainMessage(
                            NetStatusFusionCode.NET_DISCONNECTED, wifiSsid));
                }
            }
        }
        else
        {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                    && !isWifiConnected)
            {// 这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        0);
                switch (wifiState)
                {
                    case WifiManager.WIFI_STATE_DISABLED:
//                        mHandler.sendEmptyMessage(
//                                NetStatusFusionCode.WIFI_CLOSE);
                    break;
                    case WifiManager.WIFI_STATE_ENABLED:
//                        mHandler.sendEmptyMessage(
//                                NetStatusFusionCode.WIFI_OPEN);
                    break;

                    default:
                    break;
                    //
                }
            }
        }

        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
        {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (gprs.isConnected() && !wifi.isConnected())
            {
                NetworkInfo info = intent.getParcelableExtra(
                        ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null)
                {

//                    switch (getNetworkClass(info.getSubtype()))
//                    {
//                        case NetStatusFusionCode.NETWORK_CLASS_2_G:
//                            mHandler.sendEmptyMessage(
//                                    NetStatusFusionCode.NETWORK_CLASS_2_G);
//                        break;
//                        case NetStatusFusionCode.NETWORK_CLASS_3_G:
//                            mHandler.sendEmptyMessage(
//                                    NetStatusFusionCode.NETWORK_CLASS_3_G);
//                        break;
//                        case NetStatusFusionCode.NETWORK_CLASS_4_G:
//                            mHandler.sendEmptyMessage(
//                                    NetStatusFusionCode.NETWORK_CLASS_4_G);
//                        break;
//                        case NetStatusFusionCode.NETWORK_CLASS_UNKNOWN:
//                            mHandler.sendEmptyMessage(
//                                    NetStatusFusionCode.NETWORK_CLASS_UNKNOWN);
//                        break;
//                    }
                    if (NetUtils.isConnected(context)){
                        mHandler.sendMessage(mHandler.obtainMessage(
                                NetStatusFusionCode.NET_CONNECTED));
                    }else
                    {
                        mHandler.sendMessage(mHandler.obtainMessage(
                                NetStatusFusionCode.NET_DISCONNECTED));
                    }
                }
            }
            else if (!gprs.isConnected() && !wifi.isConnected())
            {
//                mHandler.sendEmptyMessage(NetStatusFusionCode.NET_DISCONNECTED);
                mHandler.sendMessage(mHandler.obtainMessage(
                        NetStatusFusionCode.NET_DISCONNECTED));
            }
        }
    }

    public int getNetworkClass(int networkSubType)
    {
        switch (networkSubType)
        {
            case NetStatusFusionCode.NETWORK_TYPE_GPRS:

            case NetStatusFusionCode.NETWORK_TYPE_EDGE:

            case NetStatusFusionCode.NETWORK_TYPE_CDMA:

            case NetStatusFusionCode.NETWORK_TYPE_1xRTT:

            case NetStatusFusionCode.NETWORK_TYPE_IDEN:

                return NetStatusFusionCode.NETWORK_CLASS_2_G;
            case NetStatusFusionCode.NETWORK_TYPE_UMTS:

            case NetStatusFusionCode.NETWORK_TYPE_EVDO_0:

            case NetStatusFusionCode.NETWORK_TYPE_EVDO_A:

            case NetStatusFusionCode.NETWORK_TYPE_HSDPA:

            case NetStatusFusionCode.NETWORK_TYPE_HSUPA:

            case NetStatusFusionCode.NETWORK_TYPE_HSPA:

            case NetStatusFusionCode.NETWORK_TYPE_EVDO_B:

            case NetStatusFusionCode.NETWORK_TYPE_EHRPD:

            case NetStatusFusionCode.NETWORK_TYPE_HSPAP:

                return NetStatusFusionCode.NETWORK_CLASS_3_G;
            case NetStatusFusionCode.NETWORK_TYPE_LTE:

                return NetStatusFusionCode.NETWORK_CLASS_4_G;
            default:
                return NetStatusFusionCode.NETWORK_CLASS_UNKNOWN;

        }
    }
}
