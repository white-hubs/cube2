package com.guhecloud.cube.srv.gen.disinfection.server.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备手动控制方法参数枚举（参数名 + 参数 code）
 */
@AllArgsConstructor
@Getter
public enum ManualControlMethodParamEnum {
    /**
     * 开关
     */
    SWITCH("开关", "status"),
    /**
     * 定时关闭
     */
    TIMED_OFF("定时关闭", "auto_off"),
    /**
     * 计划模式
     */
    PLAN_MODE("计划模式", "m"),
    /**
     * 计划时间表
     */
    PLAN_SCHEDULE("计划时间表", "t_table");

    /**
     * 参数名
     */
    private final String name;
    /**
     * 参数 code（物模型参数标识）
     */
    private final String code;
}
