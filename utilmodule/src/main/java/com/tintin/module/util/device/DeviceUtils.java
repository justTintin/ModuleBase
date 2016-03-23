package com.tintin.module.util.device;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
* [设备相关工具类 ]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/14] 
*/
public class DeviceUtils
{

    private static PowerManager.WakeLock sWakeLock;

    /**
     * 获取机器的id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context)
    {
        String deviceId = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    /**
     * 格式化手机号号码
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNumber(String phoneNum)
    {
        String formatNo = "";
        if (phoneNum.startsWith("+86") || phoneNum.startsWith("086"))
        {
            formatNo = phoneNum.substring(3, phoneNum.length());
        }
        else if (phoneNum.startsWith("12593") || phoneNum.startsWith("12520"))
        {
            formatNo = phoneNum.substring(5, phoneNum.length());
        }
        else if (phoneNum.startsWith("086"))
        {
            formatNo = phoneNum.substring(3, phoneNum.length());
        }
        else if (phoneNum.startsWith("0"))
        {
            formatNo = phoneNum.substring(1, phoneNum.length());
        }
        else
        {
            formatNo = phoneNum;
        }
        return formatNo;
    }

    public static boolean checkPhoneNumber(String phoneNum)
    {
        if (phoneNum.startsWith("+86") || phoneNum.startsWith("086"))
        {
            return false;
        }
        else if (phoneNum.startsWith("12593") || phoneNum.startsWith("12520"))
        {
            return false;
        }
        else if (phoneNum.startsWith("086"))
        {
            return false;
        }
        else if (phoneNum.startsWith("0"))
        {
            return false;
        }
        else if (phoneNum.length() != 11)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 打电话功能，需要权限
     * @param context
     * @param phnum
     */
    public static void phoneCall(Context context, String phnum)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phnum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * [判断是否为移动号码]<BR>
     * @param phoneNum 传入的手机号码
     * @return 是否是移动的手机号
     */
    private static boolean isChinaMobileNum(String phoneNum)
    {
        if (phoneNum.matches("((134|135|136|137|138|139|147|150|151|152|157|158|159|182|187|188)[0-9])[0-9]{7}"))
        {
            return true;
        }
        else
        {
            return isPrefixNumber(phoneNum);
        }
    }

    private static boolean isPrefixNumber(String prefixNumber)
    {
        if (prefixNumber.startsWith("+86") || prefixNumber.startsWith("086"))
        {
            return isChinaMobileNum(prefixNumber.substring(3));
        }
        else if (prefixNumber.startsWith("12593")
                || prefixNumber.startsWith("12520"))
        {
            return isChinaMobileNum(prefixNumber.substring(5));
        }
        else if (prefixNumber.startsWith("086"))
        {
            return isChinaMobileNum(prefixNumber.substring(3));
        }
        else if (prefixNumber.startsWith("0"))
        {
            return isChinaMobileNum(prefixNumber.substring(1));
        }
        return false;
    }

    /**
     * 保持屏幕唤醒
     * @param context 上下文
     */
    public static void acquireWakeLock(Context context)
    {
        if (sWakeLock == null)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
                    context.getClass().getCanonicalName());
            sWakeLock.acquire();
        }
    }

    /**
     * 解除屏幕唤醒 <BR>
     */
    public static void releaseWakeLock()
    {
        if (sWakeLock != null && sWakeLock.isHeld())
        {
            sWakeLock.release();
            sWakeLock = null;
        }
    }

    private static long lastClickTime;

    private static long lastShowTime;

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void notFastShow(Context context)
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastShowTime;
        if (timeD > 8000)
        {
            Toast.makeText(context, "click too fast ", Toast.LENGTH_SHORT)
                    .show();
        }
        lastShowTime = time;

    }

    public static String[] getCpuInfo()
    {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = { "", "" };
        String[] arrayOfString;
        try
        {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++)
            {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        }
        catch (IOException e)
        {
        }
        return cpuInfo;
    }

}
