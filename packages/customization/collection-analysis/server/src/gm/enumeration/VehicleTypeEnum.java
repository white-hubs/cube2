package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 采购车类型枚举
 */
@Getter
public enum VehicleTypeEnum {
    GOODS_TRAIN("货车"),
    VAN("面包车"),
    OTHER("其他车"),
    ;
    final String describe;

    VehicleTypeEnum(String describe) {
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
        for (VehicleTypeEnum value : VehicleTypeEnum.values()) {
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
        for (VehicleTypeEnum value : VehicleTypeEnum.values()) {
            if (value.describe.equals(describe)) {
                return value.name();
            }
        }
        return "";
    }
}
