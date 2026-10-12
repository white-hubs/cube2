package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @author wql
 * @describe 指标项枚举
 * @date 2023/10/12 15:41
 */
@Getter
public enum RiskIndexItemEnum {
    ONE_LEVEL("一级报警"),
    TWO_LEVEL("二级报警"),
    THREE_LEVEL_AND_FOUR_LEVEL("三级、四级报警");
    //    FOUR_LEVEL("四级报警");

    private final String remarks;

    RiskIndexItemEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getRemarks(String name) {
        if (ObjectUtils.isNotEmpty(name)) {
            for (RiskIndexItemEnum ex : RiskIndexItemEnum.values()) {
                if (ex.name().equals(name)) {
                    return ex.getRemarks();
                }
            }
        }
        return "";
    }
}
