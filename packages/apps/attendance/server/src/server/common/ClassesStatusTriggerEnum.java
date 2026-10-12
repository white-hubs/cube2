package com.guhecloud.cube.srv.gen.attendance.management.server.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 班次状态触发情况类型枚举
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum ClassesStatusTriggerEnum {
    CLOCK("打卡"),
    REPAIR_CLOCK("补卡"),
    OVERTIME("加班"),
    CHANGE_CLASSES("换班"),
    LEAVE("请假"),
    LEAVE_THE_POST("离岗"),
    AWAY_BUSINESS("出差"),
    ;

    private final String desc;

    ClassesStatusTriggerEnum(String desc) {
        this.desc = desc;
    }

    public static List<String> getAllName() {
        List<String> typeList = new ArrayList<>();
        typeList.add(ClassesStatusTriggerEnum.REPAIR_CLOCK.name());
        typeList.add(ClassesStatusTriggerEnum.OVERTIME.name());
        typeList.add(ClassesStatusTriggerEnum.LEAVE.name());
        typeList.add(ClassesStatusTriggerEnum.CHANGE_CLASSES.name());
        typeList.add(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
        typeList.add(ClassesStatusTriggerEnum.AWAY_BUSINESS.name());
        return typeList;
    }
}
