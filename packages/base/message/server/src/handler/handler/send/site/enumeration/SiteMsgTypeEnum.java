package com.guhecloud.cube.base.message2.server.handler.handler.send.site.enumeration;

import lombok.Getter;

/**
 * 站内信类型
 *
 * @author lanchun
 * @date 2022/6/7 9:37
 */
@Getter
public enum SiteMsgTypeEnum {
    NOTICE_MESSAGE("通知消息"),
    ALARM_MESSAGE("报警消息"),
    DEVICE_SUBSCRIBE_MESSAGE("设备订阅消息"),
    DEVICE_ATTRIBUTE_CHANGE("设备属性变化"),
    AUTHORITY_REFRESH_MESSAGE("权限刷新消息");

    /**
     * 描述信息
     */
    private final String description;

    SiteMsgTypeEnum(String description) {
        this.description = description;
    }
}
