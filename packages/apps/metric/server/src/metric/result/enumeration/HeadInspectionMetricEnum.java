package com.guhecloud.cube.biz.metric.server.metric.result.enumeration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_EXCEPTION_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_TOTAL_NUMBER_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/4 10:23
 * @Description:
 */
public enum HeadInspectionMetricEnum {

    /**
     * 巡检数量
     */
    INSPECT_TASK_TOTAL {
        @Override
        public List<InspectionRateMetricDTO> getMetric(InspectionMetricParam param) {
            AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
            List<MarketMetricResult<Long>> allMarketInspectionTotalMetrics = asyncHttpClientUtils
                    .getAllMarketMetrics(INSPECTION_TOTAL_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<Long>() {});

            return allMarketInspectionTotalMetrics.stream().map(t -> InspectionRateMetricDTO.builder()
                    .inspectionTotal(t.getMetricStruct())
                    .build()).collect(Collectors.toList());
        }
    },
    /**
     * 异常率占比
     */
    INSPECT_ERROR_RATE {
        @Override
        public List<InspectionRateMetricDTO> getMetric(InspectionMetricParam param) {
            AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
            List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionErrorMetrics = asyncHttpClientUtils
                    .getAllMarketMetrics(INSPECTION_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
            return allMarketInspectionErrorMetrics.stream().map(t -> InspectionRateMetricDTO.builder()
                    .inspectionTotal(t.getMetricStruct().getTotal())
                    .inspectionAbnormalNumber(t.getMetricStruct().getNumber())
                    .inspectionAbnormalRate(t.getMetricStruct().getRate())
                    .build()).collect(Collectors.toList());
        }

    },
    /**
     * 完成率
     */
    INSPECT_COMPLETE_RATE {
        @Override
        public List<InspectionRateMetricDTO> getMetric(InspectionMetricParam param) {
            AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
            List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                    .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});

            return allMarketInspectionCompleteMetrics.stream().map(t -> InspectionRateMetricDTO.builder()
                    .inspectionTotal(t.getMetricStruct().getTotal())
                    .inspectionCompleteNumber(t.getMetricStruct().getNumber())
                    .inspectCompleteRate(t.getMetricStruct().getRate())
                    .build()).collect(Collectors.toList());
        }

    };

    public abstract List<InspectionRateMetricDTO> getMetric(InspectionMetricParam param);

}
