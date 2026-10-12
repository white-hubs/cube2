package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @author wql
 * @describe 风险指标类型枚举
 * @date 2023/10/12 15:40
 */
@Getter
public enum RiskIndexTypeEnum {
    ALARM_NUMBER("报警数据"),
    ALARM_RESPONSE("报警响应"),
    HIDDEN_DANGER_NUMBER("隐患数量"),
    HIDDEN_DANGER_RESPONSE("隐患响应");

    private final String remarks;

    RiskIndexTypeEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getRemarks(String name) {
        if (ObjectUtils.isNotEmpty(name)) {
            for (RiskIndexTypeEnum ex : RiskIndexTypeEnum.values()) {
                if (ex.name().equals(name)) {
                    return ex.getRemarks();
                }
            }
        }
        return "";
    }
}
