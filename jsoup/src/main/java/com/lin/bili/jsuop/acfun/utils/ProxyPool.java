package com.lin.bili.jsuop.acfun.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ProxyPool {
    private static List<String> proxyList;

    static {
        proxyList = new ArrayList<>();
        String s = HttpUtil.get("http://192.168.211.200:5010/all/");
        JSONArray objects = JSONUtil.parseArray(s);
        for (Object object : objects) {
            JSONObject jsonObject = (JSONObject) object;
            String proxy = (String) jsonObject.get("proxy");
            proxyList.add(proxy);
        }
    }

    public static String randomProxy() {
         return proxyList.get((int)( Math.random()*proxyList.size()));
    }
}
