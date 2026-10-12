package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.DoorGasLinkageConfigMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.DoorGasLinkageConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.DoorGasType;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.DoorGasLinkageConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorDeviceResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasConfigRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasConfigResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigDetailResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigSaveRequest;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 门禁气体联动配置业务实现。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DoorGasLinkageConfigServiceImpl
        extends ServiceImpl<DoorGasLinkageConfigMapper, DoorGasLinkageConfig>
        implements DoorGasLinkageConfigService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 1000;
    private static final int DEFAULT_DATA_VALID_SECONDS = 60;
    private static final String DOOR_APPLICATION_CODE = "DOORMANAGE";

    private final DeviceCenterStdClient deviceCenterStdClient;
    private final SecurityDeviceClient securityDeviceClient;

    @Override
    public Pagination<DoorGasLinkageConfigPageResponse> page(DoorGasLinkageConfigPageRequest request) {
        int pageNumber = request == null || request.pageNumber() == null
                         ? DEFAULT_PAGE_NUMBER : request.pageNumber();
        int pageSize = request == null || request.pageSize() == null
                       ? DEFAULT_PAGE_SIZE : request.pageSize();
        Assert.isParameterError(pageNumber < 1, "页码必须大于0");
        Assert.isParameterError(pageSize < 1 || pageSize > MAX_PAGE_SIZE, "每页条数必须在1到1000之间");

        Long areaId = request == null ? null : request.areaId();
        Boolean gasLinkageEnabled = request == null ? null : request.gasLinkageEnabled();
        List<Long> matchedDeviceIds = gasLinkageEnabled == null
                                      ? null : loadMatchedDoorDeviceIds(gasLinkageEnabled);
        log.info("查询门禁气体联动配置分页，pageNumber={}，pageSize={}，areaId={}，gasLinkageEnabled={}，matchedDeviceIdCount={}",
                 pageNumber, pageSize, areaId, gasLinkageEnabled,
                 matchedDeviceIds == null ? null : matchedDeviceIds.size());
        if (matchedDeviceIds != null && matchedDeviceIds.isEmpty()) {
            log.info("门禁气体联动配置分页无匹配设备，gasLinkageEnabled={}", gasLinkageEnabled);
            return Pagination.empty();
        }
        return toPageResponse(querySecurityDevicePage(pageNumber, pageSize, areaId, matchedDeviceIds));
    }

    @Override
    public DoorGasLinkageConfigDetailResponse detail(Long doorDeviceId) {
        Assert.isParameterError(doorDeviceId == null, MsgBinding.DOOR_GAS_DEVICE_ID_REQUIRED);
        log.info("查询门禁气体联动配置详情，doorDeviceId={}", doorDeviceId);
        DeviceBaseDetailsDTO door = requireDevice(doorDeviceId);
        DoorGasLinkageConfig config = lambdaQuery()
                .eq(DoorGasLinkageConfig::getDoorDeviceId, doorDeviceId)
                .one();
        if (config == null) {
            log.info("门禁气体联动配置不存在，返回默认详情，doorDeviceId={}", doorDeviceId);
            return new DoorGasLinkageConfigDetailResponse(
                    null,
                    doorDeviceId,
                    toDoorResponse(door),
                    false,
                    DEFAULT_DATA_VALID_SECONDS,
                    List.of(),
                    null);
        }

        List<GasConfigValue> gasConfigs = parseGasConfigs(config.getDeviceConfigJson());
        Map<Long, DeviceBaseDetailsDTO> sensorById = loadDeviceDetails(gasConfigs.stream()
                                                                               .map(GasConfigValue::sensorDeviceId)
                                                                               .filter(Objects::nonNull)
                                                                               .collect(Collectors.toSet()));
        return new DoorGasLinkageConfigDetailResponse(
                config.getId(),
                doorDeviceId,
                toDoorResponse(door),
                Boolean.TRUE.equals(config.getGasLinkageEnabled()),
                defaultValidSeconds(config.getDataValidSeconds()),
                gasConfigs.stream().map(item -> toGasResponse(item, sensorById.get(item.sensorDeviceId()))).toList(),
                config.getUpdateTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveConfig(Long doorDeviceId, DoorGasLinkageConfigSaveRequest request) {
        Assert.isParameterError(doorDeviceId == null, MsgBinding.DOOR_GAS_DEVICE_ID_REQUIRED);
        Assert.isParameterError(request == null, MsgBinding.DOOR_GAS_CONFIG_REQUIRED);
        log.info("保存门禁气体联动配置，doorDeviceId={}，gasLinkageEnabled={}，gasConfigCount={}，dataValidSeconds={}",
                 doorDeviceId, request.gasLinkageEnabled(),
                 request.gasConfigs() == null ? 0 : request.gasConfigs().size(), request.dataValidSeconds());
        requireDevice(doorDeviceId);

        boolean enabled = Boolean.TRUE.equals(request.gasLinkageEnabled());
        int validSeconds = request.dataValidSeconds() == null
                           ? DEFAULT_DATA_VALID_SECONDS : request.dataValidSeconds();
        Assert.isParameterError(validSeconds <= 0, MsgBinding.DOOR_GAS_VALID_SECONDS_INVALID);

        List<DoorGasConfigRequest> incoming = request.gasConfigs() == null ? List.of() : request.gasConfigs();
        Assert.isParameterError(enabled && incoming.isEmpty(), MsgBinding.DOOR_GAS_SENSOR_REQUIRED);
        validateGasConfigs(incoming);
        validateSensorDevices(incoming);

        DoorGasLinkageConfig config = lambdaQuery()
                .eq(DoorGasLinkageConfig::getDoorDeviceId, doorDeviceId)
                .one();
        boolean created = config == null;
        if (config == null) {
            config = new DoorGasLinkageConfig();
            config.setId(IdWorker.getId());
            config.setDoorDeviceId(doorDeviceId);
        }
        config.setGasLinkageEnabled(enabled);
        config.setDataValidSeconds(validSeconds);
        config.setDeviceConfigJson(JsonUtils.serialize(incoming.stream()
                                                               .map(this::toStoredConfig)
                                                               .toList()));
        if (created) {
            save(config);
        } else {
            updateById(config);
        }
        log.info("门禁气体联动配置保存成功，doorDeviceId={}，created={}，gasLinkageEnabled={}，gasConfigCount={}",
                 doorDeviceId, created, enabled, incoming.size());
        return true;
    }

    private Map<Long, DoorGasLinkageConfig> loadConfigsByDeviceId(Set<Long> deviceIds) {
        if (deviceIds.isEmpty()) {
            return Map.of();
        }
        return list(Wrappers.<DoorGasLinkageConfig>lambdaQuery()
                            .in(DoorGasLinkageConfig::getDoorDeviceId, deviceIds))
                .stream()
                .collect(Collectors.toMap(DoorGasLinkageConfig::getDoorDeviceId,
                                          Function.identity(), (left, right) -> right));
    }

    private List<Long> loadMatchedDoorDeviceIds(Boolean gasLinkageEnabled) {
        if (Boolean.TRUE.equals(gasLinkageEnabled)) {
            return loadConfiguredDoorDeviceIds(true);
        }

        List<Long> allDoorDeviceIds = securityDeviceClient.getSecurityDeviceIds(DOOR_APPLICATION_CODE);
        Set<Long> enabledDoorDeviceIds = new HashSet<>(loadConfiguredDoorDeviceIds(true));
        List<Long> matchedDeviceIds = allDoorDeviceIds == null ? List.of() : allDoorDeviceIds.stream()
                .filter(Objects::nonNull)
                .filter(deviceId -> !enabledDoorDeviceIds.contains(deviceId))
                .distinct()
                .toList();
        log.info("按未启用状态筛选门禁设备，allDoorDeviceIdCount={}，enabledDoorDeviceIdCount={}，matchedDeviceIdCount={}",
                 allDoorDeviceIds == null ? 0 : allDoorDeviceIds.size(), enabledDoorDeviceIds.size(),
                 matchedDeviceIds.size());
        return matchedDeviceIds;
    }

    private List<Long> loadConfiguredDoorDeviceIds(boolean gasLinkageEnabled) {
        return list(Wrappers.<DoorGasLinkageConfig>lambdaQuery()
                            .select(DoorGasLinkageConfig::getDoorDeviceId)
                            .eq(DoorGasLinkageConfig::getGasLinkageEnabled, gasLinkageEnabled)
                            .orderByAsc(DoorGasLinkageConfig::getDoorDeviceId))
                .stream()
                .map(DoorGasLinkageConfig::getDoorDeviceId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private Pagination<SecurityDeviceVo> querySecurityDevicePage(int pageNumber,
                                                                 int pageSize,
                                                                 Long areaId,
                                                                 List<Long> matchedDeviceIds
                                                                ) {
        String areaIdParam = areaId == null ? null : String.valueOf(areaId);
        log.info("调用门禁关联设备分页接口，applicationCode={}，pageNumber={}，pageSize={}，areaId={}，deviceIdFilterCount={}",
                 DOOR_APPLICATION_CODE, pageNumber, pageSize, areaIdParam,
                 matchedDeviceIds == null ? null : matchedDeviceIds.size());
        Pagination<SecurityDeviceVo> result = securityDeviceClient.getSecurityDevicePage(
                DOOR_APPLICATION_CODE, pageNumber, pageSize, areaIdParam, matchedDeviceIds);
        log.info("门禁关联设备分页接口返回，total={}，recordCount={}",
                 result == null ? null : result.getTotal(),
                 result == null || result.getRecords() == null ? null : result.getRecords().size());
        return result;
    }

    private Pagination<DoorGasLinkageConfigPageResponse> toPageResponse(Pagination<SecurityDeviceVo> devicePage) {
        if (devicePage == null || devicePage.getRecords() == null || devicePage.getRecords().isEmpty()) {
            return Pagination.empty();
        }
        Set<Long> pageDeviceIds = devicePage.getRecords().stream()
                .map(SecurityDeviceVo::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DoorGasLinkageConfig> configByDeviceId = loadConfigsByDeviceId(pageDeviceIds);
        List<DoorGasLinkageConfigPageResponse> records = devicePage.getRecords().stream()
                .map(device -> toPageResponse(device, configByDeviceId.get(device.getDeviceId())))
                .filter(Objects::nonNull)
                .toList();
        return new Pagination<>(devicePage.getTotal(), devicePage.getPages(), records);
    }

    private DoorGasLinkageConfigPageResponse toPageResponse(SecurityDeviceVo device,
                                                            DoorGasLinkageConfig config
                                                           ) {
        if (device == null || device.getDeviceId() == null) {
            return null;
        }
        return new DoorGasLinkageConfigPageResponse(
                config == null ? null : config.getId(),
                device.getDeviceId(),
                device.getDeviceName(),
                device.getCode(),
                device.getAreaName(),
                isGasLinkageEnabled(config),
                config == null ? DEFAULT_DATA_VALID_SECONDS : defaultValidSeconds(config.getDataValidSeconds()),
                config == null ? List.of() : parseGasConfigs(config.getDeviceConfigJson()).stream()
                                             .map(GasConfigValue::gasType)
                                             .filter(Objects::nonNull)
                                             .toList(),
                config == null ? null : config.getUpdateTime());
    }

    private boolean isGasLinkageEnabled(DoorGasLinkageConfig config) {
        return config != null && Boolean.TRUE.equals(config.getGasLinkageEnabled());
    }

    private DeviceBaseDetailsDTO requireDevice(Long deviceId) {
        DeviceBaseDetailsDTO device = loadDeviceDetails(Set.of(deviceId)).get(deviceId);
        log.info("校验门禁设备归属，doorDeviceId={}，deviceFound={}", deviceId, device != null);
        if (device == null) {
            log.warn("门禁设备不存在或不属于当前租户，doorDeviceId={}", deviceId);
        }
        Assert.isNotFound(device == null, MsgBinding.DOOR_GAS_DEVICE_NOT_FOUND);
        return device;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetails(Set<Long> deviceIds) {
        if (deviceIds.isEmpty()) {
            return Map.of();
        }
        log.info("调用设备中心查询设备详情，deviceIdCount={}", deviceIds.size());
        List<DeviceBaseDetailsDTO> details = deviceCenterStdClient.getDetailsListByDeviceId(deviceIds);
        log.info("设备中心设备详情查询返回，requestedDeviceIdCount={}，returnedDeviceCount={}",
                 deviceIds.size(), details == null ? 0 : details.size());
        if (details == null) {
            return Map.of();
        }
        return details.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(DeviceBaseDetailsDTO::getId,
                                          Function.identity(), (left, right) -> right));
    }

    private DoorDeviceResponse toDoorResponse(DeviceBaseDetailsDTO device) {
        return new DoorDeviceResponse(device.getDeviceName(),
                                      device.getDeviceCode(),
                                      device.getAreaId(),
                                      device.getAreaName(),
                                      device.getProductCategoryName());
    }

    private DoorGasConfigResponse toGasResponse(GasConfigValue config, DeviceBaseDetailsDTO sensor) {
        return new DoorGasConfigResponse(
                config.gasType(),
                config.sensorDeviceId(),
                sensor == null ? null : sensor.getDeviceName(),
                config.gasType() == DoorGasType.O2 ? config.lowerThreshold() : null,
                config.upperThreshold(),
                config.unit(),
                config.sensorDeviceId() != null);
    }

    private List<GasConfigValue> parseGasConfigs(String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }
        try {
            List<GasConfigValue> result = JsonUtils.deserialize(json, new TypeReference<List<GasConfigValue>>() {});
            return result == null ? List.of() : result;
        } catch (RuntimeException exception) {
            throw new ServiceException(MsgBinding.DOOR_GAS_JSON_INVALID, exception);
        }
    }

    private void validateGasConfigs(List<DoorGasConfigRequest> configs) {
        Set<DoorGasType> gasTypes = new HashSet<>();
        for (DoorGasConfigRequest config : configs) {
            Assert.isParameterError(config == null, MsgBinding.DOOR_GAS_CONFIG_REQUIRED);
            Assert.isParameterError(config.gasType() == null, MsgBinding.DOOR_GAS_TYPE_REQUIRED);
            Assert.isParameterError(!gasTypes.add(config.gasType()), MsgBinding.DOOR_GAS_CONFIG_DUPLICATED);
            if (config.sensorDeviceId() == null) {
                Assert.isParameterError(config.lowerThreshold() != null || config.upperThreshold() != null
                                        || StringUtils.isNotBlank(config.unit()), MsgBinding.DOOR_GAS_THRESHOLD_UNSUPPORTED);
                continue;
            }
            Assert.isParameterError(config.upperThreshold() == null, MsgBinding.DOOR_GAS_THRESHOLD_REQUIRED);
            Assert.isParameterError(config.gasType() == DoorGasType.O2 && config.lowerThreshold() == null,
                                    MsgBinding.DOOR_GAS_THRESHOLD_REQUIRED);
            Assert.isParameterError(config.gasType() != DoorGasType.O2 && config.lowerThreshold() != null,
                                    MsgBinding.DOOR_GAS_THRESHOLD_UNSUPPORTED);
            if (config.gasType() == DoorGasType.O2) {
                Assert.isParameterError(config.lowerThreshold().compareTo(config.upperThreshold()) >= 0,
                                        MsgBinding.DOOR_GAS_THRESHOLD_ORDER_INVALID);
            }
            Assert.isParameterError(StringUtils.isBlank(config.unit()), MsgBinding.DOOR_GAS_UNIT_REQUIRED);
        }
    }

    private void validateSensorDevices(List<DoorGasConfigRequest> configs) {
        Set<Long> sensorIds = configs.stream()
                .map(DoorGasConfigRequest::sensorDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> sensorById = loadDeviceDetails(sensorIds);
        for (DoorGasConfigRequest config : configs) {
            if (config.sensorDeviceId() == null) {
                continue;
            }
            DeviceBaseDetailsDTO sensor = sensorById.get(config.sensorDeviceId());
            Assert.isParameterError(sensor == null, MsgBinding.DOOR_GAS_SENSOR_NOT_FOUND);
        }
    }

    private GasConfigValue toStoredConfig(DoorGasConfigRequest request) {
        return new GasConfigValue(request.gasType(), request.sensorDeviceId(), request.lowerThreshold(),
                                  request.upperThreshold(), request.unit());
    }

    private int defaultValidSeconds(Integer value) {
        return value == null ? DEFAULT_DATA_VALID_SECONDS : value;
    }

    private record GasConfigValue(DoorGasType gasType,
                                  Long sensorDeviceId,
                                  BigDecimal lowerThreshold,
                                  BigDecimal upperThreshold,
                                  String unit) {
    }
}
