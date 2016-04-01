package com.tintin.networkmod.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.Handler;
import android.os.Message;
import android.util.Log;



/**
 * [一句话功能简述]
 * [功能详细描述]
 *
 * @author Administrator
 * @version [DoronApp, 2016/3/7 15:50]
 */
public class UDPClient implements Runnable
{

    public static final String TAG = UDPClient.class.getSimpleName();

    private byte[] msg;

    public boolean start;

    private String mUdpIp;

    private Handler mHandler;

    private int port;

    private int mTime = 500;

    /**
     * @param msg
     */
    public UDPClient(String udpIp, int port, Handler mHandler, byte[] msg,
            int time)
    {
        super();
        mUdpIp = udpIp;
        this.msg = msg;
        this.mHandler = mHandler;
        this.port = port;
        this.mTime = time;
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(mTime);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            InetAddress serverAddr = InetAddress.getByName(mUdpIp);
            DatagramSocket socket = new DatagramSocket();
            Log.d(TAG, "udp== " + mUdpIp + " " + port);
            //            for (int i = 0; i < msg.length; i++)
            //            {
            //                Logger.d(TAG, "msg:i==" + i +" =="+ msg[i]);
            //            }
            if (null != msg)
            {
                DatagramPacket packet = new DatagramPacket(msg, msg.length,
                        serverAddr, port);
                socket.send(packet);
                Message message = new Message();
                message.obj = msg;
                message.what = 0;
                mHandler.sendMessage(message);
            }
            else
            {
                Message message = new Message();
                message.obj = msg;
                message.what =1;
                mHandler.sendMessage(message);
            }

        }
        catch (Exception e)
        {
            Message message = new Message();
            message.obj = msg;
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }
}
