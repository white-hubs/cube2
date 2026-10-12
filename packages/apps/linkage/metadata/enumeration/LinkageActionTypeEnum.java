package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 联动动作类型
 *
 * @author lanchun
 * @date 2022/6/21 17:47
 */
public enum LinkageActionTypeEnum {
    ALARM_NOTIFY_ACTION("联动报警通知动作", 1),
    IOT_DEVICE_ACTION("联动物联设备动作", 2),
    VIDEO_DEVICE_ACTION("联动摄像机动作", 2),
    MESSAGE_NOTIFY_ACTION("联动消息通知动作", 2);

    /**
     * 描述信息
     */
    private final String description;

    /**
     * 顺序
     */
    private final Integer order;

    LinkageActionTypeEnum(String description, Integer order) {
        this.description = description;
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }
}
