package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 隐患单状态枚举
 * @date 2023/9/11 17:03
 */
@Getter
public enum HiddenDangerStatusEnum {
    WAITING_CONFIRM("待确认"),
    WAITING_FOR_HANDLE("待处理"),
    TO_BE_CLOSED("待关闭"),
    CONFIRMED("已确认"),
    FINISHED("已结束");

    private final String desc;

    HiddenDangerStatusEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(HiddenDangerStatusEnum.valueOf(name)) ? "" :
               HiddenDangerStatusEnum.valueOf(name).getDesc();
    }
}
