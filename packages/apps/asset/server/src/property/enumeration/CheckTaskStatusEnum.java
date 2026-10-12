package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

@Getter
public enum CheckTaskStatusEnum {
    INCOMPLETE("未盘"),
    COMPLETED("已盘"),
    WIN("盘赢"),
    LOSS("盘亏"),
    ABNORMAL("异常");
    private final String remarks;

    CheckTaskStatusEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getCheckTaskStatusEnum(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        for (CheckTaskStatusEnum ex : CheckTaskStatusEnum.values()) {
            if (name.equals(ex.name())) {
                return ex.getRemarks();
            }
        }
        return "";
    }
}
