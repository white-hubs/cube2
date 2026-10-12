package com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 考核结果类型枚举
 *
 * @author bq
 * @date 2023/02/22
 */
@Getter
public enum AssessResultEnum {
    A("优秀（9分以上）"),
    B("合格（6-9分）"),
    C("不及格（6分）"),
    D("未通过，延长试用期（6分以下）"),
    E("解除劳动合同");

    String value;

    AssessResultEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        AssessResultEnum[] values = AssessResultEnum.values();
        for (AssessResultEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("id", typeEnum.name());
            map.put("name", typeEnum.getValue());
            list.add(map);
        }
        return list;
    }

}
