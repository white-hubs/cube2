package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 报警通知人员获取策略枚举
 *
 * @author lanchun
 * @date 2022/10/31 15:41
 */
public enum MessageTargetNotifyEnum {

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

    MessageTargetNotifyEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
