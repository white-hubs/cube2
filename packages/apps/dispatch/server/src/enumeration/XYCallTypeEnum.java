package com.guhecloud.cube.biz.dispatch.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 讯一电话类型枚举
 *
 * @author appet
 * @date 2023/9/21
 */
@Getter
public enum XYCallTypeEnum {

    CT_1("单呼"),
    CT_12("单呼"),
    CT_2("组呼"),
    CT_3("通播"),
    CT_4("广播"),
    CT_5("会议"),
    CT_6("强插"),
    CT_7("强拆"),
    CT_8("监听"),
    CT_9("对讲"),
    CT_10("转接"),
    CT_13("临时对讲"),
    CT_14("监控"),
    CT_15("视频会议"),
    CT_16("紧急呼叫"),
    CT_17("视频转发"),
    CT_19("视频回传"),

    ;

    /**
     * 描述信息
     */
    private final String desc;

    XYCallTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
