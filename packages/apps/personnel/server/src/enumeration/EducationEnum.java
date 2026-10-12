package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/10/18
 * @description 学历信息
 */
@Getter
public enum EducationEnum {

    PRIMARY_SCHOOL("小学"),
    MIDDLE_SCHOOL("初中"),
    HIGH_SCHOOL("高中"),
    TECHNICAL_SECONDARY_SCHOOL("中专"),
    JUNIOR_COLLEGE("大专"),
    UNDERGRADUATES("本科"),
    postgraduate("研究生"),
    //    DOCTORAL_CANDIDATE("博士"),
    ;

    String value;

    EducationEnum(String value) {
        this.value = value;
    }

    public static String getNameByValues(String value) {
        String name = "";
        EducationEnum[] values = EducationEnum.values();
        for (EducationEnum typeEnum : values) {
            if (typeEnum.value.equals(value)) {
                name = typeEnum.name();
            }
        }
        return name;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        EducationEnum[] values = EducationEnum.values();
        for (EducationEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
