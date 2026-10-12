package com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration;

import com.guhecloud.platform.base.util.StringUtils;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/18 11:13
 * @Description:
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

    public static Gender getGender(String gender) {
        if (StringUtils.isBlank(gender)) {
            return null;
        }
        try {
            return Gender.valueOf(gender);
        } catch (Exception exception) {
            return null;
        }
    }
}
