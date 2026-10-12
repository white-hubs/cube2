package com.guhecloud.cube.biz.metric.server.metric.result.enumeration;

import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.InspectionMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionTotalMetricDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 16:27
 * @Description: 巡检数据相关指标类型
 */

public enum InspectionMetricEnum {

    /**
     * 巡检数量
     */
    INSPECT_TASK_TOTAL {
        @Override
        public InspectionRateMetricDTO getMetric(InspectionMetricParam param) {
            InspectionMetricService service = ApplicationContextHolder.getBean(InspectionMetricService.class);
            InspectionTotalMetricDTO inspectionTotalMetricDTO = service.inspectionTaskTotal(param);
            return InspectionRateMetricDTO.builder()
                    .inspectionTotal(inspectionTotalMetricDTO.getInspectionTotal())
                    .build();
        }
    },
    /**
     * 异常率占比
     */
    INSPECT_ERROR_RATE {
        @Override
        public InspectionRateMetricDTO getMetric(InspectionMetricParam param) {
            InspectionMetricService service = ApplicationContextHolder.getBean(InspectionMetricService.class);
            InspectionErrorRateMetricDTO inspectionErrorRateMetricDTO = service.inspectionErrorRate(param);
            return InspectionRateMetricDTO.builder()
                    .inspectionTotal(inspectionErrorRateMetricDTO.getInspectionTotal())
                    .inspectionAbnormalNumber(inspectionErrorRateMetricDTO.getInspectionAbnormalNumber())
                    .inspectionAbnormalRate(inspectionErrorRateMetricDTO.getInspectionAbnormalRate())
                    .build();
        }

    },
    /**
     * 完成率
     */
    INSPECT_COMPLETE_RATE {
        @Override
        public InspectionRateMetricDTO getMetric(InspectionMetricParam param) {
            InspectionMetricService service = ApplicationContextHolder.getBean(InspectionMetricService.class);
            InspectionCompleteRateMetricDTO inspectionCompleteRateMetricDTO = service.inspectionCompleteRate(param);
            return InspectionRateMetricDTO.builder()
                    .inspectionTotal(inspectionCompleteRateMetricDTO.getInspectTotal())
                    .inspectionAbnormalNumber(inspectionCompleteRateMetricDTO.getInspectionCompleteNumber())
                    .inspectionAbnormalRate(inspectionCompleteRateMetricDTO.getInspectCompleteRate())
                    .build();
        }

    };

    public abstract InspectionRateMetricDTO getMetric(InspectionMetricParam param);

}
