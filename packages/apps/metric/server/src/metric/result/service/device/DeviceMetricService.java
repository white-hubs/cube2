package com.guhecloud.cube.biz.metric.server.metric.result.service.device;

import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.DeviceMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceExpireRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceHealthLevelMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceOnlineRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.ProCategoryDeviceCountMetricDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.HealthValueClient;
import com.guhecloud.cube.srv.gen.device.supervise.client.dto.DeviceAvgHealthDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceMetricService {

    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;

    private final HealthValueClient healthValueClient;

    private static final String TAG_PREFIX = "builtin.app::";

    public DeviceMetricService(DeviceBaseStatisticsClient deviceBaseStatisticsClient, HealthValueClient healthValueClient) {
        this.deviceBaseStatisticsClient = deviceBaseStatisticsClient;
        this.healthValueClient = healthValueClient;
    }

    /**
     * 设备在线率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public DeviceOnlineRateMetricDTO getDeviceOnlineRateMetric(DeviceMetricParam param) {
        DeviceStateCountDTO deviceStateCountDTO = deviceBaseStatisticsClient.countingNumberOfGroupByStatusFromSecurity(getAppTags(param));
        if (Objects.isNull(deviceStateCountDTO) ||
            0 == deviceStateCountDTO.getTotalNumber()) {
            return new DeviceOnlineRateMetricDTO(null, 0, 0, 0.0);
        }
        return DeviceOnlineRateMetricDTO.builder()
                .totalNum(deviceStateCountDTO.getTotalNumber())
                .onlineNum(deviceStateCountDTO.getNormalNumber())
                .onlineRate(BigDecimal.valueOf(deviceStateCountDTO.getNormalNumber())
                                    .divide(BigDecimal.valueOf(deviceStateCountDTO.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .doubleValue())
                .build();
    }

    /**
     * 设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "DEVICE_EXPIRATION_RATE", description = "设备质保到期率指标")
    public DeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        QueryDeviceByTagParam queryDeviceByTagParam = new QueryDeviceByTagParam();
        queryDeviceByTagParam.setTagCodes(Collections.singletonList(TAG_PREFIX + param.getAppCode()));
        DeviceUsefulLifeExpireStatisticsVO deviceExpireStatistics = deviceBaseStatisticsClient.getDeviceExpireStatistics(queryDeviceByTagParam);
        return DeviceExpireRateMetricDTO.builder()
                .expireNum(deviceExpireStatistics.getExpireNum())
                .expireRate(deviceExpireStatistics.getExpireRate().multiply(BigDecimal.valueOf(100)))
                .build();
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "DEVICE_HEALTH_LEVEL", description = "设备健康度指标")
    public DeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        DeviceAvgHealthDTO healthValueByTag = this.healthValueClient.getHealthValueByTag(TAG_PREFIX + param.getAppCode());
        if (Objects.isNull(healthValueByTag)) {
            return DeviceHealthLevelMetricDTO.builder()
                    .healthLevel(0.0)
                    .healthBelowSixtyNumber(0)
                    .healthBelowSixtyRate(0.0)
                    .build();
        }
        return DeviceHealthLevelMetricDTO.builder()
                .healthLevel(healthValueByTag.getAvgHealthValue())
                .healthBelowSixtyNumber(healthValueByTag.getBelowSixtyNumber())
                .healthBelowSixtyRate(healthValueByTag.getBelowSixtyRate() * 100)
                .build();
    }

    /**
     * 按品类统计设备数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "CATEGORY_DEVICE_NUMBER", description = "按品类统计设备数量指标")
    public List<ProCategoryDeviceCountMetricDTO> getProCategoryDeviceCountMetric(DeviceMetricParam param) {
        List<ProductCategoryOffLineCountVO> productCategoryStatistics = this.deviceBaseStatisticsClient.getProductCategoryStatisticsByTagCodes(convertParam(param));
        return productCategoryStatistics.stream()
                .map(prodCategory -> ObjectUtils.copying(prodCategory, ProCategoryDeviceCountMetricDTO.class))
                .collect(Collectors.toList());
    }

    private QueryDeviceByTagParam convertParam(DeviceMetricParam param) {
        QueryDeviceByTagParam queryDeviceByTagParam = new QueryDeviceByTagParam();
        queryDeviceByTagParam.setTagCodes(getAppTags(param));
        return queryDeviceByTagParam;
    }

    private List<String> getAppTags(DeviceMetricParam param) {
        List<String> tags = new ArrayList<>();
        if (StringUtils.isNoneBlank(param.getAppCode())) {
            tags.add(TAG_PREFIX + param.getAppCode());
        }
        if (!CollectionUtils.isEmpty(param.getAppCodes())) {
            for (String appCode : param.getAppCodes()) {
                tags.add(TAG_PREFIX + appCode);
            }
        }
        return tags;
    }
}
