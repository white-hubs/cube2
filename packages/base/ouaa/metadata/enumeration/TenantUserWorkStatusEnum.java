package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

/**
 * 租户人员状态
 */
public enum TenantUserWorkStatusEnum {

    REST("休息"),

    WORKING("在岗"),

    LEAVE_POST("离岗"),

    ASK_LEAVE("请假");

    final String value;

    TenantUserWorkStatusEnum(String value) {
        this.value = value;
    }

}
