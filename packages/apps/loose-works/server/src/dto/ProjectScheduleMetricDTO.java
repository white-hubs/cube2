package com.guhecloud.cube.srv.gen.loose.works.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 10:58
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleMetricDTO {

    /**
     * 工程总数
     */
    private Integer totalNumber;

    /**
     * 数量
     */
    private Long number;

    /**
     * 比率
     */
    private Float rate;

    /**
     * 平均处理时间
     */
    private Long avgTime;
}
