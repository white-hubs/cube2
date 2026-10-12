package com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum AiKeCardTypeEnum {
    CARD_1("开门卡"),

    CARD_2("保安卡"),

    CARD_3("胁迫卡"),

    CARD_4("配置卡"),

    CARD_5("按钮"),

    CARD_6("蓝牙"),

    CARD_7("二维码"),

    CARD_8("密码"),

    CARD_9("人脸"),

    CARD_10("远程"),

    ;

    /**
     * 描述信息
     */
    private final String desc;

    AiKeCardTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
