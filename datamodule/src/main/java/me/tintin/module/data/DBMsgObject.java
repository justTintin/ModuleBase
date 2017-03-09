package me.tintin.module.data;

import java.util.List;

import android.content.ContentValues;


public class DBMsgObject <T extends BaseModel>{
    public Class<T> claz;
    public List<ContentCondition<T>> contentConditionList;
    DBOperateAsyncListener listener;
    DBOperateDeleteListener deleteListener;
    
    public static class ContentCondition <T extends BaseModel>{
    	public String whereClause;
    	public String[] whereArgs;
    	public ContentValues contentValues;
    	public T model;
    }
}
