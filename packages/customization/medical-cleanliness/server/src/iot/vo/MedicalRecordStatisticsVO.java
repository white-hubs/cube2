package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-19  14:11
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class MedicalRecordStatisticsVO {
    /**
     * 设备编号
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 总数
     */
    private Integer total;
    /**
     * 正常
     */
    private Integer normalTotal;
    /**
     * 异常
     */
    private Integer abnormalTotal;
    /**
     * 无效
     */
    private Integer invalidTotal;
    /**
     * 采集时间
     */
    private LocalDateTime monitoringTime;
}
