package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡更统计返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrolStatisticsVO {
    /**
     * 错误
     */
    private Integer errCount;
    /**
     * 待确认
     */
    private Integer waitCount;
    /**
     * 进行中
     */
    private Integer executionCount;
    /**
     * 已处理
     */
    private Integer finishedCount;
    /**
     * 总
     */
    private Integer totalCount;
}
