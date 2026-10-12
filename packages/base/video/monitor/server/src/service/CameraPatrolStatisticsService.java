package com.guhecloud.cube.biz.vmonitor.server.service;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolLineStatisticsParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTopStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTrendStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPersonPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolTrendStatisticsVO;

import java.util.List;

public interface CameraPatrolStatisticsService {

    CameraPatrolRecordStatisticsVO getCameraPatrolRecordAbnormalStatistics(CameraPatrolLineStatisticsQueryParam param);

    CameraPatrolStatisticsVO getCameraPatrolStatistics(CameraPatrolLineStatisticsParam param);

    List<CameraPatrolTrendStatisticsVO> getCameraPatrolAbnormalTrendStatistics(CameraPatrolTrendStatisticsQueryParam param);

    List<CameraPatrolPointStatisticsVO> getCameraPatrolPointTop(CameraPatrolTopStatisticsQueryParam param);

    List<CameraPatrolTrendStatisticsVO> getCameraPatrolTimeDistribution(CameraPatrolLineStatisticsQueryParam param);

    List<CameraPatrolPersonPointStatisticsVO> getCameraPatrolPersonPointStatistics(CameraPatrolLineStatisticsQueryParam param);
}
