package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 设备巡检统计信息
 * @date 2023/8/9 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DeviceInspectionVO {
    /**
     * 总任务数
     */
    private int totalTask;
    /**
     * 正常任务数
     */
    private int normalTask;
    /**
     * 异常任务数
     */
    private int abnormalTask;
}
