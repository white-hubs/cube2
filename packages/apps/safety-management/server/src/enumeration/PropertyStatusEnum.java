package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @author wql
 * @describe 资产状态枚举
 * @date 2023/9/13 17:00
 */
@Getter
public enum PropertyStatusEnum {
    UNDER("审批中"),
    LEAVE_UNUSED("闲置"),
    MAKE_USE_OF("在用"),
    WEED_OUT("报废"),
    DELETE("已删除"),
    REPAIR_REPORT("报修"),
    EXPIRE("已过期"),
    NORMAL("未过期");
    private final String remarks;

    PropertyStatusEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getRemarks(String name) {
        if (ObjectUtils.isNotEmpty(name)) {
            for (PropertyStatusEnum ex : PropertyStatusEnum.values()) {
                if (ex.name().equals(name)) {
                    return ex.getRemarks();
                }
            }
        }
        return "";
    }
}
