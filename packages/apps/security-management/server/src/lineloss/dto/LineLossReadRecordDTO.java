package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 读数记录
 */
@Data
public class LineLossReadRecordDTO {
    private Long deviceId;
    /**
     * 属性码
     */
    private String attributeCode;
    /**
     * 读数
     */
    private BigDecimal readValue;
    /**
     * 用量(本次和上次读数的差值)
     */
    private BigDecimal useValue;
    // 单位(暂时没有用，也没有存内容)
    private String unit;
    private Integer tenant;
    /**
     * 采集时间(是配置的时间，可能跟创建时间有一点点误差)
     */
    private LocalDateTime collectTime;
    /**
     * 上次读数
     */
    private BigDecimal lastReadValue;
    /**
     * 上次采集时间
     */
    private LocalDateTime lastCollectTime;
    /**
     * 创建时间(是实际插入时间)
     */
    private LocalDateTime createTime;
}
