package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MeterDeviceExcelVO {
    /**
     * 设备编码
     */
    private String code;
    //设备名称
    private String deviceName;
    /**
     * 仪表类型中文名
     */
    private String instrumentName;
    //设备区域
    private String deviceArea;
    //读数
    private BigDecimal readeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //上期用量
    private BigDecimal lastReadeValue;
    //上级设备
    private String parentName;

}
