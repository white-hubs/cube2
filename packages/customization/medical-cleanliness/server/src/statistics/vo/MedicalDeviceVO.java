package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  13:34
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalDeviceVO {
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 设备品类
     */
    private String deviceProdCategoryName;
    /**
     * 总数
     */
    private Integer totalNumber;
    /**
     * 在线数量
     */
    private Integer normalNumber;
    /**
     * 离线数量
     */
    private Integer offlineNumber;
    /**
     * 报警次数
     */
    private Long alarmNumber;
    /**
     * 日期
     */
    private LocalDateTime createTime;
}
