package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-05  11:24
 * @Description:
 * @Version: 1.0
 */
@Data
public class CorrectNormConfigVO {
    /**
     * 租户编码
     */
    private Integer tenant;
    /**
     * 修正值
     */
    private BigDecimal correctValue;
    /**
     * 指标编码
     */
    private String normCode;
    /**
     * 指标名称
     */
    private String normName;
    /**
     * 单位
     */
    private String unit;
    /**
     * 状态
     */
    private String state;
    /**
     * 修正时间
     */
    private LocalDateTime handleTime;
    /**
     * 设备信息
     */
    private DeviceVO device;
}
