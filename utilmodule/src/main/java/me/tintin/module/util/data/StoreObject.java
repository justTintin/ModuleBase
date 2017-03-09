package me.tintin.module.util.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * 对象数据操作
 */
public class StoreObject
{

    private static final String TAG = StoreObject.class.getSimpleName();

    public static String saveOAuth(Object object)
    {
        String strBase64 = null;
        Log.i(TAG, "saveOAuth==" + new Gson().toJson(object));
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            strBase64 = new String(Base64.encodeBase64(baos.toByteArray()));

        }
        catch (IOException e)
        {
            // TODO Auto-generated
        }
        Log.i(TAG, "存储成功");
        return strBase64;
    }

    public static Object readOAuth(String storeName,
            SharedPreferences preferences)
    {
        Object object = null;
        String productBase64 = preferences.getString(storeName, "");
        if (!"".equals(productBase64))
        {

            //读取字节
            byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            try
            {
                //再次封装
                ObjectInputStream bis = new ObjectInputStream(bais);
                try
                {
                    //读取对象
                    object = bis.readObject();
                }
                catch (ClassNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch (StreamCorruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return object;
    }
}
