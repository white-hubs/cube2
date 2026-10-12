package com.guhecloud.cube.biz.dispatch.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 普天终端类型枚举
 *
 * @author appet
 * @date 2022/8/1
 */
@Getter
public enum PTDeviceTypeEnum {
    DT_0("网络话机"),

    DT_1("外线手机"),

    DT_2("外线固话"),

    DT_10("调度台"),

    DT_31("手持终端"),

    DT_30("车载终端"),

    DT_50("智能终端"),

    DT_51("卫星电话"),

    DT_60("摄像头用户"),

    DT_80("卡片机"),

    DT_70("gps设备"),

    DT_90("无屏机设备"),

    DT_100("三防机"),

    DT_110("执法记录仪"),

    DT_120("无人机"),

    DT_130("国标设备"),

    DT_131("国标平台");

    /**
     * 描述信息
     */
    private final String desc;

    PTDeviceTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
