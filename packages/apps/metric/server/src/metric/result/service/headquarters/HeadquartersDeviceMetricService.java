package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpDeviceQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceOnlineRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.ProCategoryDeviceCountMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadDeviceExpireRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadDeviceHealthLevelMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadOnlineRateMetricDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.dto.DeviceAvgHealthDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_EXPIRE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_STATUS_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_TYPE_NUMBER_REQUEST_URL;

/**
 * 集团指标服务
 *
 * @author lanchun
 * @date 2025/6/12 15:59
 */
@Service
public class HeadquartersDeviceMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    private static final String TAG_PREFIX = "builtin.app::";

    public HeadquartersDeviceMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    /**
     * 设备在线率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public HeadOnlineRateMetricDTO getDeviceOnlineRateMetric(MetricParam param) {
        List<String> tags = Collections.singletonList(TAG_PREFIX + param.getAppCode());
        List<MarketMetricResult<DeviceStateCountDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, tags, tags, new TypeReference<DeviceStateCountDTO>() {
                });
        int deviceTotalNumber = 0;
        int offlineNumber = 0;
        int onlineNum = 0;
        for (MarketMetricResult<DeviceStateCountDTO> allMarketMetric : allMarketMetrics) {
            DeviceStateCountDTO metricStruct = allMarketMetric.getMetricStruct();
            deviceTotalNumber += metricStruct.getTotalNumber();
            offlineNumber += metricStruct.getOfflineNumber();
            onlineNum += metricStruct.getNormalNumber();
        }
        if (0 == deviceTotalNumber) {
            return HeadOnlineRateMetricDTO.getDefault();
        }
        return HeadOnlineRateMetricDTO.builder()
                .deviceTotalNumber(deviceTotalNumber)
                .onlineNum(onlineNum)
                .onlineRate(BigDecimal.valueOf(onlineNum)
                                    .divide(BigDecimal.valueOf(deviceTotalNumber), 2, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100)).doubleValue())
                .offlineNumber(offlineNumber)
                .offlineRate(BigDecimal.valueOf(offlineNumber)
                                     .divide(BigDecimal.valueOf(deviceTotalNumber), 2, RoundingMode.HALF_UP)
                                     .multiply(BigDecimal.valueOf(100))
                                     .doubleValue())
                .build();
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_DEVICE_HEALTH_LEVEL", description = "总部设备健康度指标")
    public HeadDeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        List<String> tags = Collections.singletonList(TAG_PREFIX + param.getAppCode());
        List<MarketMetricResult<DeviceAvgHealthDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL, HttpMethod.GET, tags, tags, new TypeReference<DeviceAvgHealthDTO>() {
                });
        int totalDeviceNumber = 0;
        double totalDeviceHealthLevel = 0;
        int totalBelowSixtyNumber = 0;
        List<HeadDeviceHealthLevelMetricDTO.MarketDeviceAvgHealth> belowSixtyMarkets = new ArrayList<>();
        for (MarketMetricResult<DeviceAvgHealthDTO> allMarketMetric : allMarketMetrics) {
            DeviceAvgHealthDTO metricStruct = allMarketMetric.getMetricStruct();
            totalDeviceNumber += metricStruct.getTotalDeviceNumber();
            totalDeviceHealthLevel += metricStruct.getAvgHealthValue();
            totalBelowSixtyNumber += metricStruct.getBelowSixtyNumber();
            if (metricStruct.getAvgHealthValue() < 60) {
                belowSixtyMarkets.add(HeadDeviceHealthLevelMetricDTO.MarketDeviceAvgHealth.builder()
                                              .marketCode(allMarketMetric.getMarketTenant())
                                              .marketName(allMarketMetric.getMarketName())
                                              .healthLevel(metricStruct.getAvgHealthValue())
                                              .build());
            }
        }
        return HeadDeviceHealthLevelMetricDTO.builder()
                .totalDeviceNumber(totalDeviceNumber)
                .healthLevel(allMarketMetrics.isEmpty() ? 0 : BigDecimal.valueOf(totalDeviceHealthLevel)
                        .divide(BigDecimal.valueOf(allMarketMetrics.size()), 2, RoundingMode.HALF_UP).doubleValue())
                .healthBelowSixtyNumber(totalBelowSixtyNumber)
                .healthBelowSixtyRate(0 == totalDeviceNumber ? 0 : BigDecimal.valueOf(totalBelowSixtyNumber)
                        .divide(BigDecimal.valueOf(totalDeviceNumber), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue())
                .belowSixtyMarkets(belowSixtyMarkets)
                .build();
    }

    /**
     * 总部设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_DEVICE_EXPIRATION_RATE", description = "总部设备质保到期率指标")
    public HeadDeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        QueryDeviceByTagParam queryParam = new QueryDeviceByTagParam();
        queryParam.setTagCodes(Collections.singletonList(TAG_PREFIX + param.getAppCode()));
        List<MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO>> allMarketMetrics = asyncHttpClientUtils.getAllMarketMetrics(DEVICE_EXPIRE_RATE_REQUEST_URL, HttpMethod.POST, queryParam, queryParam, new TypeReference<DeviceUsefulLifeExpireStatisticsVO>() {
        });
        int totalDeviceNumber = 0;
        int totalExpireNum = 0;
        List<HeadDeviceExpireRateMetricDTO.MarketDeviceExpireRate> geSixtyMarkets = new ArrayList<>();
        for (MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO> allMarketMetric : allMarketMetrics) {
            DeviceUsefulLifeExpireStatisticsVO metricStruct = allMarketMetric.getMetricStruct();
            totalDeviceNumber += metricStruct.getTotalDeviceNumber();
            totalExpireNum += metricStruct.getExpireNum();
            if (metricStruct.getExpireRate().doubleValue() > 60) {
                geSixtyMarkets.add(HeadDeviceExpireRateMetricDTO.MarketDeviceExpireRate.builder()
                                           .marketCode(allMarketMetric.getMarketTenant())
                                           .marketName(allMarketMetric.getMarketName())
                                           .expireNum(metricStruct.getExpireNum())
                                           .expireRate(BigDecimal.valueOf(metricStruct.getExpireRate()
                                                                                  .multiply(BigDecimal.valueOf(100))
                                                                                  .doubleValue()))
                                           .build());
            }
        }
        return HeadDeviceExpireRateMetricDTO.builder()
                .totalDeviceNumber(totalDeviceNumber)
                .expireNum(totalExpireNum)
                .expireRate(0 == totalExpireNum ? 0 : BigDecimal.valueOf(totalExpireNum)
                        .divide(BigDecimal.valueOf(totalExpireNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue())
                .marketDeviceExpireRates(geSixtyMarkets)
                .build();
    }

    public List<DeviceOnlineRateMetricDTO> getHeadMcpDeiceNumberMetric(McpDeviceQueryParam param) {
        List<String> tags = param.getAppCodes().stream()
                .map(appCode -> TAG_PREFIX + appCode)
                .collect(Collectors.toList());
        List<MarketMetricResult<DeviceStateCountDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, tags, tags, new TypeReference<DeviceStateCountDTO>() {
                });
        return allMarketMetrics.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> DeviceOnlineRateMetricDTO.builder()
                        .marketName(t.getMarketName())
                        .totalNum(t.getMetricStruct().getTotalNumber())
                        .onlineNum(t.getMetricStruct().getNormalNumber())
                        .onlineRate(Objects.isNull(t.getMetricStruct().getTotalNumber()) || 0 == t.getMetricStruct().getTotalNumber()
                                    ? 0 : BigDecimal.valueOf(t.getMetricStruct().getNormalNumber())
                                            .divide(BigDecimal.valueOf(t.getMetricStruct().getTotalNumber()), 2, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100))
                                            .doubleValue())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProCategoryDeviceCountMetricDTO> getHeadMcpDeiceTypeNumberMetric(McpDeviceQueryParam param) {
        QueryDeviceByTagParam queryDeviceByTagParam = new QueryDeviceByTagParam();
        queryDeviceByTagParam.setTagCodes(param.getAppCodes().stream()
                                                  .map(appCode -> TAG_PREFIX + appCode)
                                                  .collect(Collectors.toList()));
        List<MarketMetricResult<List<ProductCategoryOffLineCountVO>>> productCategoryStatistics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_TYPE_NUMBER_REQUEST_URL, HttpMethod.POST, queryDeviceByTagParam, null, new TypeReference<List<ProductCategoryOffLineCountVO>>() {
                });
        return productCategoryStatistics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct()
                        .stream().map(prodCategory -> {
                            ProCategoryDeviceCountMetricDTO metricDTO = ObjectUtils.copying(prodCategory, ProCategoryDeviceCountMetricDTO.class);
                            metricDTO.setMarketName(t.getMarketName());
                            return metricDTO;
                        })
                        .collect(Collectors.toList())
                    )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
