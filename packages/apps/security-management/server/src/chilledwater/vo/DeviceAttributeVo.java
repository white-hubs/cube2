package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

@Data
public class DeviceAttributeVo {
    //控制终端开关机
    private String controlBox;
    //水流状态
    private String waterFlow;
    //面板目标温度
    private String controlBoxTemperture;
    //制冷电磁阀
    private String reFrigeration;
    //钛泡初始温度
    private String oTiBubblesTemperature;
    //钛泡实时温度
    private String tiBubblesTemperature;
    //钛泡内水初始温度
    private String oWaterTemperature;
    //钛泡内水温度
    private String waterTemperature;
    //今日制冷运行时长
    private String dayTime;
    //本月制冷运行时长
    private String monthTime;
    //钛泡低温保护设置
    private String tiBubblesLower;
    //报警器
    private String alarm;
    //水流状态
    private String aAlarmWater;

    private String status;
    private Boolean deviceStatus;

}
