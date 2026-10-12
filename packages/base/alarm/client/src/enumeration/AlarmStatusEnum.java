package com.guhecloud.cube.base.alarm.client.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 报警确认状态
 *
 * @author lanchun
 * @date 2022/6/6 16:08
 */
@Getter
public enum AlarmStatusEnum {

    ALARM_UN_CONFIRMED("待接收"),

    ALARM_PROGRESS_CONFIRMED("处理中"),

    ALARM_SUSPEND("已挂起"),

    ALARM_HANDLER("已处理");

    /**
     * 描述信息
     */
    private final String description;

    AlarmStatusEnum(@NonNull String description) {
        this.description = description;
    }

}
