package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 * 性别枚举
 *
 * @author Woody
 * @date 2021/6/25
 */
public enum Gender {
    /**
     * 保密
     */
    NONE,

    /**
     * 男性
     */
    MALE,

    /**
     * 女性
     */
    FEMALE;

    public static List<String> getRemarks() {
        return Arrays.asList("男", "女", "保密");
    }

    public static Gender getByRemark(String remark) {
        if ("男".equals(remark)) {
            return MALE;
        } else if ("女".equals(remark)) {
            return FEMALE;
        }
        return NONE;
    }
}
