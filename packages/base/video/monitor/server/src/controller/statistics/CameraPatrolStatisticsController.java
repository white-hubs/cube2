package com.guhecloud.cube.biz.vmonitor.server.controller.statistics;

import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolLineStatisticsParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTopStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTrendStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolStatisticsService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPersonPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolTrendStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 监控巡逻可视乎接口
 *
 * @author lanchun
 * @date 2023/3/24 10:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolStatisticsController {

    private final CameraPatrolStatisticsService cameraPatrolBimStatisticsService;

    /**
     * 巡逻异常数统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-bim-statistics/record-abnormal")
    public CameraPatrolRecordStatisticsVO getCameraPatrolRecordAbnormalStatistics(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolRecordAbnormalStatistics(param);
    }

    /**
     * 视频巡逻统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-statistics")
    public CameraPatrolStatisticsVO getCameraPatrolStatistics(@Valid CameraPatrolLineStatisticsParam param) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolStatistics(param);
    }

    /**
     * 巡逻异常趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-statistics/abnormal-trend")
    public List<CameraPatrolTrendStatisticsVO> getCameraPatrolAbnormalTrendStatistics(CameraPatrolTrendStatisticsQueryParam param) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolAbnormalTrendStatistics(param);
    }

    /**
     * 巡逻点位排行top统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-statistics/point-top")
    public List<CameraPatrolPointStatisticsVO> getCameraPatrolPointTop(CameraPatrolTopStatisticsQueryParam param) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolPointTop(param);
    }

    /**
     * 巡逻时间分布统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-statistics/time-distribution")
    public List<CameraPatrolTrendStatisticsVO> getCameraPatrolTimeDistribution(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolTimeDistribution(param);
    }

    /**
     * 巡逻人员点位统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-statistics/person/camera-point")
    public List<CameraPatrolPersonPointStatisticsVO> getCameraPatrolPersonPointStatistics(@Valid CameraPatrolLineStatisticsQueryParam param
                                                                                         ) {
        return this.cameraPatrolBimStatisticsService.getCameraPatrolPersonPointStatistics(param);
    }
}
