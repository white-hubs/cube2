package com.guhecloud.cube.base.flow.context.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/10/18
 * @description 应用列表
 */
@Getter
public enum ApplicationEnum {
    /**
     * 考勤管理
     */
    ATTENDANCE_MANAGEMENT("考勤管理"),
    PERSONNELMANAGEMENT("人事管理"),
    OFFICIAL_MANAGEMENT("行政管理"),
    PROPERTY_MANAGEMENT("资产管理"),
    WORK_REPORT("工作汇报"),
    HIDDENDANGER("隐患排查"),
    FAULT("故障报修"),
    SMALLENFINEERING("小散工程"),
    OUTSOURCE_REPAIR("委外维修"),
    RETROFIT("更新改造"),
    COMPLAINT("投诉建议"),
    PARKINGMANAGE("停车管理"),
    VISITOR_MANAGEMENT("访客管理"),
    MEETING_ROOM_MANAGEM("会议室管理"),
    HOTEL_MANAGEMENT("酒店管理");
    final String value;

    ApplicationEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        ApplicationEnum[] values = ApplicationEnum.values();
        for (ApplicationEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<>();
        ApplicationEnum[] values = ApplicationEnum.values();
        for (ApplicationEnum typeEnum : values) {
            map.put(typeEnum.name(), typeEnum.getValue());
        }
        return map;
    }

}
