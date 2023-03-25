package com.lin.bili.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JJsonUtils {
    /**
     * 防止前端吞long精度，用jackson的json序列化
     * @param o
     * @return
     */
    public static String parse(Object o)  {
        try {
            String jsonStr = new ObjectMapper().writeValueAsString(o);
            return jsonStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
