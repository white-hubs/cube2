package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleMetricVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 小散工程基础
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 18:17
 * @Description:
 */

@Slf4j
@Service
public class BaseLooseWorksMetricService {
    private final ProjectScheduleClient projectScheduleClient;

    public BaseLooseWorksMetricService(ProjectScheduleClient projectScheduleClient) {this.projectScheduleClient = projectScheduleClient;}

    @MetricTopic(topic = "BASE_PROJECT_NUMBER", description = "小散工程数量")
    public Long projectNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.COMPLETED_RATE);
        return projectScheduleMetricVO.getNumber();
    }

    /**
     * 小散工程完成数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "BASE_PROJECT_COMPLETED_Number", description = "小散工程完成数")
    public Long projectCompletedNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.COMPLETED_RATE);
        return projectScheduleMetricVO.getNumber();
    }

    /**
     * 小散工程完成率
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "BASE_PROJECT_COMPLETED_RATE", description = "小散工程完成率")
    public Double projectCompletedRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.COMPLETED_RATE);
        return projectScheduleMetricVO.getRate();
    }

    /**
     * 小散工程异常率
     */
    @MetricTopic(topic = "BASE_PROJECT_ABNORMAL_RATE", description = "小散工程异常率")
    public Double projectAbnormalRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ABNORMAL_RATE);
        return projectScheduleMetricVO.getRate();
    }

    /**
     * 小散工程异常数
     */
    @MetricTopic(topic = "BASE_PROJECT_ABNORMAL_NUMBER", description = "小散工程异常数")
    public Long projectAbnormalNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ABNORMAL_RATE);
        return projectScheduleMetricVO.getNumber();
    }

    /**
     * 小散工程整改率
     */
    @MetricTopic(topic = "BASE_PROJECT_REFORM_RATE", description = "小散工程整改率")
    public Double projectReformRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.REFORM_RATE);
        return projectScheduleMetricVO.getRate();
    }

    /**
     * 小散工程整改数
     */
    @MetricTopic(topic = "BASE_PROJECT_REFORM_NUMBER", description = "小散工程整改数")
    public Long projectReformNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.REFORM_RATE);
        return projectScheduleMetricVO.getNumber();
    }

    /**
     * 小散工程验收率
     */
    @MetricTopic(topic = "BASE_PROJECT_ACCEPTANCE_RATE", description = "小散工程验收率")
    public Double projectAcceptanceRate(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ACCEPTANCE_RATE);
        return projectScheduleMetricVO.getRate();
    }

    /**
     * 小散工程验收数
     */
    @MetricTopic(topic = "BASE_PROJECT_ACCEPTANCE_NUMBER", description = "小散工程验收数")
    public Long projectAcceptanceNumber(MetricParam param) {
        ProjectScheduleMetricVO projectScheduleMetricVO = projectScheduleClient.projectScheduleMetric(param.getPeriod(), MetricType.ACCEPTANCE_RATE);
        return projectScheduleMetricVO.getNumber();
    }
}
