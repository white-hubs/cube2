package com.guhecloud.cube.biz.door.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 海康电子巡更统计图
 * @date 2024/3/15 15:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatrolListStatisticsVO {
    /**
     * 统计项
     */
    private String key;
    /**
     * 统计值
     */
    private Long value;
}
