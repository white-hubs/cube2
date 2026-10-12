package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡检任务耗时信息
 * @date 2023/4/18 15:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InspectionTaskTimeVO {
    /**
     * 巡检类型
     */
    private String inspectionType;
    /**
     * 巡检类型id
     */
    private Long typeId;
    /**
     * 任务耗时
     */
    private Long taskTime;
    /**
     * 任务耗时-小时
     */
    private Double taskTimeHour;
    /**
     * 耗时环比
     */
    private String taskTimeRatio;

}
