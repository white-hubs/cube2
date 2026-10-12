package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.HikRemoteCheckDeviceMappingProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.DoorGasLinkageConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.DoorGasType;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.DoorGasLinkageConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.HikRemoteCheckService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckAccessControllerEventRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckResultResponse;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 海康门禁远程核验回调接收业务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HikRemoteCheckServiceImpl implements HikRemoteCheckService {

    private static final String CHECK_RESULT_SUCCESS = "success";
    private static final String CHECK_RESULT_FAILED = "failed";
    private static final String ALLOW_MESSAGE = "允许通行";
    private static final String OFFLINE_MESSAGE = "设备离线，请确定环境安全";

    private final HikRemoteCheckDeviceMappingProvider deviceMappingProvider;
    private final DoorGasLinkageConfigService doorGasLinkageConfigService;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;
    private final IotDevicePropClient iotDevicePropClient;

    @Override
    public HikRemoteCheckResponse receive(Integer tenant, HikRemoteCheckRequest request, MultipartFile picture) {
        Assert.isParameterError(tenant == null, "租户不能为空");
        SessionContextHolder.requestTenant(tenant);
        Assert.isParameterError(request == null, "远程核验事件不能为空");
        HikRemoteCheckAccessControllerEventRequest event = request.accessControllerEvent();
        Assert.isParameterError(event == null, "远程核验门禁事件不能为空");
        log.info("接收海康门禁远程核验事件，tenant={}，macAddress={}，设备序列号={}，人员编号={}，事件流水号={}，remoteCheck={}，图片大小={}",
                 SessionContextHolder.getRequester().getTenant(),
                 request.macAddress(),
                 request.shortSerialNumber(),
                 event.employeeNoString(),
                 event.serialNo(),
                 event.remoteCheck(),
                 picture == null ? 0L : picture.getSize());

        Long doorDeviceId = deviceMappingProvider.resolveDoorDeviceId(request.macAddress());
        if (doorDeviceId == null) {
            return allow(event.serialNo(), "MAC 未匹配到门禁设备");
        }
        log.info("海康门禁远程核验完成 MAC 映射，macAddress={}，doorDeviceId={}",
                 request.macAddress(), doorDeviceId);
        DoorGasLinkageConfig config = doorGasLinkageConfigService.lambdaQuery()
                .eq(DoorGasLinkageConfig::getDoorDeviceId, doorDeviceId)
                .one();
        log.info("查询门禁气体联动规则，doorDeviceId={}，configFound={}，enabled={}，dataValidSeconds={}，configJsonLength={}",
                 doorDeviceId,
                 config != null,
                 config != null && Boolean.TRUE.equals(config.getGasLinkageEnabled()),
                 config == null ? null : config.getDataValidSeconds(),
                 config == null || config.getDeviceConfigJson() == null ? 0 : config.getDeviceConfigJson().length());
        if (config == null || !Boolean.TRUE.equals(config.getGasLinkageEnabled())) {
            return allow(event.serialNo(), config == null ? "门禁未配置气体联动规则" : "气体联动规则未启用");
        }

        List<GasRule> rules = parseRules(config.getDeviceConfigJson());
        log.info("解析门禁气体联动规则，doorDeviceId={}，ruleCount={}", doorDeviceId, rules.size());
        LiangcangLocationCardConfigDTO propertyConfig = locationCardConfigProvider.resolve();
        if (!hasCompleteRules(rules, propertyConfig, config.getDataValidSeconds())) {
            return allow(event.serialNo(), "气体联动规则不完整或气体属性编码未配置");
        }
        Map<GasRule, GasValue> values = loadGasValues(rules, propertyConfig);
        log.info("读取门禁联动气体实时数据，doorDeviceId={}，expectedRuleCount={}，matchedValueCount={}",
                 doorDeviceId, rules.size(), values.size());
        boolean hasUnavailableGasData = values.size() != rules.size();
        for (GasRule rule : rules) {
            GasValue value = values.get(rule);
            if (!isValid(value, config)) {
                hasUnavailableGasData = true;
                log.info("气体实时数据缺失或已失效，doorDeviceId={}，gasType={}，sensorDeviceId={}，timestamp={}，dataValidSeconds={}",
                         doorDeviceId,
                         rule.gasType(),
                         rule.sensorDeviceId(),
                         value == null ? null : value.timestamp(),
                         config.getDataValidSeconds());
                continue;
            }
            boolean outsideRange = isOutsideRange(rule, value.value());
            log.info("判断气体联动阈值，doorDeviceId={}，gasType={}，sensorDeviceId={}，value={}，lowerThreshold={}，upperThreshold={}，outsideRange={}",
                     doorDeviceId,
                     rule.gasType(),
                     rule.sensorDeviceId(),
                     value.value(),
                     rule.lowerThreshold(),
                     rule.upperThreshold(),
                     outsideRange);
            if (outsideRange) {
                String message = outsideRangeMessage(rule.gasType());
                log.warn("气体联动核验拒绝通行，doorDeviceId={}，gasType={}，sensorDeviceId={}，value={}，lowerThreshold={}，upperThreshold={}，message={}",
                         doorDeviceId,
                         rule.gasType(),
                         rule.sensorDeviceId(),
                         value.value(),
                         rule.lowerThreshold(),
                         rule.upperThreshold(),
                         message);
                return new HikRemoteCheckResponse(new HikRemoteCheckResultResponse(
                        event.serialNo(), CHECK_RESULT_FAILED, message, message));
            }
        }
        if (hasUnavailableGasData) {
            return allow(event.serialNo(), OFFLINE_MESSAGE, "存在缺失或超过有效时长的气体数据，但其余有效气体均在安全范围内");
        }
        return allow(event.serialNo(), "全部有效气体数据均在安全范围内");
    }

    private HikRemoteCheckResponse allow(Integer serialNo, String reason) {
        return allow(serialNo, ALLOW_MESSAGE, reason);
    }

    private HikRemoteCheckResponse allow(Integer serialNo, String message, String reason) {
        log.info("海康门禁远程核验允许通行，serialNo={}，reason={}，message={}", serialNo, reason, message);
        return new HikRemoteCheckResponse(new HikRemoteCheckResultResponse(
                serialNo, CHECK_RESULT_SUCCESS, message, message));
    }

    private List<GasRule> parseRules(String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }
        try {
            List<GasRule> rules = JsonUtils.deserialize(json, new TypeReference<List<GasRule>>() {});
            return rules == null ? List.of() : rules;
        } catch (RuntimeException exception) {
            log.warn("门禁气体联动配置解析失败，configJsonLength={}，远程核验按允许通行处理",
                     json.length(), exception);
            return List.of();
        }
    }

    private boolean hasCompleteRules(List<GasRule> rules,
                                     LiangcangLocationCardConfigDTO propertyConfig,
                                     Integer validSeconds
                                    ) {
        if (rules.isEmpty() || validSeconds == null || validSeconds <= 0) {
            return false;
        }
        return rules.stream().allMatch(rule -> rule != null
                                               && rule.gasType() != null
                                               && rule.sensorDeviceId() != null
                                               && rule.upperThreshold() != null
                                               && (rule.gasType() != DoorGasType.O2 || rule.lowerThreshold() != null)
                                               && propertyCode(propertyConfig) != null);
    }

    private Map<GasRule, GasValue> loadGasValues(List<GasRule> rules,
                                                 LiangcangLocationCardConfigDTO propertyConfig
                                                ) {
        try {
            MultiDevicePropParam param = new MultiDevicePropParam();
            param.setDevIdAndPropCodes(rules.stream()
                                               .map(rule -> rule.sensorDeviceId() + "." + propertyCode(propertyConfig))
                                               .collect(Collectors.joining(",")));
            log.info("调用气体设备实时属性查询，gasCount={}，sensorDeviceIds={}",
                     rules.size(), rules.stream().map(GasRule::sensorDeviceId).distinct().toList());
            Map<String, List<KeyValueWithTsDTO>> valuesByDevice = iotDevicePropClient.multiProp(param);
            Map<GasRule, GasValue> result = new HashMap<>();
            for (GasRule rule : rules) {
                GasValue value = findValue(valuesByDevice == null ? null : valuesByDevice.get(
                        String.valueOf(rule.sensorDeviceId())), propertyCode(propertyConfig));
                if (value != null) {
                    result.put(rule, value);
                }
            }
            return result;
        } catch (RuntimeException exception) {
            log.warn("读取门禁联动气体实时值失败，gasCount={}，远程核验按允许通行处理",
                     rules.size(), exception);
            return Map.of();
        }
    }

    private GasValue findValue(List<KeyValueWithTsDTO> values, String propertyCode) {
        if (values == null) {
            return null;
        }
        for (KeyValueWithTsDTO value : values) {
            if (value != null && propertyCode.equals(value.getKey())) {
                try {
                    return new GasValue(new BigDecimal(String.valueOf(value.getValue())), value.getTs());
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isValid(GasValue value, DoorGasLinkageConfig config) {
        if (value == null || value.timestamp() == null || value.value() == null) {
            return false;
        }
        long validMillis = config.getDataValidSeconds() * 1000L;
        return System.currentTimeMillis() - value.timestamp() <= validMillis;
    }

    private boolean isOutsideRange(GasRule rule, BigDecimal value) {
        if (rule.gasType() == DoorGasType.O2) {
            return value.compareTo(rule.lowerThreshold()) < 0 || value.compareTo(rule.upperThreshold()) > 0;
        }
        return value.compareTo(rule.upperThreshold()) > 0;
    }

    private String propertyCode(LiangcangLocationCardConfigDTO config) {
        if (config == null) {
            return null;
        }
        return trimToNull(config.getGasRealtimePropCode());
    }

    private String trimToNull(String value) {
        return StringUtils.isBlank(value) ? null : value.trim();
    }

    private String outsideRangeMessage(DoorGasType gasType) {
        return switch (gasType) {
            case O2 -> "氧气浓度超出安全范围，禁止通行";
            case CO2 -> "二氧化碳浓度超过上限，禁止通行";
            case PH3 -> "磷化氢浓度超过上限，禁止通行";
        };
    }

    private record GasRule(DoorGasType gasType,
                           Long sensorDeviceId,
                           BigDecimal lowerThreshold,
                           BigDecimal upperThreshold,
                           String unit) {
    }

    private record GasValue(BigDecimal value, Long timestamp) {
    }
}
