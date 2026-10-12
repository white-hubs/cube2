package com.guhecloud.cube.biz.metric.server.metric.result.enumeration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpElectronQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_EXCEPTION_RATE_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/4 11:45
 * @Description:
 */
public enum HeadPatrolTaskMetricEnum {

    /**
     * 异常率
     */
    ELECTRON_ERROR_RATE {
        @Override
        public List<PatrolTaskRateMetricDTO> getMetric(ElectronMetricParam param) {
            AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
            List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronErrMetrics = asyncHttpClientUtils
                    .getAllMarketMetrics(PATROL_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});

            return allMarketElectronErrMetrics.stream().map(t -> PatrolTaskRateMetricDTO.builder()
                    .marketName(t.getMarketName())
                    .patrolTotal(t.getMetricStruct().getTotal())
                    .patrolAbnormalNumber(t.getMetricStruct().getNumber())
                    .patrolAbnormalRate(t.getMetricStruct().getRate())
                    .build()).collect(Collectors.toList());
        }
    },
    /**
     * 完成率
     */
    ELECTRON_COMPLETED_RATE {
        @Override
        public List<PatrolTaskRateMetricDTO> getMetric(ElectronMetricParam param) {
            AsyncHttpClientUtils asyncHttpClientUtils = ApplicationContextHolder.getBean(AsyncHttpClientUtils.class);
            List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronCompleteMetrics = asyncHttpClientUtils
                    .getAllMarketMetrics(PATROL_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});

            return allMarketElectronCompleteMetrics.stream().map(t -> PatrolTaskRateMetricDTO.builder()
                    .marketName(t.getMarketName())
                    .patrolTotal(t.getMetricStruct().getTotal())
                    .patrolAbnormalNumber(t.getMetricStruct().getNumber())
                    .patrolAbnormalRate(t.getMetricStruct().getRate())
                    .build()).collect(Collectors.toList());
        }
    };

    public abstract List<PatrolTaskRateMetricDTO> getMetric(ElectronMetricParam param);

    public ElectronMetricParam convertParam(McpElectronQueryParam param) {
        ElectronMetricParam queryParam = new ElectronMetricParam();
        queryParam.setPeriod(new Period(param.getBeginTime(), param.getEndTime()));
        queryParam.setAreaName(param.getAreaName());
        queryParam.setPointName(param.getPointName());
        return queryParam;
    }
}
