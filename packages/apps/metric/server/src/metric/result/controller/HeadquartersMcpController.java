package com.guhecloud.cube.biz.metric.server.metric.result.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.HeadInspectionMetricEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.HeadPatrolTaskMetricEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.HeadFaultMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.HeadMcpElectronQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.HeadMcpInspectQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpAlarmQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpDeviceQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpFaultOrderQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpLooseWorksQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceOnlineRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.ProCategoryDeviceCountMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto.FaultOrderCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersAlarmMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersDeviceMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersLooseWorksMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersMaintainMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketAlarmNum;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketHandlerRate;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketTimeoutHandlerRate;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.ProjectScheduleAllMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.service.headquarters.dto.alarm.MarketHandlerRateLevel;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.controller.McpController.defautlPeriod;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.FAULT_REPAIR_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.HIDDEN_DANGER_NUMBER_REQUEST_URL;

/**
 * 集团
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/4 9:31
 * @Description:
 */
@Slf4j
@RestController
public class HeadquartersMcpController {

    private final HeadquartersLooseWorksMetricService headquartersLooseWorksMetricService;
    private final HeadquartersMaintainMetricService headquartersMaintainMetricService;
    private final HeadquartersAlarmMetricService headquartersAlarmMetricService;
    private final HeadquartersDeviceMetricService headquartersDeviceMetricService;

    public HeadquartersMcpController(HeadquartersLooseWorksMetricService headquartersLooseWorksMetricService,
                                     HeadquartersMaintainMetricService headquartersMaintainMetricService,
                                     HeadquartersAlarmMetricService headquartersAlarmMetricService,
                                     HeadquartersDeviceMetricService headquartersDeviceMetricService
                                    ) {
        this.headquartersLooseWorksMetricService = headquartersLooseWorksMetricService;
        this.headquartersMaintainMetricService = headquartersMaintainMetricService;
        this.headquartersAlarmMetricService = headquartersAlarmMetricService;
        this.headquartersDeviceMetricService = headquartersDeviceMetricService;
    }

    /**
     * 各市场设备统计
     */
    @GetMapping("/mcp/head/device/metric")
    public List<DeviceOnlineRateMetricDTO> getHeadMcpDeiceNumberMetric(McpDeviceQueryParam param) {
        return headquartersDeviceMetricService.getHeadMcpDeiceNumberMetric(param);
    }

    /**
     * 各市场设备类型统计
     */
    @GetMapping("/mcp/head/device/type/metric")
    public List<ProCategoryDeviceCountMetricDTO> getHeadMcpDeiceTypeNumberMetric(McpDeviceQueryParam param) {
        return headquartersDeviceMetricService.getHeadMcpDeiceTypeNumberMetric(param);
    }

    /**
     * 各市场报警数据量排名统计
     */
    @GetMapping("/mcp/head/alarm/number/rank/metric")
    public List<MarketAlarmNum> getAramNumberNumberMarketRank(McpAlarmQueryParam param) {
        return headquartersAlarmMetricService.getAlarmNumberRank(param);
    }

    /**
     * 各市场报警处理率排名统计
     */
    @GetMapping("/mcp/head/alarm/handler/rate/rank/metric")
    public List<MarketHandlerRate> getAlarmHandlerRateRankMetric(McpAlarmQueryParam param) {
        return headquartersAlarmMetricService.getAlarmHandlerRateMarketRank(param);
    }

    /**
     * 各市场处理效率排名统计
     */
    @GetMapping("/mcp/head/alarm/handler/efficiency/rank/metric")
    public List<MarketTimeoutHandlerRate> getAlarmHandlerEfficiencyRankMetric(McpAlarmQueryParam param) {
        return headquartersAlarmMetricService.getAlarmHandlerEfficiencyMarketRank(param);
    }

    /**
     * 各市场处理率最高的报警等级
     */
    @GetMapping("/mcp/head/alarm/handler/rate/top/metric")
    public List<MarketHandlerRateLevel> getAlarmHandlerRateTopMetric(McpAlarmQueryParam param) {
        return headquartersAlarmMetricService.getAlarmHandlerRateTopMarketRank(param);
    }

    /**
     * 小散工程工程巡检任务数统计
     */
    @GetMapping("/mcp/head/loose/works/inspect/number")
    public List<InspectionRateMetricDTO> getInspectionRecordCommonStatisticsMetric(HeadMcpInspectQueryParam param) {
        InspectionMetricParam queryParam = new InspectionMetricParam();
        queryParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        queryParam.setBusinessType("SMALL_ENGINEERING");
        if (Objects.isNull(param.getType())) {
            return headquartersMaintainMetricService.getAllMetric(queryParam);
        } else {
            HeadInspectionMetricEnum inspectionMetricEnum = param.getType();
            return inspectionMetricEnum.getMetric(queryParam);
        }
    }

    /**
     * 小散工程工程数统计
     */
    @GetMapping("/mcp/head/loose/works/common/metric")
    public List<ProjectScheduleAllMetricDTO> getLooseWorkCommonNumberMetric(McpLooseWorksQueryParam param) {
        return headquartersLooseWorksMetricService.getMetric(defautlPeriod(param.getBeginTime(), param.getEndTime()), param.getMetricType());
    }

    /**
     * 日常巡检巡检任务数统计
     */
    @GetMapping("/mcp/head/inspection/task/common/number")
    public List<InspectionRateMetricDTO> getInspectionTakCommonNumberMetric(HeadMcpInspectQueryParam param) {
        InspectionMetricParam queryParam = new InspectionMetricParam();
        queryParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        if (Objects.isNull(param.getType())) {
            return headquartersMaintainMetricService.getAllMetric(queryParam);
        } else {
            HeadInspectionMetricEnum inspectionMetricEnum = param.getType();
            return inspectionMetricEnum.getMetric(queryParam);
        }
    }

    /**
     * 隐患整改完成数统计
     */
    @GetMapping("/mcp/head/hidden/danger/common/metric")
    public List<HiddenDangerCompletedRateMetricDTO> getHiddenDangerCommonNumberMetric(McpQueryParam param) {
        MetricParam metricParam = new MetricParam();
        metricParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> allMarketHiddenMetrics = asyncHttpClientUtils.getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL,
                                                                                                                                HttpMethod.GET, null, param, new TypeReference<HiddenDangerNumStatisticsVO>() {});
        return allMarketHiddenMetrics.stream().map(t ->
                                                           HiddenDangerCompletedRateMetricDTO.builder()
                                                                   .marketName(t.getMarketName())
                                                                   .hiddenDangerNumber(t.getMetricStruct().getTotal())
                                                                   .hiddenDangerCompletedNumber(t.getMetricStruct().getFinished())
                                                                   .hiddenDangerCompletedRate(t.getMetricStruct().getCalculateProportion())
                                                                   .build())
                .collect(Collectors.toList());
    }

    /**
     * 故障报修按故障对象统计完成率
     */
    @GetMapping("/mcp/head/fault/rate/common/metric")
    public List<FaultOrderCompletedRateMetricDTO> getFaultOrderCommonNumberMetric(McpFaultOrderQueryParam param) {
        HeadFaultMetricParam metricParam = new HeadFaultMetricParam();
        metricParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        metricParam.setType(Objects.nonNull(param.getFaultType()) ? param.getFaultType().name() : null);

        AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
        List<MarketMetricResult<FaultCompletedMetricVO>> allMarketFaultMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});

        return allMarketFaultMetrics.stream().map(t ->
                                                          FaultOrderCompletedRateMetricDTO.builder()
                                                                  .marketName(t.getMarketName())
                                                                  .faultRepairNumber(t.getMetricStruct().getTotal())
                                                                  .faultRepairCompletedNumber(t.getMetricStruct().getCompleted())
                                                                  .faultRepairCompletedRate(t.getMetricStruct().getRate())
                                                                  .build())
                .collect(Collectors.toList());
    }

    /**
     * 电子巡更按巡更点统计巡更任务
     */
    @GetMapping("/mcp/head/electron/common/metric")
    public List<PatrolTaskRateMetricDTO> getElectronCommonNumberMetric(HeadMcpElectronQueryParam param) {
        if (Objects.isNull(param.getTaskMetricType())) {
            return headquartersMaintainMetricService.getAllPatrolMetric(convertElectronMetricParam(param));
        } else {
            HeadPatrolTaskMetricEnum electronMetricEnum = param.getTaskMetricType();
            return electronMetricEnum.getMetric(convertElectronMetricParam(param));
        }
    }

    private ElectronMetricParam convertElectronMetricParam(HeadMcpElectronQueryParam param) {
        ElectronMetricParam metricParam = new ElectronMetricParam();
        metricParam.setResult(param.getResult());
        metricParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        metricParam.setAreaName(param.getAreaName());
        metricParam.setPointName(param.getPointName());
        return metricParam;
    }
}
