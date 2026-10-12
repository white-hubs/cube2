package com.guhecloud.cube.biz.ouaa.server.utils;

import java.util.Collections;
import java.util.List;

public class CollectionsUtils {

    /**
     * Long 集合比较是否相等
     *
     * @param oldList 集合1
     * @param nowList 集合2
     *
     * @return 是否相等
     */
    public static boolean eqLongList(List<Long> oldList, List<Long> nowList) {
        boolean result = true;
        if (oldList == null && nowList != null) {
            result = false;
        } else if (oldList != null && nowList == null) {
            result = false;
        } else if (oldList != null && nowList != null) {
            result = Collections.disjoint(oldList, nowList);
        }
        return result;
    }
}
