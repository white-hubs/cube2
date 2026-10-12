package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备读数记录导出对象
 */
@Data
public class ReadMeterLogExportVO {
    //设备编号
    private String deviceId;
    //设备编码
    private String deviceCode;
    //设备名称
    private String deviceName;
    // 上次读取时间
    private String preDateTimeStr;
    // 上次抄表数
    private BigDecimal preReadValue;
    // 当前读取时间
    private String curDateTimeStr;
    // 当前读取值
    private BigDecimal readValue;
    // 用量
    private BigDecimal usage;
}
