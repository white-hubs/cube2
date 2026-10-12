package com.guhecloud.cube.biz.linkage.server.record.domain.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 报警确认结果
 *
 * @author lanchun
 * @date 2022/6/6 16:13
 */
@Getter
public enum AlarmHandlerResultEnum {

    ALARM_ACTUAL("实报"),

    ALARM_IMPRACTICAL("误报");

    /**
     * 描述信息
     */
    private final String description;

    AlarmHandlerResultEnum(@NonNull String description) {
        this.description = description;
    }

    public static AlarmHandlerResultEnum getAlarmImpractical(String name) {
        for (AlarmHandlerResultEnum ex : AlarmHandlerResultEnum.values()) {
            if (ex.name().equals(name)) {
                return ex;
            }
        }
        return null;
    }
}
