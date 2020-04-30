package com.tanzhi.modelapi.utils;

import java.util.Map;
import java.util.regex.*;

import com.tanzhi.modelapi.model.Action.IAct2;

public class Utils {
    /**
     * 遍历一个Map类型
     * 
     * @param <K>    源数据Map Key的类型
     * @param <V>    源数据Map Value的类型
     * @param entity 源数据
     * @param action 遍历操作回调
     * @throws Exception
     */
    public static <K, V> void mapForeach(Map<K, V> entity, IAct2<K, V> action){
        for(Map.Entry<K, V> entry : entity.entrySet()){
            action.invoke(entry.getKey(), entry.getValue());
        }
    }

    public static Boolean isNullOrEmpty(String value){
        if(value==null || "".equals(value)){
            return true;
        }
        return false;
    }

    public static String getValueType(String value){
        if("none".equals(value.toLowerCase())||"null".equals(value.toLowerCase())||"undefined".equals(value.toLowerCase())){
            return "null";
        }

        if(Pattern.matches("^((([^0][0-9]+|0)\\.([0-9]{1,18}))$)|^(([1-9]+)\\.([0-9]{1,18})$)", value)){
            return "double";
        }

        if(Pattern.matches("^(([^0][0-9]+|0)$)|^(([1-9]+)$)", value)){
            return "number";
        }
        return "str";
    }

    public static Boolean strContain(String value,String[] values){
        for(String item:values){
            if(item.equals(value)){
                return true;
            }
        }
        return false;
    }
}