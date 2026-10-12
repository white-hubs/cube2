package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces;

import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.TrafficDetectionAreaExportService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.TrafficDetectionAreaStatService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaStatQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaListStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonFlowRankVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonTrendStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionDeviceStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowDetailVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowSummaryVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowTrendVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonTotalStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionTrendStatVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionAreaStatController {

    private final TrafficDetectionAreaStatService trafficDetectionAreaStatService;

    private final TrafficDetectionAreaExportService trafficDetectionAreaExportService;

    /**
     * 人流量数
     *
     * @param param 查询参数
     *
     * @return 人流量数
     */
    @GetMapping("/traffic-detection-areas/statistics")
    public TrafficDetectionPersonTotalStatVO getPersonTotalStat(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.queryPersonTotalStat(param);
    }

    /**
     * 各区域人流量
     *
     * @param param 查询参数
     *
     * @return 区域人流量列表
     */
    @GetMapping("/traffic-detection-areas/statistics/areas")
    public List<TrafficDetectionAreaListStatVO> getAreaPersonTotalList(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getAreaPersonTotalList(param);
    }

    /**
     * 人流量趋势
     *
     * @param param 查询参数
     *
     * @return 人流量趋势列表
     */
    @GetMapping("/traffic-detection-areas/statistics/trend")
    public List<TrafficDetectionTrendStatVO> getPersonTotalTrend(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getTrafficDetectionTrend(param);
    }

    /**
     * 各区域人流量趋势（含区域基础信息与各时间点统计）
     *
     * @param param 查询参数（时间维度、区域筛选等与全量趋势接口一致）
     *
     * @return 按区域分组的时间序列
     */
    @GetMapping("/traffic-detection-areas/statistics/areas/trend")
    public List<TrafficDetectionAreaPersonTrendStatVO> getAreaPersonTotalTrend(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getTrafficDetectionAreaPersonTrend(param);
    }

    /**
     * 各区域人流量趋势导出
     *
     * @param param    查询参数
     * @param response 响应
     */
    @GetMapping("/traffic-detection-areas/statistics/areas/trend/export")
    public void exportAreaPersonTotalTrend(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        trafficDetectionAreaExportService.exportTrafficDetectionAreaPersonTrend(param, response);
    }

    /**
     * 流量检测设备统计列表
     *
     * @param param 查询参数
     *
     * @return 流量检测设备统计列表
     */
    @GetMapping("/traffic-detection-areas/statistics/devices")
    public List<TrafficDetectionDeviceStatVO> getDeviceStatList(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getDeviceStatList(param);
    }

    /**
     * 人流统计汇总
     *
     * @param param 查询参数
     *
     * @return 人流统计汇总
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/summary")
    public TrafficDetectionPersonFlowSummaryVO getPersonFlowSummary(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.queryPersonFlowSummary(param);
    }

    /**
     * 累计人次趋势
     *
     * @param param 查询参数
     *
     * @return 累计人次趋势
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/trend")
    public List<TrafficDetectionPersonFlowTrendVO> getPersonFlowTrend(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getPersonFlowTrend(param);
    }

    /**
     * 人流详情趋势（按 10 分钟求和）
     *
     * @param param 查询参数
     *
     * @return 人流详情趋势
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/detail-trend")
    public List<TrafficDetectionPersonFlowTrendVO> getPersonFlowDetailTrend(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getPersonFlowDetailTrend(param);
    }

    /**
     * 人流详情列表
     *
     * @param param 查询参数
     *
     * @return 人流详情列表
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/details")
    public Pagination<TrafficDetectionPersonFlowDetailVO> listPersonFlowDetail(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.pagePersonFlowDetail(param);
    }

    /**
     * 人流详情列表导出
     *
     * @param param    查询参数
     * @param response 响应
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/details/export")
    public void exportPersonFlowDetail(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        trafficDetectionAreaExportService.exportTrafficDetectionPersonFlowDetail(param, response);
    }

    /**
     * 区域人流排行
     *
     * @param param 查询参数
     *
     * @return 区域人流排行
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/area-rank")
    public Pagination<TrafficDetectionAreaPersonFlowRankVO> getAreaPersonFlowRank(TrafficDetectionAreaStatQueryParam param) {
        return trafficDetectionAreaStatService.getAreaPersonFlowRank(param);
    }

    /**
     * 区域人流排行导出
     *
     * @param param    查询参数
     * @param response 响应
     */
    @GetMapping("/traffic-detection-areas/statistics/person-flow/area-rank/export")
    public void exportAreaPersonFlowRank(TrafficDetectionAreaStatQueryParam param, HttpServletResponse response) {
        trafficDetectionAreaExportService.exportTrafficDetectionAreaPersonFlowRank(param, response);
    }
}
