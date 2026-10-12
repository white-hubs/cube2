package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsPageQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolCurrencyStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolGrowthRateStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineAbnormalStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineStatisticsPageVO;

import java.util.List;

/**
 * 视频线路统计
 *
 * @author lanchun
 * @date 2022/11/9 11:25
 */
public interface CameraPatrolLineStatisticsService {

    /**
     * 视频线路统计分页列表
     *
     * @param param
     *
     * @return
     */
    Page<CameraPatrolLineStatisticsPageVO> getPatrolLineStatisticsPage(CameraPatrolLineStatisticsPageQueryParam param);

    /**
     * 视频线路统计列表
     *
     * @param param
     *
     * @return
     */
    List<CameraPatrolLineStatisticsPageVO> getPatrolLineStatisticsList(CameraPatrolLineStatisticsPageQueryParam param);

    /**
     * 巡逻异常数增长率统计
     *
     * @param param
     *
     * @return
     */
    CameraPatrolGrowthRateStatisticsVO getPatrolAbnormalRatioStatistics(CameraPatrolLineStatisticsQueryParam param);

    /**
     * 巡逻次数增长率统计
     *
     * @param param
     *
     * @return
     */
    CameraPatrolGrowthRateStatisticsVO getPatrolRecordRatioStatistics(CameraPatrolLineStatisticsQueryParam param);

    /**
     * 巡检线路异常统计
     *
     * @param param
     *
     * @return
     */
    List<CameraPatrolCurrencyStatisticsVO> getPatrolLineAbnormalInnerGroupStatistics(CameraPatrolLineStatisticsQueryParam param);

    /**
     * 巡逻异常情况
     *
     * @param param
     *
     * @return
     */
    CameraPatrolLineAbnormalStatisticsVO getCameraPatrolLineAbnormalStatistics(CameraPatrolLineStatisticsQueryParam param);
}
