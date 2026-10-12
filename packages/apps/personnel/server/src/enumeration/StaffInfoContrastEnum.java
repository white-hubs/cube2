package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/09
 * @description 员工信息对照表
 */
@Getter
public enum StaffInfoContrastEnum {

    PRIMARY_SCHOOL("小学"),
    MIDDLE_SCHOOL("初中"),
    HIGH_SCHOOL("高中"),
    TECHNICAL_SECONDARY_SCHOOL("中专"),
    JUNIOR_COLLEGE("大专"),
    UNDERGRADUATES("本科"),
    postgraduate("研究生");

    String value;

    StaffInfoContrastEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        StaffInfoContrastEnum[] values = StaffInfoContrastEnum.values();
        for (StaffInfoContrastEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
