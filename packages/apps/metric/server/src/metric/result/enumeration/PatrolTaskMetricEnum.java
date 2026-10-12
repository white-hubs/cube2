package com.guhecloud.cube.biz.metric.server.metric.result.enumeration;

import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.ElectronMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;

/**
 * 巡更任务
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 19:00
 * @Description:
 */
public enum PatrolTaskMetricEnum {

    /**
     * 异常率
     */
    ELECTRON_ERROR_RATE {
        @Override
        public PatrolTaskRateMetricDTO getMetric(ElectronMetricParam param) {
            ElectronMetricService metricService = ApplicationContextHolder.getBean(ElectronMetricService.class);
            ElectronErrorRateMetricDTO electronErrorRateMetricDTO = metricService.electronErrRate(param);
            return PatrolTaskRateMetricDTO.builder()
                    .patrolTotal(electronErrorRateMetricDTO.getElectronTotal())
                    .patrolAbnormalNumber(electronErrorRateMetricDTO.getElectronAbnormalNumber())
                    .patrolAbnormalRate(electronErrorRateMetricDTO.getElectronAbnormalRate())
                    .build();
        }
    },
    /**
     * 完成率
     */
    ELECTRON_COMPLETED_RATE {
        @Override
        public PatrolTaskRateMetricDTO getMetric(ElectronMetricParam param) {
            ElectronMetricService metricService = ApplicationContextHolder.getBean(ElectronMetricService.class);
            ElectronCompleteRateMetricDTO electronCompleteRateMetricDTO = metricService.electronCompleteRate(param);
            return PatrolTaskRateMetricDTO.builder()
                    .patrolTotal(electronCompleteRateMetricDTO.getElectronTotal())
                    .patrolAbnormalNumber(electronCompleteRateMetricDTO.getElectronCompleteNumber())
                    .patrolAbnormalRate(electronCompleteRateMetricDTO.getElectronCompleteRate())
                    .build();
        }
    };

    public abstract PatrolTaskRateMetricDTO getMetric(ElectronMetricParam param);
}
