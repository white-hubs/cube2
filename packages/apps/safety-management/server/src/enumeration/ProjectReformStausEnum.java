package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/19 16:28
 * @Description:
 */
@Getter
public enum ProjectReformStausEnum {
    PASS("已完成"),
    UNDO("撤销"),
    REJECT("驳回"),
    ACTIVE("处理中");
    private final String description;

    ProjectReformStausEnum(String description) {
        this.description = description;
    }

}
