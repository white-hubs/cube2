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
public class MedicalAlarmVO {
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 设备品类
     */
    private String deviceProdCategoryName;
    /**
     * 报警总数
     */
    private Long totalNum;
    /**
     * 待接收报警数
     */
    private Long unConfirmedNum;

    /**
     * 处理中报警数
     */
    private Long progressConfirmedNum;

    /**
     * 已挂起报警数
     */
    private Long suspendNum;

    /**
     * 已处理报警数
     */
    private Long handlerNum;
    /**
     * 日期
     */
    private LocalDateTime createTime;
}
