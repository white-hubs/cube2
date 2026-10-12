package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线损采集记录 VO
 * 对应原型图中的表格数据
 */
@Data
public class LineLossReadRecordVO {

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 上期抄表时间
     */
    private LocalDateTime lastCollectTime;

    /**
     * 上次抄表数
     */
    private BigDecimal lastReadValue;

    /**
     * 本期抄表时间
     */
    private LocalDateTime currentCollectTime;

    /**
     * 抄表值
     */
    private BigDecimal readValue;

    /**
     * 用量
     */
    private BigDecimal useValue;
}
