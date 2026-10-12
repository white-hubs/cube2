package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-10  09:45
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalOverrunAlarmRecord {
    private Long id;
    /**
     * 记录时间
     */
    private LocalDateTime monitoringTime;
    /**
     * 设备id
     */
    private Long deviceId;
    private String deviceName;
    /**
     * 所属部门
     */
    private Long depId;
    private String depName;
    /**
     * 企业编号
     */
    private Integer tenant;
    private String tenantName;
    /**
     * 设备状态
     */
    private String status;
    /**
     * 指标数据
     */
    private String recordData;
    private Integer number;
}
