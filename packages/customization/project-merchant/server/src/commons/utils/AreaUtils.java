package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import java.lang.reflect.Field;
import java.util.List;

public class AreaUtils {

    /**
     * 根据区域ID数组，把区域分级设置到各属性
     * @param areaIds 区域ID数组
     * @param t 接收区域ID的类
     * @return
     */
    public static Object setAreaIdToClass(List<String> areaIds, Object t) {
        Class<? extends Object> tClass = t.getClass();
        if (areaIds == null || areaIds.size() == 0) return t;
        try {
            Field areaId1 = tClass.getDeclaredField("areaId1");
            Field areaId2 = tClass.getDeclaredField("areaId2");
            Field areaId3 = tClass.getDeclaredField("areaId3");
            areaId1.setAccessible(true);
            areaId2.setAccessible(true);
            areaId3.setAccessible(true);
            areaId1.set(t, areaIds.get(0));
            areaId2.set(t, areaIds.size() > 1 ? areaIds.get(1) : "");
            areaId3.set(t, areaIds.size() > 2 ? areaIds.get(2) : "");
        } catch (Exception e) {

        }
        return t;
    }

}
