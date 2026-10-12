package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 部门巡检统计
 * @date 2024/1/24 10:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaskDepStatistics {
    /**
     * 部门名称
     */
    private String depName;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 任务数量
     */
    private Long taskNum;
    /**
     * 环比
     */
    private Float chainRatio;
}
