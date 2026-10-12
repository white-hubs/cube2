package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
public enum EventTypeEnum {
    UNKNOWN(0, "未知"),
    CARD_OPEN(1, "刷卡开门"),
    BUTTON_OPEN(2, "按钮开门"),
    REMOTE_OPEN(3, "远程开门"),
    PUBLIC_PWD_OPEN(8, "公共密码开门"),
    PERSONAL_PWD_OPEN(12, "个人密码开门"),
    INVALID_OPEN(16, "无效开门(无权限)"),
    BLUETOOTH_OPEN(98, "蓝牙开门"),
    APP_OPEN(99, "APP开门"),
    QRCODE_OPEN(103, "二维码开门"),
    FACE_OPEN(104, "人脸识别开门");
    private final Integer code;
    private final String desc;

    @Nullable

    public static String getDescBy(Integer eventType) {
        for (EventTypeEnum eventTypeEnum : values()) {
            if (eventTypeEnum.getCode().equals(eventType)) {
                return eventTypeEnum.getDesc();
            }
        }
        return null;
    }

    @Nullable
    public static EventTypeEnum getBy(Integer eventType) {
        for (EventTypeEnum eventTypeEnum : values()) {
            if (eventTypeEnum.getCode().equals(eventType)) {
                return eventTypeEnum;
            }
        }
        return null;
    }

    public static String getEventResult(Integer eventType) {
        EventTypeEnum eventTypeEnum = getBy(eventType);
        if (eventTypeEnum != null && eventTypeEnum.equals(EventTypeEnum.INVALID_OPEN)) {
            return EventResultEnum.DO_REFUSE.name();
        }

        return EventResultEnum.DO_PASS.name();
    }
}
