package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolLine;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsPageQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolLineStatisticsParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolCurrencyStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineStatisticsPageVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频巡逻路线mapper接口
 *
 * @author lanchun
 * @date 2021/12/6 19:53
 */
@Repository
public interface CameraPatrolLineMapper extends BaseMapper<CameraPatrolLine> {

    int updateLastPatrol(@Param("id") Long id, @Param("partolTime") LocalDateTime partolTime);

    Page<CameraPatrolLine> selectAuthCameraPatrolLinePage(Page page, @Param("param") CameraPatrolLineQueryParam param);

    Page<CameraPatrolLineStatisticsPageVO> selectPatrolLineStatisticsPage(Page page,
                                                                          @Param("param") CameraPatrolLineStatisticsPageQueryParam param
                                                                         );

    List<CameraPatrolLineStatisticsPageVO> selectPatrolLineStatisticsList(@Param("param") CameraPatrolLineStatisticsPageQueryParam param);

    List<CameraPatrolCurrencyStatisticsVO> getCameraPatrolLineAbnormalStatistics(@Param("param") CameraPatrolLineStatisticsQueryParam param
                                                                                );

    List<CameraPatrolCurrencyStatisticsVO> getCameraPatrolLineAbnormalInnerStatistics(
            @Param("param") CameraPatrolLineStatisticsQueryParam param
                                                                                     );

    Integer selectCameraPatrolLineCount(@Param("param") CameraPatrolLineStatisticsParam param);

    Integer selectCameraPatrolLineDevices(@Param("param") CameraPatrolLineStatisticsParam param);

}