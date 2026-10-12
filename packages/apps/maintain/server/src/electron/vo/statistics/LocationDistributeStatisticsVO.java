package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wql
 * @describe 巡更点分布情况
 * @date 2024/1/24 19:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDistributeStatisticsVO {
    /**
     * 巡更点总数
     */
    private Long total;
    /**
     * 巡更点分布区域
     */
    private List<LocationAreaStatisticsVO> locationAreaList;
}
