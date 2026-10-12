package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 性别枚举
 */
@Getter
public enum SexEnum {
    MALE("男"),
    FEMALE("女"),
    UNKNOWN("未知"),
    ;
    final String describe;

    SexEnum(String describe) {
        this.describe = describe;
    }

    /**
     * 根据编码获取描述
     *
     * @param code 编码
     *
     * @return 描述
     */
    public static String getDescribe(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        for (SexEnum value : SexEnum.values()) {
            if (value.name().equals(code)) {
                return value.describe;
            }
        }
        return "";
    }

    /**
     * 根据描述获取编码
     *
     * @param describe 描述
     *
     * @return 编码
     */
    public static String getCode(String describe) {
        if (StringUtils.isBlank(describe)) {
            return "";
        }
        for (SexEnum value : SexEnum.values()) {
            if (value.describe.equals(describe)) {
                return value.name();
            }
        }
        return "";
    }
}
