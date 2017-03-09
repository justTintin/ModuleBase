package me.tintin.module.util.data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.GsonBuilder;
import me.tintin.module.util.time.CalendarSerializer;
import me.tintin.module.util.time.DateSerializer;
import me.tintin.module.util.time.TimestampSerializer;

/**
 * [一句话功能简述]
 * [功能详细描述]
 *
 * @author Administrator
 * @version [DoronApp, 2015/12/5 13:26]
 */
public class GsonUtils
{
    public static GsonBuilder createCommonBuilder()
    {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class, new DateSerializer())
                .registerTypeHierarchyAdapter(Calendar.class,
                        new CalendarSerializer())
                .registerTypeHierarchyAdapter(Timestamp.class,
                        new TimestampSerializer())
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableInnerClassSerialization();

        return gsonBuilder;
    }
}
