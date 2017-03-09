package me.tintin.module.zxing.zxingutil;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;



/**
 * [公共的工具类]<BR>
 * @author archermind
 * @version [ODP Client R001C01LAI141, Jun 3, 2014]
 */
public class ZxingUtil
{
    private static WakeLock sWakeLock;

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


}
