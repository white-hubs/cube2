package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线损趋势数据 VO
 * 用于返回线损率随时间变化的趋势数据
 */
@Data
public class LineLossTrendVO {
    /**
     * 线路 ID
     */
    private Long lineId;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 线损率
     */
    private BigDecimal lossRate;

    /**
     * 输入电量 (kWh)
     */
    private BigDecimal inputEnergy;

    /**
     * 输出电量 (kWh)
     */
    private BigDecimal outputEnergy;

    /**
     * 采集时间
     */
    private LocalDateTime collectTime;
}
