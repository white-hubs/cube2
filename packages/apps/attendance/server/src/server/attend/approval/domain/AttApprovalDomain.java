package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 *
 */
public class AttApprovalDomain {
    /**
     * 合并json对象
     * source覆盖target。即:如果有相同的key保留source的值
     *
     * @param source 源
     * @param target 目标
     *
     * @return 合并后的对象
     */
    public static JSONObject jsonMerge(JSONObject source, JSONObject target) {
        if (source == null) {
            source = new JSONObject();
        }
        if (target == null) {
            target = new JSONObject();
        }
        for (Map.Entry<String, Object> stringObjectEntry : source.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if (!target.containsKey(key)) {
                target.put(key, value);
            } else {
                if (value instanceof JSONObject valueJson) {
                    JSONObject targetValue = jsonMerge(valueJson, target.getJSONObject(key));
                    target.put(key, targetValue);
                } else if (value instanceof JSONArray valueArray) {
                    for (Object o : valueArray) {
                        JSONObject obj = (JSONObject) o;
                        JSONObject targetValue = jsonMerge(obj, target.getJSONObject(key));
                        target.put(key, targetValue);
                    }
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }
}
