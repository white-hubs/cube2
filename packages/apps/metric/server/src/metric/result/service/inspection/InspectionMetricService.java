package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectRecordItemStatisticsMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionHandlerEfficiencyRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionHandlerRateRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionTotalMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.client.InspectRecordClient;
import com.guhecloud.cube.srv.gen.maintain.client.InspectTaskClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.vo.InspectRecordItemStatisticsVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 巡检
 */
@Slf4j
@Service
public class InspectionMetricService {
    private final InspectRecordClient inspectRecordClient;
    private final InspectTaskClient inspectTaskClient;

    public InspectionMetricService(final InspectRecordClient inspectRecordClient, final InspectTaskClient inspectTaskClient) {
        this.inspectRecordClient = inspectRecordClient;
        this.inspectTaskClient = inspectTaskClient;
    }

    /**
     * 日常巡检异常率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "INSPECT_ERROR_RATE", description = "日常巡检异常率指标")
    public InspectionErrorRateMetricDTO inspectionErrorRate(InspectionMetricParam param) {
        TotalNumberRateDTO dto = inspectRecordClient.getRecordErrRate(convertQueryParam(param));

        return InspectionErrorRateMetricDTO.builder().inspectionTotal(dto.getTotal()).inspectionAbnormalNumber(dto.getNumber())
                .inspectionAbnormalRate(dto.getRate()).build();
    }

    /**
     * 日常巡检完成率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "INSPECT_COMPLETE_RATE", description = "日常巡检完成率指标")
    public InspectionCompleteRateMetricDTO inspectionCompleteRate(InspectionMetricParam param) {
        TotalNumberRateDTO dto = inspectRecordClient.getRecordCompleteRate(convertQueryParam(param));

        return InspectionCompleteRateMetricDTO.builder().inspectTotal(dto.getTotal()).inspectionCompleteNumber(dto.getNumber())
                .inspectCompleteRate(dto.getRate()).build();
    }

    /**
     * 日常巡检总数指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "INSPECT_TASK_TOTAL", description = "日常巡检总数指标")
    public InspectionTotalMetricDTO inspectionTaskTotal(InspectionMetricParam param) {
        Long total = inspectTaskClient.getTaskTotal(convertQueryParam(param));
        return InspectionTotalMetricDTO.builder().inspectionTotal(total).build();
    }

    private static InspectionCommonParam convertQueryParam(InspectionMetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        queryParam.setBusinessType(param.getBusinessType());
        queryParam.setAreaName(param.getAreaName());
        queryParam.setObjectType(param.getAreaName());
        return queryParam;
    }

    /**
     * 巡检项统计数量指标
     *
     * @param param 时间条件
     *
     * @return 列表
     */
    @MetricTopic(topic = "INSPECT_RECORD_ITEMS_STATISTICS_METRIC", description = "巡检项统计数量指标")
    public List<InspectRecordItemStatisticsMetricDTO> getInspectionItemsStatisticsMetric(InspectionMetricParam param) {
        InspectRecordItemQueryParam recordItemQueryParam = new InspectRecordItemQueryParam();
        recordItemQueryParam.setPeriod(param.getPeriod());
        recordItemQueryParam.setItemErrorValueFlag(param.getItemErrorValueFlag());
        List<InspectRecordItemStatisticsVO> inspectRecordItems = this.inspectRecordClient.getInspectRecordItemsStatistics(recordItemQueryParam);
        return inspectRecordItems.stream()
                .map(item ->
                             ObjectUtils.copying(item, InspectRecordItemStatisticsMetricDTO.class)
                    )
                .collect(Collectors.toList());
    }

    /**
     * 日常巡检处理率指标
     */
    @MetricTopic(topic = "INSPECT_HANDLE_RATE_RANK_METRIC", description = "日常巡检处理率指标")
    public InspectionHandlerRateRankMetricDTO inspectionHandleRate(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordCompleteRate(queryParam);
        HandlerRateMetricDTO inspectTaskCompleteRateMetric = inspectTaskClient.getInspectTaskCompleteRateMetric(param.getPeriod());
        return InspectionHandlerRateRankMetricDTO.builder()
                .inspectTaskTotal(dto.getTotal())
                .inspectTaskHandlerNumber(dto.getNumber())
                .inspectTaskHandledRate(dto.getRate())
                .topHandlerRateDept(inspectTaskCompleteRateMetric.getTopHandlerRateDept())
                .bottomHandlerRateDept(inspectTaskCompleteRateMetric.getBottomHandlerRateDept())
                .topHandlerRateUser(inspectTaskCompleteRateMetric.getTopHandlerRateUser())
                .bottomHandlerRateUser(inspectTaskCompleteRateMetric.getBottomHandlerRateUser())
                .build();
    }

    /**
     * 日常巡检处理效率指标
     */
    @MetricTopic(topic = "INSPECT_HANDLE_EFFICIENCY_RANK_METRIC", description = "日常巡检处理效率指标")
    public InspectionHandlerEfficiencyRankMetricDTO inspectionHandleEfficiencyRankMetric(MetricParam param) {
        TaskHandlerTimeVO timeNumMetric = inspectTaskClient.getInspectTaskTimeCompleteTimeNumMetric(param.getPeriod());
        HandlerEfficiencyMetricDTO efficiencyMetric = inspectTaskClient.getInspectTaskCompleteEfficiencyMetric(param.getPeriod());
        double avgHandlerTime = 0;
        if (Objects.nonNull(timeNumMetric) && 0 != timeNumMetric.getTaskHandlerNumber()) {
            avgHandlerTime = BigDecimal.valueOf(timeNumMetric.getTaskTotalTime())
                    .divide(BigDecimal.valueOf(timeNumMetric.getTaskHandlerNumber()), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return InspectionHandlerEfficiencyRankMetricDTO.builder()
                .inspectTaskHandlerNumber(timeNumMetric.getTaskHandlerNumber())
                .inspectTaskHandlerTime(timeNumMetric.getTaskTotalTime())
                .avgHandlerTime(avgHandlerTime)
                .topHandlerRateUser(efficiencyMetric.getTopHandlerEfficiencyUser())
                .bottomHandlerRateUser(efficiencyMetric.getBottomHandlerEfficiencyUser())
                .build();
    }

    /**
     * 日常巡检次数指标
     */
    @MetricTopic(topic = "INSPECT_RECORD_NUM_METRIC", description = "日常巡检次数指标")
    public InspectionRecordNumMetricDTO inspectionRecordNumMetric(MetricParam param) {
        return inspectRecordClient.getRecordUserNumMetric(param.getPeriod());
    }

    public InspectionRateMetricDTO getAllMetric(InspectionMetricParam param) {
        TotalNumberRateDTO errRate = inspectRecordClient.getRecordErrRate(convertQueryParam(param));
        TotalNumberRateDTO completeRate = inspectRecordClient.getRecordCompleteRate(convertQueryParam(param));
        return InspectionRateMetricDTO.builder()
                .inspectionTotal(completeRate.getTotal())
                .inspectionCompleteNumber(completeRate.getNumber())
                .inspectCompleteRate(completeRate.getRate())
                .inspectionAbnormalNumber(errRate.getNumber())
                .inspectionAbnormalRate(errRate.getRate())
                .build();
    }
}
