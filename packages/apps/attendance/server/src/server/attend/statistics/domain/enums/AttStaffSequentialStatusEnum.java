package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.domain.enums;

import lombok.Getter;

/**
 * 人员时序状态
 */
@Getter
public enum AttStaffSequentialStatusEnum {
    WORKING("在岗", 2),
    REST("休息", 1),
    LEAVE("请假", 5),
    POST("离岗", 3),
    AWAY("出差", 4),
    ;
    private final String value;
    // 优先级,值越大优先级越高
    private final Integer priority;

    AttStaffSequentialStatusEnum(String value, Integer priority) {
        this.value = value;
        this.priority = priority;
    }

    /**
     * 根据name获取枚举
     *
     * @param name 英文的name
     *
     * @return 对应枚举
     */
    public static AttStaffSequentialStatusEnum valueGetEnum(String name) {
        for (AttStaffSequentialStatusEnum attStaffSequentialStatusEnum : AttStaffSequentialStatusEnum.values()) {
            if (attStaffSequentialStatusEnum.name().equals(name)) {
                return attStaffSequentialStatusEnum;
            }
        }
        return AttStaffSequentialStatusEnum.REST;
    }
}
