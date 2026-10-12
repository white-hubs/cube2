package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 报警状态枚举
 * @date 2023/9/20 10:37
 */
@Getter
public enum AlarmStatusEnum {
    ALARM_UN_CONFIRMED("待接收"),

    ALARM_PROGRESS_CONFIRMED("处理中"),

    ALARM_SUSPEND("已挂起"),

    ALARM_HANDLER("已处理");
    private final String desc;

    AlarmStatusEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(AlarmStatusEnum.valueOf(name)) ? "" :
               AlarmStatusEnum.valueOf(name).getDesc();
    }
}
