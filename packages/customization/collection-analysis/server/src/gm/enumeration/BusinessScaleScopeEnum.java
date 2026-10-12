package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 经营规模枚举
 */
@Getter
public enum BusinessScaleScopeEnum {
    THREE("0-3吨"),
    FIVE("3-5吨"),
    FIVE_ABOVE("5吨以上"),
    ;
    final String describe;

    BusinessScaleScopeEnum(String describe) {
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
        for (BusinessScaleScopeEnum value : BusinessScaleScopeEnum.values()) {
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
        for (BusinessScaleScopeEnum value : BusinessScaleScopeEnum.values()) {
            if (value.describe.equals(describe)) {
                return value.name();
            }
        }
        return "";
    }
}
