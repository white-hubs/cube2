package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.Data;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/29 16:11
 */
@Data
public class DeviceOperationStatisticsDTO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 工单数量
     */
    private Integer num;

    /**
     * 任务数量
     */
    private Integer taskNum;

    /**
     * 异常数量
     */
    private Integer abnormalNum;
}
