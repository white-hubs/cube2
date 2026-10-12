package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 小散工程趋势统计
 * @date 2024/1/24 15:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectTrendStatisticsVO {
    /**
     * 月份
     */
    private Integer month;
    /**
     * 数量
     */
    private Long num;

    /**
     * 时间
     */
    private String period;

}
