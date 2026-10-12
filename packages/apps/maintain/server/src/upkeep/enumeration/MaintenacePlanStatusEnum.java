package com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MaintenacePlanStatusEnum {
    DEACTIVATED("已停用"),
    ENABLED("已启用"),
    EXPIRED("已过期");

    private final String description;

    MaintenacePlanStatusEnum(@NonNull String description) {
        this.description = description;
    }

    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MaintenacePlanStatusEnum item : MaintenacePlanStatusEnum.values()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", item);
            map.put("code", item.description);
            list.add(map);
        }
        return list;
    }

}
