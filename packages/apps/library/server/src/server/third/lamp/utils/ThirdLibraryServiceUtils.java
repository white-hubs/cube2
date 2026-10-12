package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.model.Pagination;

import java.util.ArrayList;
import java.util.List;

public class ThirdLibraryServiceUtils {

    private ThirdLibraryServiceUtils() {
    }

    public static <T> Pagination<T> getObjPage(Object data, Class<T> clazz) {
        if (data == null) {
            return Pagination.empty();
        }
        JSONObject jsonObject = (JSONObject) data;
        Integer total = jsonObject.getInteger("total");
        Integer pageSize = jsonObject.getInteger("perpage");
        JSONArray jsonArray = jsonObject.getJSONArray("datalist");
        int i = (int) Math.ceil(Double.parseDouble(total + ".0") / pageSize);
        List<T> list = new ArrayList<>();
        if (jsonArray != null && !jsonArray.isEmpty()) {
            list = jsonArray.toJavaList(clazz);
        }
        return new Pagination<>(total.longValue(), (long) i, list);
    }
}
