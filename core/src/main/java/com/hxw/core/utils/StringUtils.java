package com.hxw.core.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @author hxw on 2018/5/3.
 */
public final class StringUtils {

    @NonNull
    public static String jsonFormat(@NonNull String json) {
        if (json.isEmpty()) {
            return "json 数据为空!";
        } else {
            try {
                if (json.startsWith("{")) {
                    json = new JSONObject(json).toString(4);
                } else if (json.startsWith("[")) {
                    json = new JSONArray(json).toString(4);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    /**
     * 解析url的参数键值对
     */
    public static Map<String, String> urlRequestFormat(String str) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit;

        if (str == null) {
            return mapRequest;
        }
        try {
            URL url = new URL(str);
            String query = url.getQuery();
            //每个键值为一组
            arrSplit = query.split("[&]");
            for (String strSplit : arrSplit) {
                String[] arrSplitEqual;
                arrSplitEqual = strSplit.split("[=]");
                //解析出键值
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("解析后数组越界,url格式有问题");
        }
        return mapRequest;
    }
}
