package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadDeviceHealthLevelMetricDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.dto.DeviceAvgHealthDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_EXPIRE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.DEVICE_STATUS_NUMBER_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/17 11:39
 * @Description:
 */
@Service
public class HeadBaseDeviceMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    private static final String TAG_PREFIX = "builtin.app::";

    public HeadBaseDeviceMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    /**
     * 设备总数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_NUMBER", description = "总部设备总数")
    public Integer getBaseDeviceNumberMetric(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceStateCountDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceStateCountDTO>() {
                });
        int deviceTotalNumber = 0;
        for (MarketMetricResult<DeviceStateCountDTO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceStateCountDTO metricStruct = allMarketMetric.getMetricStruct();
            deviceTotalNumber += metricStruct.getTotalNumber();
        }
        return deviceTotalNumber;
    }

    /**
     * 总部设备在线数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_ONLINE_NUMBER", description = "总部设备在线数")
    public Integer getDeviceOnlineNumberMetric(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceStateCountDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceStateCountDTO>() {
                });
        int normalNumber = 0;
        for (MarketMetricResult<DeviceStateCountDTO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceStateCountDTO metricStruct = allMarketMetric.getMetricStruct();
            normalNumber += metricStruct.getNormalNumber();
        }
        return normalNumber;
    }

    /**
     * 总部设备到期数
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_EXPIRE_NUMBER", description = "总部设备到期数")
    public Integer getDeviceExpireNumberMetric(MetricParam param) {
        QueryDeviceByTagParam queryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO>> allMarketMetrics = asyncHttpClientUtils.getAllMarketMetrics(DEVICE_EXPIRE_RATE_REQUEST_URL, HttpMethod.POST, queryParam, queryParam, new TypeReference<DeviceUsefulLifeExpireStatisticsVO>() {
        });

        int totalExpireNum = 0;
        for (MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceUsefulLifeExpireStatisticsVO metricStruct = allMarketMetric.getMetricStruct();
            totalExpireNum += metricStruct.getExpireNum();
        }
        return totalExpireNum;
    }

    /**
     * 总部设备健康度低于60数量
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_HEALTH_BELOW_SIXTY_NUMBER", description = "总部设备健康度低于60数量")
    public Integer getHeadDeviceHealthBelowSixtyNumber(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceAvgHealthDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceAvgHealthDTO>() {
                });
        int totalBelowSixtyNumber = 0;
        List<HeadDeviceHealthLevelMetricDTO.MarketDeviceAvgHealth> belowSixtyMarkets = new ArrayList<>();
        for (MarketMetricResult<DeviceAvgHealthDTO> allMarketMetric : allMarketMetrics) {
            DeviceAvgHealthDTO metricStruct = allMarketMetric.getMetricStruct();
            totalBelowSixtyNumber += metricStruct.getBelowSixtyNumber();
        }
        return totalBelowSixtyNumber;
    }

    /**
     * 总部设备在线率最高的三个市场
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_ONLINE_RATE_TOP_MARKET_RANK", description = "总部设备在线率最高的三个市场")
    public List<HandlerRateDTO> getHeadDeviceOnLineRateMarket(MetricParam param) {
        List<HandlerRateDTO> handlerRateDTOS = getDeiceOnLineRateMaket(param);
        return handlerRateDTOS.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(3)
                .collect(Collectors.toList());
    }

    /**
     * 总部设备在线率最低的三个市场
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_ONLINE_RATE_LAST_MARKET_RANK", description = "总部设备在线率最低的三个市场")
    public List<HandlerRateDTO> getHeadDeviceOnLineRateMarketLow(MetricParam param) {
        List<HandlerRateDTO> handlerRateDTOS = getDeiceOnLineRateMaket(param);
        return handlerRateDTOS.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(3)
                .collect(Collectors.toList());
    }

    /**
     * 设备健康度低于60分的市场
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_HEALTH_BELOW_SIXTY_MARKET", description = "设备健康度低于60分的市场")
    public List<HandlerRateDTO> getHeadDeviceHealthBelowSixtyMetric(MetricParam param) {
        List<HandlerRateDTO> belowSixtyMarkets = getHeadDeviceHealthMarket(param);
        return belowSixtyMarkets;
    }

    /**
     * 设备健康度高于90分的市场
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_HEALTH_ABOVE_NINETY_MARKET", description = "设备健康度高于90分的市场")
    public List<HandlerRateDTO> getHeadDeviceHealthAboveNinetyMetric(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceAvgHealthDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceAvgHealthDTO>() {
                });
        List<HandlerRateDTO> aboveNinetyMarkets = new ArrayList<>();
        for (MarketMetricResult<DeviceAvgHealthDTO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceAvgHealthDTO metricStruct = allMarketMetric.getMetricStruct();
            if (metricStruct.getAvgHealthValue() > 90) {
                aboveNinetyMarkets.add(HandlerRateDTO.builder()
                                               .marketName(allMarketMetric.getMarketName())
                                               .num(metricStruct.getAvgHealthValue().longValue())
                                               .build());
            }
        }
        return aboveNinetyMarkets;
    }

    /**
     * 总部设备到期率市场排行榜
     */
    @MetricTopic(topic = "HEAD_BASE_DEVICE_EXPIRE_RATE_MARKET_RANK", description = "总部设备到期率市场排行榜")
    public List<HandlerRateDTO> getHeadDeviceExpireRateMetric(MetricParam param) {
        QueryDeviceByTagParam queryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO>> allMarketMetrics = asyncHttpClientUtils.getAllMarketMetrics(DEVICE_EXPIRE_RATE_REQUEST_URL, HttpMethod.POST, queryParam, queryParam, new TypeReference<DeviceUsefulLifeExpireStatisticsVO>() {
        });
        List<HandlerRateDTO> marketRanks = new ArrayList<>();
        for (MarketMetricResult<DeviceUsefulLifeExpireStatisticsVO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceUsefulLifeExpireStatisticsVO metricStruct = allMarketMetric.getMetricStruct();
            marketRanks.add(HandlerRateDTO.builder()
                                    .marketName(allMarketMetric.getMarketName())
                                    .num(Long.valueOf(metricStruct.getExpireNum()))
                                    .rate(metricStruct.getExpireRate().multiply(BigDecimal.valueOf(100))
                                                  .doubleValue())
                                    .build());
        }
        return marketRanks.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .collect(Collectors.toList());
    }

    private QueryDeviceByTagParam getQueryDeviceByTagParam(MetricParam param) {
        QueryDeviceByTagParam queryParam = new QueryDeviceByTagParam();
        queryParam.setTagCodes(Collections.singletonList(TAG_PREFIX + param.getAppCode()));
        return queryParam;
    }

    private List<HandlerRateDTO> getHeadDeviceHealthMarket(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceAvgHealthDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceAvgHealthDTO>() {
                });
        List<HandlerRateDTO> belowSixtyMarkets = new ArrayList<>();
        for (MarketMetricResult<DeviceAvgHealthDTO> allMarketMetric : allMarketMetrics) {
            if (Objects.isNull(allMarketMetric.getMetricStruct())) {
                continue;
            }
            DeviceAvgHealthDTO metricStruct = allMarketMetric.getMetricStruct();
            if (metricStruct.getAvgHealthValue() < 60) {
                belowSixtyMarkets.add(HandlerRateDTO.builder()
                                              .marketName(allMarketMetric.getMarketName())
                                              .num(metricStruct.getAvgHealthValue().longValue())
                                              .build());
            }
        }
        return belowSixtyMarkets;
    }

    /**
     * 总部设备在线率指标
     */
    private List<HandlerRateDTO> getDeiceOnLineRateMaket(MetricParam param) {
        QueryDeviceByTagParam deviceQueryParam = getQueryDeviceByTagParam(param);
        List<MarketMetricResult<DeviceStateCountDTO>> allMarketMetrics = asyncHttpClientUtils.
                getAllMarketMetrics(DEVICE_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, deviceQueryParam, deviceQueryParam, new TypeReference<DeviceStateCountDTO>() {
                });
        return allMarketMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> HandlerRateDTO.builder()
                        .marketName(t.getMarketName())
                        .num((long) t.getMetricStruct().getNormalNumber())
                        .rate(Objects.isNull(t.getMetricStruct().getTotalNumber()) ||
                              0 == t.getMetricStruct().getTotalNumber()
                              ? 0 : BigDecimal.valueOf(t.getMetricStruct().getNormalNumber())
                                      .divide(BigDecimal.valueOf(t.getMetricStruct().getTotalNumber()), 2, RoundingMode.HALF_UP)
                                      .multiply(BigDecimal.valueOf(100)).doubleValue())
                        .build()
                    ).collect(Collectors.toList());
    }
}
