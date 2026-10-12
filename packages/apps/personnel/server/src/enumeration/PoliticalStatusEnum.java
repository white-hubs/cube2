package com.guhecloud.cube.srv.gen.personnel.management.server.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/9
 * @description 政治面貌
 */
@Getter
public enum PoliticalStatusEnum {

    QZ("群众"),
    GQTY("共青团员"),
    RDJJFZ("入党积极分子"),
    YBDY("预备党员"),
    DY("党员"),
    ;

    String value;

    PoliticalStatusEnum(String value) {
        this.value = value;
    }

    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        PoliticalStatusEnum[] values = PoliticalStatusEnum.values();
        for (PoliticalStatusEnum typeEnum : values) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", typeEnum.getValue());
            map.put("value", typeEnum.name());
            list.add(map);
        }
        return list;
    }

}
