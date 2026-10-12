package com.guhecloud.cube.biz.work.center.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作业务类型统计返回参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkBusinessTypeStatisticsVO {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 数量
     */
    private Long total;
}
