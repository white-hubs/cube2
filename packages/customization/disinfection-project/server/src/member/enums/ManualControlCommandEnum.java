package com.guhecloud.cube.srv.gen.disinfection.server.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备手动控制指令枚举
 */
@AllArgsConstructor
@Getter
public enum ManualControlCommandEnum {
    /**
     * 驱蚊指令，需传时长（秒）
     */
    DRIVE_MOSQUITO("驱蚊指令", "mosquito_repellent", true, new ManualControlMethodParamEnum[]{ManualControlMethodParamEnum.TIMED_OFF,ManualControlMethodParamEnum.SWITCH}),
    /**
     * 雾灌指令，需传时长（秒）
     */
    MIST_SPRAY("雾灌指令", "mist_irrigation", true, new ManualControlMethodParamEnum[]{ManualControlMethodParamEnum.TIMED_OFF,ManualControlMethodParamEnum.SWITCH}),
    /**
     * 设备关闭
     */
    DEVICE_OFF("设备关闭", "close_all", false, new ManualControlMethodParamEnum[]{ ManualControlMethodParamEnum.SWITCH }),
    /**
     * 定时任务
     */
    SCHEDULED_TASK("定时任务", "schedule", false, new ManualControlMethodParamEnum[]{
            ManualControlMethodParamEnum.TIMED_OFF, ManualControlMethodParamEnum.PLAN_MODE, ManualControlMethodParamEnum.PLAN_SCHEDULE
    });

    private final String name;
    /**
     * 对应物模型方法标识，可按实际产品方法 code 调整
     */
    private final String methodCode;
    private final boolean needDuration;
    /**
     * 该方法支持的方法参数枚举数组（开关、定时关闭、计划模式、计划时间表等）
     */
    private final ManualControlMethodParamEnum[] methodParams;

    public boolean isNeedDuration() {
        return needDuration;
    }
}
