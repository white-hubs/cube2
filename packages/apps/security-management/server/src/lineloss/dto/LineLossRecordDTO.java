package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线损记录
 */
@Data
public class LineLossRecordDTO {
    private Long lineId;
    private String lineName;
    /**
     * 开始节点id
     */
    private Long fromNodeId;
    private String fromNodeName;
    /**
     * 起点位置
     */
    private String fromNodeLocation;
    /**
     * 结束节点id
     */
    private Long toNodeId;
    private String toNodeName;
    /**
     * 终点位置
     */
    private String toNodeLocation;
    /**
     * 输入设备id
     */
    private Long fromDeviceId;
    private String fromDeviceName;
    /**
     * 输出设备id
     */
    private Long toDeviceId;
    private String toDeviceName;
    /**
     * 属性码
     */
    private String attributeCode;
    /**
     * 输入设备读数
     */
    private BigDecimal fromReadValue;
    /**
     * 输出设备读数
     */
    private BigDecimal toReadValue;
    /**
     * 输入设备用量(本次和上次读数的差值)
     */
    private BigDecimal fromUseValue;
    /**
     * 输出设备用量(本次和上次读数的差值)
     */
    private BigDecimal toUseValue;
    /**
     * 线路损失(输出设备用量-输入设备用量)
     */
    private BigDecimal lossValue;
    /**
     * 线路损失率(线路损失/输入设备用量)
     */
    private BigDecimal lossRate;
    // 单位(暂时没有用，也没有存内容)
    private String unit;
    private Integer tenant;
    /**
     * 采集时间(是配置的时间，可能跟创建时间有一点点误差)
     */
    private LocalDateTime collectTime;
    /**
     * 创建时间(是实际插入时间)
     */
    private LocalDateTime createTime;
}
