package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 巡逻路线异常统计返回对象
 *
 * @author lanchun
 * @date 2022/11/9 14:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolLineAbnormalStatisticsVO {

    /**
     * 线路总数
     */
    private Integer countNum;

    /**
     * 异常数最多的线路名称
     */
    private String mostAbnormalPatrolName;

    /**
     * 最多异常数的线路异常数量
     */
    private Integer mostAbnormalPatrolNum;

    /**
     * 线路分组异常数量集合
     */
    private List<CameraPatrolCurrencyStatisticsVO> cameraPatrolLineAbnormalStatisticsVOS;
}
