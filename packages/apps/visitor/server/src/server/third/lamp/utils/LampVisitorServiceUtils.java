package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LampVisitorServiceUtils {
    public static <T> List<T> getObjArrList(JSONObject data, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) data.get("pageData");
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
