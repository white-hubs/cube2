package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.config.LedBridgeConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.config.PlazaConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.iot.dto.LedScreenUpdateRequest;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.constants.ParkingPropCodes;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.provider.DeviceManageableGateway;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.ParkDeviceAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoDeviceRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
@EventBusListen(order = 1)
public class ParkingIotDeviceProGateway implements DeviceManageableGateway {

    private final ParkingInfoRepository parkingInfoRepository;

    private final ParkingInfoDeviceRepository parkingInfoDeviceRepository;

    private final FilterableDevicePropMsgHandler filterableDevicePropMsgHandler;

    private final ParkDeviceAppService parkDeviceAppService;

    private final SubscribeContainer subscribeContainer;

    private final RedisTemplate<String, Object> redisTemplate;

    private final RestTemplate restTemplate;

    private final DataDictionaryAdapter dataDictionaryAdapter;
    private static final String PARKING_IOT_DEVICE_SUBSCRIBE = "parking:iot:device:subscribe";

    private static final String GUIDE_SCREEN_SEND_URL = "/api/guide/screen/send";

    /**
     * 字典项编码（itemCode），LED中间层配置和车场引导屏配置共用
     */
    private static final String PARKING_GUIDE_LED_CONFIG_ITEM_CODE = "PARKING_GUIDE_LED_CONFIG";

    /**
     * LED中间层配置字典编码（code）
     */
    private static final String LED_BRIDGE_CONFIG_CODE = "LED_BRIDGE_CONFIG";

    /**
     * 车场引导屏配置字典编码（code）
     */
    private static final String PARKING_GUIDE_LED_CONFIG_CODE = "PARKING_GUIDE_LED_CONFIG";

    public ParkingIotDeviceProGateway(ParkingInfoRepository parkingInfoRepository,
                                      ParkingInfoDeviceRepository parkingInfoDeviceRepository, ParkDeviceAppService parkDeviceAppService,
                                      SubscribeContainer subscribeContainer,
                                      RedisTemplate<String, Object> redisTemplate,
                                      RestTemplate restTemplate,
                                      DataDictionaryAdapter dataDictionaryAdapter
                                     ) {
        this.parkingInfoRepository = parkingInfoRepository;
        this.parkingInfoDeviceRepository = parkingInfoDeviceRepository;
        this.parkDeviceAppService = parkDeviceAppService;
        this.subscribeContainer = subscribeContainer;
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
        this.dataDictionaryAdapter = dataDictionaryAdapter;
        this.filterableDevicePropMsgHandler = FilterableDevicePropMsgHandler.builder()
                .build(this::devicePropMsgHandler);
    }

    /**
     * 启动时注册订阅
     */
    @Subscribe
    @AllowConcurrentEvents
    public void registerFilterableDevicePropMsgHandler(SysReadEvent event) {
        //注册属性订阅消息处理
        this.subscribeContainer.registerDevicePropMsgHandler(this.filterableDevicePropMsgHandler);
        var devices = this.parkingInfoRepository.findDevicesByProviderCode(ParkingProviderEnum.DEVICE_CENTER.name());
        if (CollectionUtils.isEmpty(devices)) {
            return;
        }
        refreshDevicesSub(Collections.emptySet(), devices.stream()
                .map(ParkingInfoDevice::getDeviceId)
                .collect(Collectors.toSet()));
    }

    /**
     * 处理属性订阅消息
     *
     * @param msg 消息内容
     */
    public void devicePropMsgHandler(LivePropertyUpdateMsg msg) {
        //查询设备关联的车场信息
        var parkingInfos = this.parkingInfoRepository.findListByDeviceIds(Collections.singleton(Long.valueOf(msg.getDeviceId())));
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return;
        }
        //修改车位使用情况
        for (ParkingInfo parking : parkingInfos) {
            try {
                SessionContextHolder.requestTenant(parking.getTenant());
                //如用车场设备标签
                if (CollectionUtils.isEmpty(this.parkDeviceAppService.filterParkingTagDeviceIds(Collections.singleton(Long.valueOf(msg.getDeviceId()))))) {
                    log.warn("device {} is not parking tag device", msg.getDeviceId());
                    continue;
                }
                var parkingInfoDetail = this.parkingInfoRepository.findById(parking.getId());
                var totalInNum = 0;
                var updateParkingInfoDevice = new ParkingInfoDevice(Long.valueOf(msg.getDeviceId()));
                for (ParkingInfoDevice device : parkingInfoDetail.getDevices()) {
                    // 只更新消息中对应的设备
                    if (device.getDeviceId().toString().equals(msg.getDeviceId())) {
                        for (var prop : msg.getUpdates()) {
                            if (ParkingPropCodes.CURRENT_CAR_NUM.equals(prop.getKey())) {
                                var currentCarNumber = Integer.valueOf(prop.getValue().toString());
                                device.setCurrentCarNumber(currentCarNumber);
                                updateParkingInfoDevice.setCurrentCarNumber(currentCarNumber);
                            }
                        }
                    }
                    totalInNum += device.getCurrentCarNumber();
                }
                parkingInfoDetail.updateParkingSpace(totalInNum);
                this.parkingInfoRepository.updateParkingNumber(parkingInfoDetail);
                this.parkingInfoDeviceRepository.updateByDevices(Collections.singletonList(updateParkingInfoDevice));
                //发送车场信息到引导屏
                sendParkingInfoToGuideScreen(parkingInfoDetail);
            } catch (Exception e) {
                log.error("更新车场{}设备{}车位数量失败", parking.getCode(), msg.getDeviceId(), e);
            } finally {
                SessionContextHolder.clearSessionContext();
            }
        }
    }

    private void sendParkingInfoToGuideScreen(@NotNull ParkingInfo parkingInfoDetail) {
        try {
            // 1. 获取LED中间层配置
            var ledBridgeConfig = getLedBridgeConfig();
            if (ledBridgeConfig == null || StringUtils.isBlank(ledBridgeConfig.getUpdateContentUrl())) {
                log.warn("LED中间层配置不存在或URL为空，车场ID: {}", parkingInfoDetail.getId());
                return;
            }

            // 2. 获取车场引导屏配置
            var parkingLedConfigMap = getParkingLedConfig();
            if (MapUtils.isEmpty(parkingLedConfigMap)) {
                log.warn("车场引导屏配置不存在或为空，车场ID: {}", parkingInfoDetail.getId());
                return;
            }

            // 3. 收集所有屏幕配置下涉及到的车场ID，批量查询车场信息（剩余车位数）
            var allParkingIds = new ArrayList<Long>();
            for (var entry : parkingLedConfigMap.entrySet()) {
                var screenConfig = entry.getValue();
                if (screenConfig == null || CollectionUtils.isEmpty(screenConfig.getParkingInfo())) {
                    continue;
                }
                for (var p : screenConfig.getParkingInfo()) {
                    if (p == null || StringUtils.isBlank(p.getParkingId())) {
                        continue;
                    }
                    try {
                        allParkingIds.add(Long.parseLong(p.getParkingId()));
                    } catch (NumberFormatException e) {
                        log.warn("车场引导屏配置中的 parkingId 无法转换为Long，parkingId: {}", p.getParkingId());
                    }
                }
            }

            if (CollectionUtils.isEmpty(allParkingIds)) {
                log.debug("车场引导屏配置未配置任何车场ID，触发车场ID: {}", parkingInfoDetail.getId());
                return;
            }

            // 去重
            allParkingIds = allParkingIds.stream().distinct().collect(Collectors.toCollection(ArrayList::new));

            var parkingInfoPOList = parkingInfoRepository.lambdaQuery()
                    .in(ParkingInfoPO::getId, allParkingIds)
                    .list();

            var remainingSpaceMap = parkingInfoPOList.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(ParkingInfoPO::getId,
                                              p -> p.getTotalSpaceNum() == null ? 0 : p.getTotalSpaceNum(),
                                              (a, b) -> a));

            var totalNumMap = parkingInfoPOList.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(ParkingInfoPO::getId,
                                              p -> p.getTotalNum() == null ? 0 : p.getTotalNum(),
                                              (a, b) -> a));

            // 4. 按屏幕配置分组（一个屏幕配置调用一次），组装 areas 并调用中间层接口
            for (var entry : parkingLedConfigMap.entrySet()) {
                var screenName = entry.getKey();
                var screenConfig = entry.getValue();
                if (screenConfig == null) {
                    continue;
                }
                sendToBridge(ledBridgeConfig.getUpdateContentUrl(), new ScreenConfig(screenName, screenConfig), remainingSpaceMap, totalNumMap);
            }

        } catch (Exception e) {
            log.error("发送车场信息到引导屏失败，车场ID: {}", parkingInfoDetail.getId(), e);
        }
    }

    /**
     * 获取LED中间层配置
     */
    private LedBridgeConfig getLedBridgeConfig() {
        try {
            var param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(PARKING_GUIDE_LED_CONFIG_ITEM_CODE);
            param.setCode(LED_BRIDGE_CONFIG_CODE);
            var dictionaryDTO = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);

            Assert.isNotFound(dictionaryDTO == null,
                              String.format("未找到LED中间层配置字典，字典项编码: %s, 字典编码: %s",
                                            PARKING_GUIDE_LED_CONFIG_ITEM_CODE, LED_BRIDGE_CONFIG_CODE));

            var variableJson = dictionaryDTO.getVariable();
            Assert.isNotFound(StringUtils.isBlank(variableJson),
                              String.format("LED中间层配置字典的 variable 字段为空，字典项编码: %s, 字典编码: %s",
                                            PARKING_GUIDE_LED_CONFIG_ITEM_CODE, LED_BRIDGE_CONFIG_CODE));

            var config = JsonUtils.deserialize(variableJson, LedBridgeConfig.class);
            Assert.isNotFound(config == null, "解析LED中间层配置JSON失败，配置对象为空");

            return config;
        } catch (Exception e) {
            log.error("获取LED中间层配置失败，字典项编码: {}, 字典编码: {}",
                      PARKING_GUIDE_LED_CONFIG_ITEM_CODE, LED_BRIDGE_CONFIG_CODE, e);
            Assert.isNotFound(true, String.format("获取LED中间层配置失败: %s", e.getMessage()));
            return null;
        }
    }

    /**
     * 获取车场引导屏配置
     */
    private Map<String, PlazaConfig> getParkingLedConfig() {
        try {
            var param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(PARKING_GUIDE_LED_CONFIG_ITEM_CODE);
            param.setCode(PARKING_GUIDE_LED_CONFIG_CODE);
            var dictionaryDTO = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);

            Assert.isNotFound(dictionaryDTO == null,
                              String.format("未找到车场引导屏配置字典，字典项编码: %s, 字典编码: %s",
                                            PARKING_GUIDE_LED_CONFIG_ITEM_CODE, PARKING_GUIDE_LED_CONFIG_CODE));

            var variableJson = dictionaryDTO.getVariable();
            Assert.isNotFound(StringUtils.isBlank(variableJson),
                              String.format("车场引导屏配置字典的 variable 字段为空，字典项编码: %s, 字典编码: %s",
                                            PARKING_GUIDE_LED_CONFIG_ITEM_CODE, PARKING_GUIDE_LED_CONFIG_CODE));

            var configMap = JsonUtils.deserialize(variableJson, new TypeReference<Map<String, PlazaConfig>>() {});
            Assert.isNotFound(configMap == null, "解析车场引导屏配置JSON失败，配置对象为空");

            return configMap;
        } catch (Exception e) {
            log.error("获取车场引导屏配置失败，字典项编码: {}, 字典编码: {}",
                      PARKING_GUIDE_LED_CONFIG_ITEM_CODE, PARKING_GUIDE_LED_CONFIG_CODE, e);
            Assert.isNotFound(true, String.format("获取车场引导屏配置失败: %s", e.getMessage()));
            return null;
        }
    }

    /**
     * 解析内容模板中的占位符。
     * 支持：
     * 1. 简单占位符（当前车场）：${{parkingSpaceNum}}、${{parkingTotalNum}}
     * 2. 表达式占位符：${{parkingId[1].parkingSpaceNum+parkingId[2].parkingSpaceNum}}，
     *    表示取 parkingId 为 1 的剩余车位数 + parkingId 为 2 的剩余车位数；支持 parkingTotalNum，支持加减运算。
     */
    private String resolveContentTemplate(String contentTemplate, Long currentParkingId,
                                         Map<Long, Integer> remainingSpaceMap, Map<Long, Integer> totalNumMap) {
        var placeholderPattern = Pattern.compile("\\$\\{\\{([^}]+)\\}\\}");
        var matcher = placeholderPattern.matcher(contentTemplate);
        var sb = new StringBuilder();
        while (matcher.find()) {
            var inner = matcher.group(1).trim();
            var replacement = resolvePlaceholder(inner, currentParkingId, remainingSpaceMap, totalNumMap);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 解析单个占位符内容：简单名称或表达式（如 parkingId[1].parkingSpaceNum+parkingId[2].parkingSpaceNum）
     */
    private String resolvePlaceholder(String inner, Long currentParkingId,
                                      Map<Long, Integer> remainingSpaceMap, Map<Long, Integer> totalNumMap) {
        if ("parkingSpaceNum".equals(inner)) {
            return String.valueOf(remainingSpaceMap.getOrDefault(currentParkingId, 0));
        }
        if ("parkingTotalNum".equals(inner)) {
            return String.valueOf(totalNumMap.getOrDefault(currentParkingId, 0));
        }
        // 表达式： (可选正负号) parkingId[数字].parkingSpaceNum 或 parkingTotalNum，用 + - 连接
        // 示例：parkingId[1].parkingSpaceNum+parkingId[2].parkingSpaceNum
        // 允许运算符和项两侧有空格
        var termPattern = Pattern.compile("([+-])?\\s*parkingId\\[(\\d+)\\]\\.(parkingSpaceNum|parkingTotalNum)");
        var termMatcher = termPattern.matcher(inner);
        var sum = 0;
        while (termMatcher.find()) {
            var signStr = termMatcher.group(1);
            var sign = (signStr == null || "+".equals(signStr)) ? 1 : -1;
            var parkingId = Long.parseLong(termMatcher.group(2));
            var field = termMatcher.group(3);
            var value = "parkingSpaceNum".equals(field)
                    ? remainingSpaceMap.getOrDefault(parkingId, 0)
                    : totalNumMap.getOrDefault(parkingId, 0);
            sum += sign * value;
        }
        return String.valueOf(sum);
    }

    private void sendToBridge(String updateContentUrl, ScreenConfig screenConfig, Map<Long, Integer> remainingSpaceMap,
                              Map<Long, Integer> totalNumMap
                             ) {
        try {
            if (screenConfig == null || screenConfig.getScreenConfig() == null) {
                return;
            }

            var url = screenConfig.getScreenConfig().getUrl();
            Assert.isNotFound(StringUtils.isBlank(url),
                              String.format("引导屏车场配置中的url为空，screenName: %s", screenConfig.getScreenName()));

            var request = new LedScreenUpdateRequest();
            request.setControllerUrl(url);
            request.setUsername("guest");
            request.setPassword("guest");
            request.setDisplayMode(screenConfig.getScreenConfig().getDisplayMode());
            request.setClearScreen(screenConfig.getScreenConfig().getClearScreen());

            var areas = new ArrayList<LedScreenUpdateRequest.AreaInfo>();
            for (var p : screenConfig.getScreenConfig().getParkingInfo()) {
                if (p == null) {
                    continue;
                }
                if (StringUtils.isBlank(p.getLedAreaId())) {
                    continue;
                }

                Integer areaId;
                try {
                    areaId = Integer.parseInt(p.getLedAreaId());
                } catch (NumberFormatException e) {
                    log.warn("ledAreaId无法转换为Integer，ledAreaId: {}", p.getLedAreaId());
                    continue;
                }

                var area = new LedScreenUpdateRequest.AreaInfo();
                area.setAreaId(areaId);

                // 如果没有parkingId，直接使用contentTemplate作为内容
                if (StringUtils.isBlank(p.getParkingId())) {
                    if (StringUtils.isNotBlank(p.getContentTemplate())) {
                        area.setContent(p.getContentTemplate());
                    } else {
                        log.warn("没有parkingId且没有contentTemplate，跳过该区域，areaId: {}", areaId);
                        continue;
                    }
                } else {
                    // 有parkingId的情况，进行模板替换或使用剩余车位数
                    Long parkingId;
                    try {
                        parkingId = Long.parseLong(p.getParkingId());
                    } catch (NumberFormatException e) {
                        log.warn("parkingId无法转换为Long，parkingId: {}", p.getParkingId());
                        continue;
                    }

                    // 如果有内容模板，使用模板替换；否则使用剩余车位数
                    if (StringUtils.isNotBlank(p.getContentTemplate())) {
                        var content = resolveContentTemplate(
                                p.getContentTemplate(), parkingId, remainingSpaceMap, totalNumMap);
                        area.setContent(content);
                    } else {
                        area.setContent(String.valueOf(remainingSpaceMap.getOrDefault(parkingId, 0)));
                    }
                }
                areas.add(area);
            }

            if (CollectionUtils.isEmpty(areas)) {
                log.debug("屏幕未组装出任何areas，screenName: {}, controllerUrl: {}", screenConfig.getScreenName(), request.getControllerUrl());
                return;
            }
            request.setAreas(areas);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var httpEntity = new HttpEntity<>(request, headers);

            log.info("调用中间层更新引导屏，updateUrl: {}, screenName: {}, controllerUrl: {}, areasSize: {}",
                     updateContentUrl, screenConfig.getScreenName(), request.getControllerUrl(), areas.size());

            ResponseEntity<String> response = restTemplate.postForEntity(updateContentUrl, httpEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("调用中间层成功，screenName: {}, controllerUrl: {}", screenConfig.getScreenName(), request.getControllerUrl());
            } else {
                log.warn("调用中间层失败，screenName: {}, controllerUrl: {}, status: {}",
                         screenConfig.getScreenName(), request.getControllerUrl(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("调用中间层异常，screenName: {}", screenConfig.getScreenName(), e);
        }
    }

    /**
     * 屏幕配置
     */
    @Getter
    private static class ScreenConfig {
        private final String screenName;
        private final PlazaConfig screenConfig;

        public ScreenConfig(String screenName, PlazaConfig screenConfig) {
            this.screenName = screenName;
            this.screenConfig = screenConfig;
        }

    }

    /**
     * 刷新设备订阅
     */
    @Override
    public synchronized void refreshDevices() {
        var subDeviceIds = redisTemplate.opsForHash().keys(PARKING_IOT_DEVICE_SUBSCRIBE).stream()
                .map(Object::toString)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        var configs = this.parkingInfoRepository.findHasDockConfigsByProviderCode(ParkingProviderEnum.DEVICE_CENTER.name());
        if (CollectionUtils.isEmpty(configs)) {
            refreshDevicesSub(subDeviceIds, Collections.emptySet());
            return;
        }
        var parkingIds = configs.stream()
                .map(ParkingInfo::getId)
                .collect(Collectors.toList());
        var all = this.parkingInfoDeviceRepository.findListByParkingIds(parkingIds);
        refreshDevicesSub(subDeviceIds, all.stream()
                .map(ParkingInfoDevice::getDeviceId)
                .collect(Collectors.toSet()));
    }

    private void refreshDevicesSub(Set<Long> removeDeviceIds, Set<Long> addDeviceIds) {
        if (CollectionUtils.isNotEmpty(removeDeviceIds)) {
            //删除设备订阅
            var ids = removeDeviceIds.stream().map(String::valueOf).collect(Collectors.toSet());
            filterableDevicePropMsgHandler.removeDeviceIds(ids);
            redisTemplate.opsForHash().delete(PARKING_IOT_DEVICE_SUBSCRIBE, (Object[]) ids.toArray(new String[0]));
        }
        if (CollectionUtils.isNotEmpty(addDeviceIds)) {
            //注册设备订阅
            filterableDevicePropMsgHandler.addDeviceIds(addDeviceIds.stream().map(String::valueOf).collect(Collectors.toSet()));
            filterableDevicePropMsgHandler.addParamCode(ParkingPropCodes.CURRENT_CAR_NUM);
            //更新订阅设备
            redisTemplate.opsForHash().putAll(PARKING_IOT_DEVICE_SUBSCRIBE, addDeviceIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toMap(x -> x, x -> x)));
        }
    }

    @Override
    public ParkingProviderEnum getProvider() {
        return ParkingProviderEnum.DEVICE_CENTER;
    }

    @Override
    public void activate(ParkingInfo parkingInfo) {
        if (CollectionUtils.isEmpty(parkingInfo.getDevices())) {
            parkingInfo.updateParkingSpace(0);
            return;
        }
        var multiProp = this.parkDeviceAppService.getDeviceSinglePropValue(parkingInfo.getDevices().stream()
                                                                                   .map(ParkingInfoDevice::getDeviceId)
                                                                                   .collect(Collectors.toSet()), ParkingPropCodes.CURRENT_CAR_NUM);
        if (MapUtils.isEmpty(multiProp)) {
            refreshDevices();
            parkingInfo.updateParkingSpace(0);
            return;
        }
        var totalInNum = 0;
        for (var device : parkingInfo.getDevices()) {
            if (!multiProp.containsKey(device.getDeviceId())) {
                continue;
            }
            var value = multiProp.get(device.getDeviceId());
            device.setCurrentCarNumber(value);
            totalInNum += device.getCurrentCarNumber();
        }
        parkingInfo.updateParkingSpace(totalInNum);
        refreshDevices();
    }

    @Override
    public void refreshParkingInfo(ParkingInfo parkingInfo) {
        activate(parkingInfo);
    }

    @Override
    public void deactivate(ParkingInfo parkingInfo) {
        refreshDevices();
    }

}
