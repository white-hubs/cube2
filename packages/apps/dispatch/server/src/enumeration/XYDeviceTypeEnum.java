package com.guhecloud.cube.biz.dispatch.server.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 普天终端类型枚举
 *
 * @author appet
 * @date 2023/9/20
 */
@Getter
public enum XYDeviceTypeEnum {
    DT_0("桌面调度台"),
    DT_2("SIP终端"),
    DT_6("智能终端"),
    DT_7("监控设备"),
    DT_9("323终端"),
    DT_10("基站组"),
    DT_14("KLX中继台"),
    DT_17("维德终端"),
    DT_18("定位车辆"),
    DT_20("天通终端"),
    DT_21("自组网终端"),
    DT_110("外线手机"),
    DT_111("外线座机"),
    DT_201("手环"),
    DT_202("安全帽"),
    DT_203("智能布控球"),
    DT_204("无人机"),
    DT_205("空气检测仪"),
    DT_206("气象检测仪"),
    DT_207("便携调度台"),
    DT_208("小程序"),
    DT_211("执法记录仪"),
    DT_212("webrtc终端");

    /**
     * 描述信息
     */
    private final String desc;

    XYDeviceTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }
}
