package com.guhecloud.cube.biz.linkage.server.record.domain.enumeration;

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

    ALARM_UN_CONFIRMED(0, "待接收"),

    ALARM_PROGRESS_CONFIRMED(1, "处理中"),

    ALARM_SUSPEND(1, "已挂起"),

    ALARM_HANDLER(2, "已处理");

    /**
     * 描述信息
     */
    private final String description;

    /**
     * 序号
     */
    private final Integer order;

    AlarmStatusEnum(@NonNull Integer order, @NonNull String description) {
        this.order = order;
        this.description = description;
    }

}
