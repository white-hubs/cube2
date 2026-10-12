package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 隐患单状态枚举
 *
 * @author lzy
 * @date 2022/4/26
 */
@Getter
public enum HiddenDangerStatusEnum {

    WAITING_FOR_CONFIRM("待确认"),

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
