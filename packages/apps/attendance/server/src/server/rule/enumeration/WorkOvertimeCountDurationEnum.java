package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班时长计算规则
 */
@Getter
public enum WorkOvertimeCountDurationEnum {

    APPROVAL("按加班审批通过的时长计算"),

    CLOCK("在加班审批时段内,按实际打卡时间计算"),

    NO_APPROVAL_CLOCK("无需审批,系统直接按实际打卡时间计算");

    private final String value;

    WorkOvertimeCountDurationEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        WorkOvertimeCountDurationEnum[] values = WorkOvertimeCountDurationEnum.values();
        for (WorkOvertimeCountDurationEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
