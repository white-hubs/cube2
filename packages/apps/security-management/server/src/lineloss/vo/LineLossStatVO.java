package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线损统计 VO
 * 对应原型图中的表格数据
 */
@Data
public class LineLossStatVO {
    private LocalDateTime collectTime;
    /**
     * 线路 ID
     */
    private Long lineId;

    /**
     * 线路名称
     */
    private String lineName;

    /**
     * 起点
     */
    private String fromNodeName;

    /**
     * 终点
     */
    private String toNodeName;

    /**
     * 起点位置
     */
    private String fromNodeLocation;

    /**
     * 终点位置
     */
    private String toNodeLocation;

    /**
     * 计量设备
     */
    private String deviceName;

    /**
     * 输入电量 (kWh)
     */
    private BigDecimal inputEnergy;

    /**
     * 输出电量 (kWh)
     */
    private BigDecimal outputEnergy;

    /**
     * 设备损耗 (kWh)
     */
    private BigDecimal lossEnergy;

    /**
     * 设备线损率 (%)
     */
    private BigDecimal lossRate;
}
