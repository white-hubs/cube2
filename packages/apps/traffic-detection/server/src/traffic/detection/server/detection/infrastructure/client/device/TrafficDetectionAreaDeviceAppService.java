package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.dto.TrafficDetectionDevicePropDTO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.query.DeviceFilterPageQuery;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiHistorySampleParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrafficDetectionAreaDeviceAppService {

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    private final IotDevicePropClient iotDevicePropClient;

    private static final String TRAFFIC_DETECTION_APP_TAG_CODE = "builtin.app::TRAFFIC_DETECTION";

    public TrafficDetectionAreaDeviceAppService(DeviceCenterStdClient deviceCenterStdClient,
                                                DeviceCenterTagStdClient deviceCenterTagStdClient, IotDevicePropClient iotDevicePropClient
                                               ) {
        this.deviceCenterStdClient = deviceCenterStdClient;
        this.deviceCenterTagStdClient = deviceCenterTagStdClient;
        this.iotDevicePropClient = iotDevicePropClient;
    }

    public Pagination<DevicePageInfoVO> queryDevicePageInfo(DeviceFilterPageQuery query) {
        DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setKeyword(query.getKeyword());
        deviceFilterPageParam.setProductId(query.getProductId());
        deviceFilterPageParam.setCategoryCode(query.getCategoryCode());
        deviceFilterPageParam.setTagCodes(Collections.singletonList(TRAFFIC_DETECTION_APP_TAG_CODE));
        if (CollectionUtils.isNotEmpty(query.getMatchedDeviceIds())) {
            deviceFilterPageParam.setMatchedDeviceIds(new ArrayList<>(query.getMatchedDeviceIds()));
        }
        deviceFilterPageParam.setFilterAreaIds(query.getFilterAreaIds());
        deviceFilterPageParam.setAnyMatchedAreaId(query.getAnyMatchedAreaId());
        deviceFilterPageParam.setAnyAreaId(query.getAnyAreaId());
        deviceFilterPageParam.setAnyMatchedDepartmentId(query.getAnyMatchedDepartmentId());
        deviceFilterPageParam.setDeviceState(query.getDeviceState());
        deviceFilterPageParam.setPageNumber(query.getPageNumber());
        deviceFilterPageParam.setPageSize(query.getPageSize());
        return deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
    }

    /**
     * 查询监控设备列表
     *
     * @return
     */
    public List<DeviceBaseArchivesInfoDTO> queryTrafficDetectionDevices() {
        return deviceCenterStdClient.getListByTag(Collections.singleton(TRAFFIC_DETECTION_APP_TAG_CODE));
    }

    /**
     * 获取多设备单属性值总和
     *
     * @param deviceIds 设备ID列表
     * @param propCode  属性编码
     *
     * @return 多设备单属性值总和
     */
    public Integer getDeviceSinglePropValueSum(Set<Long> deviceIds, String propCode) {
        return getDeviceSinglePropValue(deviceIds, propCode).values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * 过滤拥有人流检测设备标签的设备
     *
     * @param deviceIds 设备ID列表
     *
     * @return 有人流检测设备标签的设备ID列表
     */
    public Set<Long> filterTrafficDetectionTagDeviceIds(Set<Long> deviceIds) {
        Map<Long, List<CustomerTagVO>> deviceAllTagMap = this.deviceCenterTagStdClient.deviceAllTag(deviceIds);
        return deviceIds.stream().filter(deviceId -> deviceAllTagMap.containsKey(deviceId) &&
                                                     deviceAllTagMap.get(deviceId).stream()
                                                             .anyMatch(tag -> tag.getTagCode().equals(TRAFFIC_DETECTION_APP_TAG_CODE)))
                .collect(Collectors.toSet());
    }

    /**
     * 获取多设备单属性值
     *
     * @param deviceIds 设备ID列表
     * @param propCode  属性编码
     *
     * @return 多设备单属性值
     */
    public Map<Long, Integer> getDeviceSinglePropValue(Set<Long> deviceIds, String propCode) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        var deviceIdSet = filterTrafficDetectionTagDeviceIds(deviceIds);
        var multiDevicePropParam = new MultiDevicePropParam();
        multiDevicePropParam.setNotBeforeTs(-1L);
        multiDevicePropParam.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        var multiProp = this.iotDevicePropClient.multiProp(multiDevicePropParam);
        if (MapUtils.isEmpty(multiProp)) {
            return Collections.emptyMap();
        }
        var propValueMap = new HashMap<Long, Integer>();
        for (var deviceId : multiProp.keySet()) {
            for (var prop : multiProp.get(deviceId)) {
                if (propCode.equals(prop.getKey())) {
                    var currentPersonNumber = Integer.valueOf(prop.getValue().toString());
                    propValueMap.put(Long.valueOf(deviceId), currentPersonNumber);
                }
            }
        }
        return propValueMap;
    }

    /**
     * 获取多设备单属性值历史平均值
     *
     * @param deviceIds 设备ID列表
     * @param propCode  属性编码
     * @param period    时间范围
     *
     * @return 多设备单属性值历史平均值
     */
    public Map<Long, Integer> getDevicesSinglePropHistoryAvgValue(Set<Long> deviceIds, String propCode, Period period) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        var deviceIdSet = filterTrafficDetectionTagDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceIdSet)) {
            return Collections.emptyMap();
        }
        var param = new MultiHistorySampleParam();
        param.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        param.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        param.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        param.setStep(1);
        param.setAggType("avg");
        var deviceHistorySamplePropMap = iotDevicePropClient.multiHistorySampleProp(param);
        if (MapUtils.isEmpty(deviceHistorySamplePropMap)) {
            return Collections.emptyMap();
        }
        var propValueMap = new HashMap<Long, Integer>();
        for (var deviceId : deviceHistorySamplePropMap.keySet()) {
            var deviceTSDTO = deviceHistorySamplePropMap.get(deviceId);
            if (CollectionUtils.isEmpty(deviceTSDTO.getPropertyTsBoList())) {
                continue;
            }
            for (var prop : deviceTSDTO.getPropertyTsBoList()) {
                if (propCode.equals(prop.getPropertyCode())) {
                    var currentPersonNumber = Integer.valueOf(prop.getValueTsBoList().getFirst().getValue().toString());
                    propValueMap.put(Long.valueOf(deviceId), currentPersonNumber);
                }
            }
        }
        return propValueMap;
    }

    /**
     * 获取多设备单属性值历史求和值
     *
     * @param deviceIds 设备ID列表
     * @param propCode  属性编码
     * @param period    时间范围
     *
     * @return 多设备单属性值历史求和值
     */
    public Map<Long, Integer> getDevicesSinglePropHistorySumValue(Set<Long> deviceIds, String propCode, Period period) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        var deviceIdSet = filterTrafficDetectionTagDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceIdSet)) {
            return Collections.emptyMap();
        }
        var param = new MultiHistorySampleParam();
        param.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        param.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        param.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        param.setStep(60);
        param.setAggType("sum");
        var deviceHistorySamplePropMap = iotDevicePropClient.multiHistorySampleProp(param);
        if (MapUtils.isEmpty(deviceHistorySamplePropMap)) {
            return Collections.emptyMap();
        }
        var propValueMap = new HashMap<Long, Integer>();
        for (var deviceId : deviceHistorySamplePropMap.keySet()) {
            var deviceTSDTO = deviceHistorySamplePropMap.get(deviceId);
            if (CollectionUtils.isEmpty(deviceTSDTO.getPropertyTsBoList())) {
                continue;
            }
            for (var prop : deviceTSDTO.getPropertyTsBoList()) {
                if (propCode.equals(prop.getPropertyCode())) {
                    var currentPersonNumber = Integer.valueOf(prop.getValueTsBoList().getFirst().getValue().toString());
                    propValueMap.put(Long.valueOf(deviceId), currentPersonNumber);
                }
            }
        }
        return propValueMap;
    }

    public List<TrafficDetectionDevicePropDTO> getTrafficDetectionDevicePropList(Set<Long> deviceIds, String propCode) {
        var appDevicesMap = queryTrafficDetectionDevices().stream().collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, n -> n));
        if (MapUtils.isEmpty(appDevicesMap)) {
            return Collections.emptyList();
        }
        var deviceIdSet = appDevicesMap.keySet().stream()
                .filter(deviceIds::contains)
                .collect(Collectors.toSet());
        var multiDevicePropParam = new MultiDevicePropParam();
        multiDevicePropParam.setNotBeforeTs(-1L);
        multiDevicePropParam.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        var multiProp = this.iotDevicePropClient.multiProp(multiDevicePropParam);
        if (MapUtils.isEmpty(multiProp)) {
            return getDefaultTrafficDetectionDevicePropDTOS(deviceIds, appDevicesMap);
        }
        var trafficDetectionDevicePropDTOMap = new ArrayList<TrafficDetectionDevicePropDTO>();
        for (var deviceId : deviceIdSet) {
            var propValue = 0;
            for (var prop : multiProp.get(deviceId.toString())) {
                if (!propCode.equals(prop.getKey())) {
                    continue;
                }
                propValue = Integer.parseInt(prop.getValue().toString());
            }
            var appDevice = appDevicesMap.get(deviceId);
            trafficDetectionDevicePropDTOMap.add(new TrafficDetectionDevicePropDTO(deviceId,
                                                                                   appDevice.getName(),
                                                                                   propValue));
        }
        return trafficDetectionDevicePropDTOMap;
    }

    /**
     * 获取多设备单属性值历史平均值列表
     *
     * @param deviceIds 设备ID列表
     * @param propCode  属性编码
     * @param period    时间范围
     *
     * @return 多设备单属性值历史平均值列表
     */
    public List<TrafficDetectionDevicePropDTO> getTrafficDetectionDevicePropHistoryAvgValueList(Set<Long> deviceIds, String propCode,
                                                                                                Period period
                                                                                               ) {
        var appDevicesMap = queryTrafficDetectionDevices().stream().collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, n -> n));
        if (MapUtils.isEmpty(appDevicesMap)) {
            return Collections.emptyList();
        }
        var deviceIdSet = appDevicesMap.keySet().stream()
                .filter(deviceIds::contains)
                .collect(Collectors.toSet());
        var multiProp = getDeviceTSHistoriesMap(propCode, period, deviceIdSet, 1);
        if (MapUtils.isEmpty(multiProp)) {
            return getDefaultTrafficDetectionDevicePropDTOS(deviceIds, appDevicesMap);
        }
        var trafficDetectionDevicePropDTOMap = new ArrayList<TrafficDetectionDevicePropDTO>();
        for (var deviceId : deviceIdSet) {
            var propValue = 0;
            var appDevice = appDevicesMap.get(deviceId);
            if (multiProp.containsKey(deviceId.toString())) {
                var deviceTSDTO = multiProp.get(deviceId.toString());
                if (CollectionUtils.isEmpty(deviceTSDTO.getPropertyTsBoList())) {
                    trafficDetectionDevicePropDTOMap.add(new TrafficDetectionDevicePropDTO(deviceId,
                                                                                           appDevice.getName(),
                                                                                           propValue));
                    continue;
                }
                for (var prop : deviceTSDTO.getPropertyTsBoList()) {
                    if (propCode.equals(prop.getPropertyCode())) {
                        propValue = Integer.parseInt(prop.getValueTsBoList().getFirst().getValue().toString());
                    }
                }
            }
            trafficDetectionDevicePropDTOMap.add(new TrafficDetectionDevicePropDTO(deviceId,
                                                                                   appDevice.getName(),
                                                                                   propValue));
        }
        return trafficDetectionDevicePropDTOMap;
    }

    public Map<String, DeviceTSDTO> getDeviceTSHistoriesMap(String propCode, Period period, Set<Long> deviceIdSet, Integer step) {
        return getDeviceTSHistoriesMap(propCode, period, deviceIdSet, step, "avg");
    }

    public Map<String, DeviceTSDTO> getDeviceTSHistoriesMap(String propCode, Period period, Set<Long> deviceIdSet, Integer step, String aggType) {
        var param = new MultiHistorySampleParam();
        param.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        param.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        param.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        param.setStep(step);
        param.setAggType(aggType);
        return this.iotDevicePropClient.multiHistorySampleProp(param);
    }

    private List<TrafficDetectionDevicePropDTO> getDefaultTrafficDetectionDevicePropDTOS(Set<Long> deviceIds,
                                                                                         Map<Long, DeviceBaseArchivesInfoDTO> appDevicesMap
                                                                                        ) {
        return deviceIds.stream().map(deviceId -> {
            var appDevice = appDevicesMap.getOrDefault(deviceId, null);
            return Objects.isNull(appDevice) ? null
                                             : new TrafficDetectionDevicePropDTO(deviceId, appDevice.getName(), 0);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String buildDevIdAndPropCodes(Set<Long> deviceIds, String propCode) {
        return deviceIds.stream().map(deviceId -> deviceId + "." + propCode).collect(Collectors.joining(","));
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return new BigDecimal(value.toString()).intValue();
    }
}
