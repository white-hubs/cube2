package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MeterReadeRecordVo {
    private Long id;
    //上期用量
    private BigDecimal lastReadeValue;
    //本期用量
    private BigDecimal nowReadeValue;
    //上期抄表读数
    private BigDecimal readeLastValue;
    //本次抄表时间
    private LocalDateTime nowReadeTime;
    //上期抄表时间
    private LocalDateTime lastReadeTime;
    //设备名称
    private String deviceName;
    //设备id
    private String deviceId;
    /**
     * 设备编码
     */
    private String code;
    //本期读数
    private BigDecimal readeValue;
    //设备区域
    private String deviceArea;

}
