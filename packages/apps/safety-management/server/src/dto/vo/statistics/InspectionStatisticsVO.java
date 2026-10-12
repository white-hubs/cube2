package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全巡检统计
 * @date 2023/9/7 19:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InspectionStatisticsVO {
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
    /**
     * 临时巡检数
     */
    private Long tempInspectionNum;
    /**
     * 异常巡检任务数
     */
    private Long errorInspectionNum;

}
