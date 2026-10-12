package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 隐患业务配置枚举
 *
 * @author lzy
 * @date 2022/5/5
 */
@Getter
public enum HiddenDangerNoticeEnum {

    FLOATING_WINDOW_IN_STATION("站内浮窗通知"),

    POP_UP_WINDOW_IN_STATION("站内弹窗通知"),

    SMS("短信通知"),

    WECHAT("微信通知"),

    DINGDING("钉钉通知"),

    ;
    public final String desc;

    HiddenDangerNoticeEnum(@NonNull String desc) {
        this.desc = desc;
    }

}
