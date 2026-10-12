package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.PersonnelAbility;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/8
 * @description 员工状态
 */
@Getter
public enum StaffStatusEnum {

    FORMAL("正式"),
    LEAVE_OFFICE("离职"),
    ON_TRIAL("试用");

    String value;

    StaffStatusEnum(String value) {
        this.value = value;
    }

    public static String getNameByValues(String value) {
        String name = "";
        StaffStatusEnum[] values = StaffStatusEnum.values();
        for (StaffStatusEnum typeEnum : values) {
            if (typeEnum.value.equals(value)) {
                name = typeEnum.name();
            }
        }
        return name;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        StaffStatusEnum[] values = StaffStatusEnum.values();
        for (StaffStatusEnum typeEnum : values) {
            // 试用且没有能力时忽略
            if (typeEnum.equals(ON_TRIAL) && !PersonnelAbility.regularWorkerFlag()) {
                continue;
            }
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
