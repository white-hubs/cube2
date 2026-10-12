package com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * 处理方式
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum TaskStatusEnum {
    WEB_FINISH("已完成"),
    FINISH("已完成"),
    UNDONE("未完成");
    private final String desc;

    TaskStatusEnum(String desc) {
        this.desc = desc;
    }

    public static String getTaskStatusName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        for (TaskStatusEnum ex : TaskStatusEnum.values()) {
            if (name.equals(ex.name())) {
                return ex.desc;
            }
        }
        return "";
    }
}
