package com.guhecloud.cube.base.flow.context.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/10/18
 * @description 表单类型
 */
@Getter
public enum FormFlowTypeEnum {
    /**
     * 仅流程
     */
    FLOW("仅流程"),

    /**
     * 数据表单
     */
    FORM("数据表单"),

    /**
     * 流程表单
     */
    FLOW_FORM("流程表单"),

    /**
     * 自定义流程表单
     */
    CUSTOM_FORM("自定义流程表单"),
    ;

    String value;

    FormFlowTypeEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        FormFlowTypeEnum[] values = FormFlowTypeEnum.values();
        for (FormFlowTypeEnum typeEnum : values) {
            // 暂时只返回流程表单
            if (typeEnum == FLOW || typeEnum == FORM) {
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
