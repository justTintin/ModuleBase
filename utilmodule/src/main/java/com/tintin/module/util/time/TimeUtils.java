package com.tintin.module.util.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2015/12/21] 
*/
public class TimeUtils
{

    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd-HH:mm:ss");

    public static String getCurTime()
    {

        String time = format.format(new Date());

        return time;

    }

}
