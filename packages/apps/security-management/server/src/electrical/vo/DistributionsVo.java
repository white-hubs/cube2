package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DistributionsVo {
    //采集时间
    private LocalDateTime collectTime;
    //变压器a相电流
    private String electricA;
    //变压器温升a
    private String temperatureA;
    //变压器B相电流
    private String electricB;
    //变压器温升B
    private String temperatureB;
    //变压器C相电流
    private String electricC;
    //变压器温升C
    private String temperatureC;
    //功率因素
    private String powerFactor;
    //高压侧电压a
    private String voltageA;
    //高压侧电压d
    private String voltageD;
    //高压侧电压c
    private String voltageC;
}
