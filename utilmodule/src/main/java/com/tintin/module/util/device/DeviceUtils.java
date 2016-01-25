package com.tintin.module.util.device;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

/*
* [设备相关工具类 ]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/14] 
*/
public class DeviceUtils
{

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

    public static void phoneCall(Context context,String phnum)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phnum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
