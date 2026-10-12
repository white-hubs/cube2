package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 仪表设备更换记录
 * @date 2024-01-26 10:08:36
 */
@Data
public class MeterDeviceChangeLogVO {

    //
    private Long id;
    //设备ID
    private Long deviceId;
    //设备当前读数
    private BigDecimal initValue;
    //被换设备当前读数
    private BigDecimal changedDeviceValue;
    //换表人姓名
    private String optUserName;
    //换表时间
    private LocalDateTime optTime;

}