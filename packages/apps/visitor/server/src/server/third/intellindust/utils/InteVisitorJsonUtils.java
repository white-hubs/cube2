package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InteVisitorJsonUtils {
    public static <T> T getObjClazz(JSONObject data, Class<T> clazz) {
        if (data == null || clazz == null) {
            return null;
        }
        return data.toJavaObject(clazz);
    }

    public static <T> List<T> getObjArrList(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (jsonArray == null || jsonArray.isEmpty()) {
            return list;
        }
        List<T> arrList = jsonArray.toJavaList(clazz);
        if (arrList == null || arrList.isEmpty()) {
            return list;
        }
        return arrList;
    }

}
