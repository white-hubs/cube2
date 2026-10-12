package com.guhecloud.cube.base.flow.context.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/09/14
 * @description 表单状态
 */
@Getter
public enum FormFlowStatusEnum {
    /**
     * 未使用
     */
    NOT_USED("未使用"),

    DELETED("已删除"),

    /**
     * 使用中
     */
    IN_USE("使用中");

    String value;

    FormFlowStatusEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        FormFlowStatusEnum[] values = FormFlowStatusEnum.values();
        for (FormFlowStatusEnum statusEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", statusEnum.getValue());
            map.put("value", statusEnum.name());
            list.add(map);
        }
        return list;
    }

}
