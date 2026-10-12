package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkAbnormalRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkAcceptanceRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkReformEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkReformRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.ProjectScheduleAllMetricDTO;
import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleMetricVO;
import com.guhecloud.platform.base.model.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 10:34
 * @Description: 小散工程指标分析
 */
@Slf4j
@Service
public class LooseWorksMetricService {
    private final ProjectScheduleClient projectScheduleClient;

    public LooseWorksMetricService(ProjectScheduleClient projectScheduleClient) {
        this.projectScheduleClient = projectScheduleClient;
    }

    @MetricTopic(topic = "PROJECT_NUMBER", description = "小散工程数量指标")
    public LooseWorkNumberMetricDTO projectNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.COMPLETED_RATE);
        return LooseWorkNumberMetricDTO.builder()
                .looseWorkNumber(projectScheduleMetricVO.getNumber())
                .build();
    }

    /**
     * 小散工程完成率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "PROJECT_COMPLETED_RATE", description = "小散工程完成率指标")
    public LooseWorkCompletedRateMetricDTO projectCompletedRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.COMPLETED_RATE);
        return LooseWorkCompletedRateMetricDTO.builder()
                .looseWorkCompletedNumber(projectScheduleMetricVO.getNumber())
                .looseWorkCompletedRate(projectScheduleMetricVO.getRate())
                .build();
    }

    /**
     * 小散工程异常率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "PROJECT_ABNORMAL_RATE", description = "小散工程异常率指标")
    public LooseWorkAbnormalRateMetricDTO projectAbnormalRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ABNORMAL_RATE);
        return LooseWorkAbnormalRateMetricDTO.builder()
                .looseWorkAbnormalNumber(projectScheduleMetricVO.getNumber())
                .looseWorkAbnormalRate(projectScheduleMetricVO.getRate())
                .build();
    }

    /**
     * 小散工程整改率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "PROJECT_REFORM_RATE", description = "小散工程整改率指标")
    public LooseWorkReformRateMetricDTO projectReformRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.REFORM_RATE);
        return LooseWorkReformRateMetricDTO.builder()
                .looseWorkReformNumber(projectScheduleMetricVO.getNumber())
                .looseWorkReformRate(projectScheduleMetricVO.getRate())
                .build();
    }

    /**
     * 小散工程整改效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "PROJECT_REFORM_EFFICIENCY", description = "小散工程整改效率指标")
    public LooseWorkReformEfficiencyMetricDTO projectReformEfficiency(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.REFORM_EFFICIENCY);
        return LooseWorkReformEfficiencyMetricDTO.builder()
                .looseWorkReformEfficiency(projectScheduleMetricVO.getAvgTime().doubleValue())
                .build();
    }

    /**
     * 小散工程验收率
     */
    @MetricTopic(topic = "PROJECT_ACCEPTANCE_RATE", description = "小散工程验收率指标")
    public LooseWorkAcceptanceRateMetricDTO projectAcceptanceRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ACCEPTANCE_RATE);
        return LooseWorkAcceptanceRateMetricDTO.builder()
                .acceptanceNumber(projectScheduleMetricVO.getNumber())
                .acceptanceRate(projectScheduleMetricVO.getRate())
                .build();
    }

    public ProjectScheduleAllMetricDTO getMetric(Period period, MetricType metricType) {
        ProjectScheduleAllMetricDTO projectScheduleAllMetricDTO = new ProjectScheduleAllMetricDTO();
        MetricParam param = new MetricParam();
        param.setPeriod(period);
        if (Objects.isNull(metricType)) {
            for (MetricType value : MetricType.values()) {
                getMetricByType(value, param, projectScheduleAllMetricDTO);
            }
        } else {
            getMetricByType(metricType, param, projectScheduleAllMetricDTO);
        }
        return projectScheduleAllMetricDTO;
    }

    private void getMetricByType(MetricType value, MetricParam param, ProjectScheduleAllMetricDTO projectScheduleAllMetricDTO) {
        switch (value) {
            case TOTAL_COUNT:
                LooseWorkNumberMetricDTO metricDTO = projectNumber(param);
                projectScheduleAllMetricDTO.setProjectCount(metricDTO.getLooseWorkNumber());
                break;
            case COMPLETED_RATE:
                LooseWorkCompletedRateMetricDTO completedRateMetricDTO = projectCompletedRate(param);
                projectScheduleAllMetricDTO.setCompleteRate(completedRateMetricDTO.getLooseWorkCompletedRate());
                break;
            case ABNORMAL_RATE:
                LooseWorkAbnormalRateMetricDTO looseWorkAbnormalRateMetricDTO = projectAbnormalRate(param);
                projectScheduleAllMetricDTO.setAbnormalRate(looseWorkAbnormalRateMetricDTO.getLooseWorkAbnormalRate());
                break;
            case REFORM_RATE:
                LooseWorkReformRateMetricDTO looseWorkReformRateMetricDTO = projectReformRate(param);
                projectScheduleAllMetricDTO.setRectificationRate(looseWorkReformRateMetricDTO.getLooseWorkReformRate());
                break;
            case REFORM_EFFICIENCY:
                LooseWorkReformEfficiencyMetricDTO reformEfficiencyMetricDTO = projectReformEfficiency(param);
                projectScheduleAllMetricDTO.setRectificationEfficiency(reformEfficiencyMetricDTO.getLooseWorkReformEfficiency());
                break;
            case ACCEPTANCE_RATE:
                LooseWorkAcceptanceRateMetricDTO looseWorkAcceptanceRateMetricDTO = projectAcceptanceRate(param);
                projectScheduleAllMetricDTO.setAcceptanceRate(looseWorkAcceptanceRateMetricDTO.getAcceptanceRate());
                break;
            default:
                break;
        }
    }
}
