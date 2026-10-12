package com.guhecloud.cube.base.message.sdk.api.enumeration;

import lombok.Getter;

/**
 * 消息通知对象枚举
 *
 * @author lanchun
 * @date 2023/7/4 17:07
 */
@Getter
public enum MessageNotifyTargetEnum {

    DEPARTMENT("部门"),

    POST("岗位"),

    DEPARTMENT_POST("部门加岗位"),

    ROLE("角色"),

    USER_GROUP("用户组"),

    USER("用户");

    /**
     * 描述信息
     */
    private final String description;

    MessageNotifyTargetEnum(String description) {
        this.description = description;
    }
}
