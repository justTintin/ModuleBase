package me.tintin.module.util.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/*
* [Jackson的单例]
* [对象转json时用writevalue，json转对象时用readvalue]
* @author Administrator
* @version [DoronApp, 2016/2/18] 
*/
public class JacksonMapper
{
    private static final ObjectMapper mapper = new ObjectMapper();

    private JacksonMapper()
    {
    }

    public static ObjectMapper getInstance()
    {
        //缺少json的某个字段属性时忽略
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return mapper;
    }

    /**
     * 对象转json
     * @param object
     * @return
     */
    public  String writevalueTo(Object object)
    {
        String json = "";
        try
        {
            json = JacksonMapper.getInstance().writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * json string转对象
     * @param jsonStr
     */
    public  void readvalueTo(String jsonStr)
    {
        try
        {
            Object object = getInstance().readValue(jsonStr,
                    new TypeReference()
                    {
                        @Override
                        public int compareTo(Object another)
                        {
                            return 0;
                        }
                    });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
