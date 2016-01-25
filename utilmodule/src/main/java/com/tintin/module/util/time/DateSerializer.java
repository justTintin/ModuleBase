package com.tintin.module.util.time;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer
  implements JsonSerializer<Date>
{
  public static final String defaultPattern = "yyyy-MM-dd HH:mm:ss";

  /**
   * 日期格式转化的样式字符串
   */
  private String dateFormat;

  private SimpleDateFormat simpleDateFormat;

  public String getDateFormat() {
    return this.dateFormat;
  }

  /**
   * 默认构造器
   */
  public DateSerializer() {
    this(DateSerializer.defaultPattern);
  }

  /**
   * 指定格式的构造函数
   * @param dateFormat
   */
  public DateSerializer(String dateFormat) {
    super();
    this.dateFormat = dateFormat;
    this.simpleDateFormat = new SimpleDateFormat(this.dateFormat);
  }

  @Override
  public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
    String value = null;
    if (null == date) {
      value = "";
    } else {
      value = this.simpleDateFormat.format(date);
    }

    return new JsonPrimitive(value);
  }
}
