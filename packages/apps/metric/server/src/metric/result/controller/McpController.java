package com.guhecloud.cube.biz.metric.server.metric.result.controller;

import com.guhecloud.cube.biz.metric.server.metric.result.controller.dto.AlarmCommonMcpDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.InspectionMetricEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.PatrolTaskMetricEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.DeviceMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ProcessEngineMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpAlarmQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpCommonInspectQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpDeviceQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpElectronQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpFaultOrderQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpInspectionQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpLooseWorksQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpProcessEngineQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.AlarmMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmAreaNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmImpracticalRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmUserHandlerEfficiencyTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmUserHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.DeviceMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceOnlineRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.ProCategoryDeviceCountMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.ElectronMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronLocationStatisticsMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.flow.ProcessEngineMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.flow.dto.WorkOrderNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.InspectionMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectRecordItemStatisticsMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.LooseWorksMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.ProjectScheduleAllMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.client.HiddenDangerClient;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 标准
 */
@Slf4j
@RestController
public class McpController {

    private final AlarmMetricService alarmMetricService;

    private final DeviceMetricService deviceMetricService;

    private final ProcessEngineMetricService processEngineMetricService;

    private final ElectronMetricService electronMetricService;

    private final InspectionMetricService inspectionMetricService;

    private final LooseWorksMetricService looseWorksMetricService;

    private final HiddenDangerClient hiddenDangerClient;

    private final FaultClient faultClient;

    public McpController(AlarmMetricService alarmMetricService, DeviceMetricService deviceMetricService,
                         ProcessEngineMetricService processEngineMetricService, ElectronMetricService electronMetricService,
                         InspectionMetricService inspectionMetricService, LooseWorksMetricService looseWorksMetricService,
                         HiddenDangerClient hiddenDangerClient,
                         FaultClient faultClient
                        ) {
        this.alarmMetricService = alarmMetricService;
        this.deviceMetricService = deviceMetricService;
        this.processEngineMetricService = processEngineMetricService;
        this.electronMetricService = electronMetricService;
        this.inspectionMetricService = inspectionMetricService;
        this.looseWorksMetricService = looseWorksMetricService;
        this.hiddenDangerClient = hiddenDangerClient;
        this.faultClient = faultClient;
    }

    /**
     * 设备数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/device/number")
    public DeviceOnlineRateMetricDTO getDeviceNumber(McpDeviceQueryParam param) {
        return deviceMetricService.getDeviceOnlineRateMetric(convertDeviceMetricParam(param));
    }

    /**
     * 按品类统计设备数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/device/categories/number")
    public List<ProCategoryDeviceCountMetricDTO> getProCategoryDeviceCount(McpDeviceQueryParam param) {
        return deviceMetricService.getProCategoryDeviceCountMetric(convertDeviceMetricParam(param));
    }

    /**
     * 报警数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/number")
    public AlarmNumberMetricDTO getAlarmNumber(McpAlarmQueryParam param) {
        return this.alarmMetricService.getAlarmNumberMetric(convertAlarmMetricParam(param));
    }

    /**
     * 报警实报误报统计
     *
     * @return
     */
    @GetMapping("/mcp/alarm/handler-result")
    public AlarmImpracticalRateMetricDTO getAlarmHandlerResult(McpAlarmQueryParam param) {
        return alarmMetricService.getAlarmImpracticalRateMetric(convertAlarmMetricParam(param));
    }

    /**
     * 报警按类型统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/event-type")
    public List<AlarmEventTypeNumberMetricDTO> getAlarmEventTypeAlarmNumber(McpAlarmQueryParam param) {
        return alarmMetricService.getAlarmEventTypeNumberMetric(convertAlarmMetricParam(param));
    }

    /**
     * 报警按级别数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/level-number")
    public List<AlarmLevelNumberMetricDTO> getLevelAlarmNumber(McpAlarmQueryParam param) {
        return alarmMetricService.getAlarmLevelStatistics(convertAlarmMetricParam(param));
    }

    /**
     * 用户报警处理数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/most-handler/user")
    public List<AlarmUserHandlerRateTopMetricDTO.UserDetails> getAlarmHandlerMostUser(McpAlarmQueryParam param) {
        return alarmMetricService.getUserHandlerRateMetric(convertAlarmMetricParam(param));
    }

    /**
     * 用户报警处理效率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/mini-handler/efficiency/user")
    public List<AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails> getAlarmHandlerEfficiencyMiniUser(McpAlarmQueryParam param) {
        return alarmMetricService.getUserHandlerEfficiencyMetric(convertAlarmMetricParam(param));
    }

    /**
     * 区域报警数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/most/area")
    public AlarmAreaNumberMetricDTO getMostAlarmUser(McpAlarmQueryParam param) {
        List<AlarmAreaNumberMetricDTO> areaAlarmNumberMetric = alarmMetricService.getAreaAlarmNumberMetric(convertAlarmMetricParam(param));
        if (CollectionUtils.isEmpty(areaAlarmNumberMetric)) {
            return null;
        }
        return areaAlarmNumberMetric.stream()
                .sorted(Comparator.comparing(AlarmAreaNumberMetricDTO::getAreaAlarmNumber).reversed())
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * 报警数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/alarm/common")
    public AlarmCommonMcpDTO getMcpAlarmCommon(McpAlarmQueryParam param) {
        AlarmMetricParam metricParam = convertAlarmMetricParam(param);
        return AlarmCommonMcpDTO.builder()
                .alarmNumber(alarmMetricService.getAlarmNumberMetric(metricParam).getAlarmNumber())
                .impracticalRate(alarmMetricService.getAlarmImpracticalRateMetric(metricParam))
                .eventTypeNumbers(alarmMetricService.getAlarmEventTypeNumberMetric(metricParam))
                .levelNumbers(alarmMetricService.getAlarmLevelStatistics(metricParam))
                .build();
    }

    /**
     * 工单按类型统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/process-engine/work-order/type/number")
    public List<WorkOrderNumberMetricDTO> getWorkOrderNumberMetric(McpProcessEngineQueryParam param) {
        return processEngineMetricService.getWorkOrderTypeNumberMetric(convertProcessEngineMetricParam(param));
    }

    /**
     * 巡更点巡更(巡查)记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/electron/locations/record/number")
    public List<ElectronLocationStatisticsMetricDTO> getElectronTaskLocationStatisticsMetric(McpElectronQueryParam param) {
        return electronMetricService.getElectronTaskLocationsStatisticsMetric(convertElectronMetricParam(param));
    }

    /**
     * 巡检(巡查)项纪录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/mcp/inspection/items/records/number")
    public List<InspectRecordItemStatisticsMetricDTO> getInspectionRecordItemsStatisticsMetric(McpInspectionQueryParam param) {
        return inspectionMetricService.getInspectionItemsStatisticsMetric(convertInspectionMetricParam(param));
    }

    /**
     * 小散工程工程巡检任务数统计
     */
    @GetMapping("/mcp/loose/works/inspect/number")
    public InspectionRateMetricDTO getInspectionRecordCommonStatisticsMetric(McpCommonInspectQueryParam param) {
        InspectionMetricParam metricParam = this.convertParam(param);
        metricParam.setBusinessType("SMALL_ENGINEERING");
        if (Objects.isNull(param.getType())) {
            return inspectionMetricService.getAllMetric(metricParam);
        } else {
            InspectionMetricEnum inspectionMetricEnum = param.getType();
            return inspectionMetricEnum.getMetric(metricParam);
        }
    }

    /**
     * 小散工程工程数统计
     */
    @GetMapping("/mcp/loose/works/common/metric")
    public ProjectScheduleAllMetricDTO getLooseWorkCommonNumberMetric(McpLooseWorksQueryParam param) {
        return looseWorksMetricService.getMetric(defautlPeriod(param.getBeginTime(), param.getEndTime()), param.getMetricType());
    }

    /**
     * 日常巡检巡检任务数统计
     */
    @GetMapping("/mcp/inspection/task/common/number")
    public InspectionRateMetricDTO getInspectionTakCommonNumberMetric(McpCommonInspectQueryParam param) {
        if (Objects.isNull(param.getType())) {
            return inspectionMetricService.getAllMetric(this.convertParam(param));
        } else {
            InspectionMetricEnum inspectionMetricEnum = param.getType();
            return inspectionMetricEnum.getMetric(this.convertParam(param));
        }
    }

    /**
     * 隐患整改完成数统计
     */
    @GetMapping("/mcp/hidden/danger/common/metric")
    public HiddenDangerNumStatisticsVO getHiddenDangerCommonNumberMetric(McpQueryParam param) {
        return hiddenDangerClient.hiddenDangerNumStatistics(defautlPeriod(param.getBeginTime(), param.getEndTime()));
    }

    /**
     * 故障报修按故障对象统计完成率
     */
    @GetMapping("/mcp/fault/rate/common/metric")
    public FaultCompletedMetricVO getFaultOrderCommonNumberMetric(McpFaultOrderQueryParam param) {
        return faultClient.faultCompletedMetic(defautlPeriod(param.getBeginTime(), param.getEndTime()),
                                               Objects.nonNull(param.getFaultType()) ? param.getFaultType().name() : null);
    }

    /**
     * 电子巡更按巡更点统计巡更任务
     */
    @GetMapping("/mcp/electron/common/metric")
    public PatrolTaskRateMetricDTO getElectronCommonNumberMetric(McpElectronQueryParam param) {
        if (Objects.isNull(param.getTaskMetricType())) {
            return electronMetricService.getAllMetric(convertElectronMetricParam(param));
        } else {
            PatrolTaskMetricEnum electronMetricEnum = param.getTaskMetricType();
            return electronMetricEnum.getMetric(convertElectronMetricParam(param));
        }
    }

    public InspectionMetricParam convertParam(McpCommonInspectQueryParam param) {
        InspectionMetricParam queryParam = new InspectionMetricParam();
        queryParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        queryParam.setAreaName(param.getAreaName());
        queryParam.setObjectType(Objects.nonNull(param.getObjectType()) ? param.getObjectType().name() : null);
        return queryParam;
    }

    public static Period defautlPeriod(LocalDateTime beginTime, LocalDateTime endTime) {
        Period period;
        if (Objects.nonNull(beginTime) && Objects.nonNull(endTime)) {
            period = new Period(beginTime, endTime);
        } else {
            var now = LocalDateTime.now();
            period = new Period(DateUtils.beginOfMonth(now), now);
        }
        return period;
    }

    private ElectronMetricParam convertElectronMetricParam(McpElectronQueryParam param) {
        ElectronMetricParam metricParam = new ElectronMetricParam();
        metricParam.setResult(param.getResult());
        metricParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        metricParam.setAreaName(param.getAreaName());
        metricParam.setPointName(param.getPointName());
        return metricParam;
    }

    private InspectionMetricParam convertInspectionMetricParam(McpInspectionQueryParam param) {
        InspectionMetricParam metricParam = new InspectionMetricParam();
        metricParam.setItemErrorValueFlag(param.getItemErrorValueFlag());
        metricParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        return metricParam;
    }

    private DeviceMetricParam convertDeviceMetricParam(McpDeviceQueryParam param) {
        DeviceMetricParam metricParam = new DeviceMetricParam();
        metricParam.setAppCodes(param.getAppCodes());
        if (Objects.nonNull(param.getBeginTime()) && Objects.nonNull(param.getEndTime())) {
            metricParam.setPeriod(new Period(param.getBeginTime(), param.getEndTime()));
        }
        return metricParam;
    }

    private AlarmMetricParam convertAlarmMetricParam(McpAlarmQueryParam param) {
        AlarmMetricParam alarmMetricParam = new AlarmMetricParam();
        if (Objects.nonNull(param.getBeginTime()) && Objects.nonNull(param.getEndTime())) {
            alarmMetricParam.setPeriod(new Period(param.getBeginTime(), param.getEndTime()));
        }
        alarmMetricParam.setAppCodes(param.getAppCodes());
        alarmMetricParam.setEventTypes(param.getEventTypes());
        return alarmMetricParam;
    }

    private ProcessEngineMetricParam convertProcessEngineMetricParam(McpProcessEngineQueryParam param) {
        ProcessEngineMetricParam metricParam = new ProcessEngineMetricParam();
        if (Objects.nonNull(param.getBeginTime()) && Objects.nonNull(param.getEndTime())) {
            metricParam.setPeriod(new Period(param.getBeginTime(), param.getEndTime()));
        }
        metricParam.setBusinessTypes(param.getBusinessTypes());
        return metricParam;
    }
}
