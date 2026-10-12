package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 巡检任务总数统计
 * @date 2024/1/23 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InspectionTaskStatisticsVO {
    /**
     * 巡检任务总数
     */
    private Integer inspectionTaskNum;
    /**
     * 待接收任务数
     */
    private Long waitAcceptNum;
    /**
     * 进行中任务数
     */
    private Long underwayTaskNum;
}
