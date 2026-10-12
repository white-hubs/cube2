package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.guhecloud.platform.base.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StrUtils {

    /**
     * 将字符串转为集合
     */
    public static List<Long> StrToList(String ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());
        return idList;
    }
}
