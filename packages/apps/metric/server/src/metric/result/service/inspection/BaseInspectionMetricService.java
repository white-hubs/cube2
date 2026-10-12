package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.client.InspectRecordClient;
import com.guhecloud.cube.srv.gen.maintain.client.InspectTaskClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordUserNumVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionCommonParam;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 18:52
 * @Description:
 */
@Slf4j
@Service
public class BaseInspectionMetricService {
    private final InspectRecordClient inspectRecordClient;
    private final InspectTaskClient inspectTaskClient;

    public BaseInspectionMetricService(InspectRecordClient inspectRecordClient, InspectTaskClient inspectTaskClient) {
        this.inspectRecordClient = inspectRecordClient;
        this.inspectTaskClient = inspectTaskClient;
    }

    /**
     * 日常巡检总任务数
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_NUMBER", description = "日常巡检总任务数")
    public Long inspectTaskNumber(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordCompleteRate(queryParam);
        return dto.getTotal();
    }

    /**
     * 日常巡检任务完成数
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_NUMBER", description = "日常巡检任务完成数")
    public Long inspectTaskCompleteNumber(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordCompleteRate(queryParam);
        return dto.getNumber();
    }

    /**
     * 日常巡检任务完成率
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_RATE", description = "日常巡检任务完成率")
    public BigDecimal inspectTaskCompleteRate(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordCompleteRate(queryParam);
        return dto.getRate();
    }

    /**
     * 日常巡检异常率
     */
    @MetricTopic(topic = "BASE_INSPECT_RECORD_ERROR_RATE", description = "日常巡检异常率")
    public BigDecimal inspectionRecordErrorRate(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordErrRate(queryParam);
        return dto.getRate();
    }

    /**
     * 日常巡检异常数
     */
    @MetricTopic(topic = "BASE_INSPECT_RECORD_ERROR_NUM", description = "日常巡检异常数")
    public Long inspectionRecordErrorNum(MetricParam param) {
        InspectionCommonParam queryParam = new InspectionCommonParam();
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin()) + "," + DateUtils.format(param.getPeriod().getEnd()));
        }
        TotalNumberRateDTO dto = inspectRecordClient.getRecordErrRate(queryParam);
        return dto.getNumber();
    }

    /**
     * 日常巡检处理率最高的五个部门
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_RATE_TOP_DEPT", description = "日常巡检处理率最高的五个部门")
    public List<HandlerRateDTO> inspectTaskCompleteRateTopDept(MetricParam param) {
        HandlerRateMetricDTO inspectTaskCompleteRateMetric = inspectTaskClient.getInspectTaskCompleteRateMetric(param.getPeriod());
        return inspectTaskCompleteRateMetric.getTopHandlerRateDept();
    }

    /**
     * 日常巡检处理率最低的五个部门
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_RATE_BOTTOM_DEPT", description = "日常巡检处理率最低的五个部门")
    public List<HandlerRateDTO> inspectTaskCompleteRateBottomDept(MetricParam param) {
        HandlerRateMetricDTO inspectTaskCompleteRateMetric = inspectTaskClient.getInspectTaskCompleteRateMetric(param.getPeriod());
        return inspectTaskCompleteRateMetric.getBottomHandlerRateDept();
    }

    /**
     * 日常巡检处理率最高的五个人
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_RATE_TOP_USER", description = "日常巡检处理率最高的五个人")
    public List<HandlerRateDTO> inspectTaskCompleteRateTopUser(MetricParam param) {
        HandlerRateMetricDTO inspectTaskCompleteRateMetric = inspectTaskClient.getInspectTaskCompleteRateMetric(param.getPeriod());
        return inspectTaskCompleteRateMetric.getTopHandlerRateUser();
    }

    /**
     * 日常巡检处理率最低的五个人
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_COMPLETE_RATE_BOTTOM_USER", description = "日常巡检处理率最低的五个人")
    public List<HandlerRateDTO> inspectTaskCompleteRateBottomUser(MetricParam param) {
        HandlerRateMetricDTO inspectTaskCompleteRateMetric = inspectTaskClient.getInspectTaskCompleteRateMetric(param.getPeriod());
        return inspectTaskCompleteRateMetric.getBottomHandlerRateUser();
    }

    /**
     * 日常巡检总处理时长
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_HANDLER_TIME", description = "日常巡检总处理时长")
    public Long inspectTaskHandlerTime(MetricParam param) {
        TaskHandlerTimeVO timeNumMetric = inspectTaskClient.getInspectTaskTimeCompleteTimeNumMetric(param.getPeriod());
        return timeNumMetric.getTaskTotalTime();
    }

    /**
     * 日常巡检处理效率最高的五个人
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_HANDLER_EFFICIENCY_TOP_USER", description = "日常巡检处理效率最高的五个人")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyTopUser(MetricParam param) {
        HandlerEfficiencyMetricDTO efficiencyMetric = inspectTaskClient.getInspectTaskCompleteEfficiencyMetric(param.getPeriod());
        return efficiencyMetric.getTopHandlerEfficiencyUser();
    }

    /**
     * 日常巡检处理效率最低的五个人
     */
    @MetricTopic(topic = "BASE_INSPECT_TASK_HANDLER_EFFICIENCY_BOTTOM_USER", description = "日常巡检处理效率最低的五个人")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyBottomUser(MetricParam param) {
        HandlerEfficiencyMetricDTO efficiencyMetric = inspectTaskClient.getInspectTaskCompleteEfficiencyMetric(param.getPeriod());
        return efficiencyMetric.getBottomHandlerEfficiencyUser();
    }

    /**
     * 日常巡检次数
     */
    @MetricTopic(topic = "BASE_INSPECT_RECORD_NUM_METRIC", description = "日常巡检次数")
    public Integer inspectionRecordNumMetric(MetricParam param) {
        InspectionRecordNumMetricDTO recordUserNumMetric = inspectRecordClient.getRecordUserNumMetric(param.getPeriod());
        return recordUserNumMetric.getInspectionRecordNum();
    }

    /**
     * 日常巡检处理数最高的五个人
     */
    @MetricTopic(topic = "BASE_INSPECT_RECORD_HANDLER_NUM_TOP_USER", description = "日常巡检处理数最高的五个人")
    public List<InspectionRecordUserNumVO> inspectionRecordHandlerNumTopUser(MetricParam param) {
        InspectionRecordNumMetricDTO recordUserNumMetric = inspectRecordClient.getRecordUserNumMetric(param.getPeriod());
        return recordUserNumMetric.getTopInspectUser();
    }

    /**
     * 日常巡检处理数最低的五个人
     */

    @MetricTopic(topic = "BASE_INSPECT_RECORD_HANDLER_NUM_BOTTOM_USER", description = "日常巡检处理数最低的五个人")
    public List<InspectionRecordUserNumVO> inspectionRecordHandlerNumBottomUser(MetricParam param) {
        InspectionRecordNumMetricDTO recordUserNumMetric = inspectRecordClient.getRecordUserNumMetric(param.getPeriod());
        return recordUserNumMetric.getBottomInspectUser();
    }

}
