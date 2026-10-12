package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 联动通知时间点类型
 *
 * @author lanchun
 * @date 2022/6/21 17:19
 */
public enum MessageNotifyTypeEnum {
    IMMEDIATELY_NOTIFY("及时通知"),
    DELAY_NOTIFY("延迟通知"),
    TIMEOUT_NOTIFY("超时通知");

    /**
     * 描述信息
     */
    private final String description;

    MessageNotifyTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
