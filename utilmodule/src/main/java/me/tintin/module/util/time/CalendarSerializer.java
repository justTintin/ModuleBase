/**
 * 
 */
package me.tintin.module.util.time;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 用于Calendar的格式化。它默认将日期格式化为 yyyy-MM-dd HH:mm:ss 格式的字符串，同时允许用户指定日期格式以覆盖默认行为。
 * 
 * @类名: CalendarSerializer.java
 */
public class CalendarSerializer implements JsonSerializer<Calendar> {

	public static final String DefaultPattern = "yyyy-MM-dd HH:mm:ss";

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
	public CalendarSerializer() {
		this(DateSerializer.defaultPattern);
	}

	/**
	 * 指定格式的构造函数
	 * 
	 * @param dateFormat
	 */
	public CalendarSerializer(String dateFormat) {
		super();
		this.dateFormat = dateFormat;
		this.simpleDateFormat = new SimpleDateFormat(this.dateFormat);
	}

	@Override
	public JsonElement serialize(Calendar calendar, Type type,
			JsonSerializationContext context) {
		String value = null;
		if (calendar == null) {
			value = "";
		} else {
			value = this.simpleDateFormat.format(calendar.getTime());
		}

		return new JsonPrimitive(value);
	}

}
