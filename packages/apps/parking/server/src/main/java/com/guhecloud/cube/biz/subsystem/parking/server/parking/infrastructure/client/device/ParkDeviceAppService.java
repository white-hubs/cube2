package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.query.DeviceFilterPageQuery;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParkDeviceAppService {

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    private final IotDevicePropClient iotDevicePropClient;

    private static final String PARKING_APP_TAG_CODE = "builtin.app::PARKINGMANAGE";

    public ParkDeviceAppService(DeviceCenterStdClient deviceCenterStdClient, DeviceCenterTagStdClient deviceCenterTagStdClient,
                                IotDevicePropClient iotDevicePropClient
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
        deviceFilterPageParam.setTagCodes(Collections.singletonList(PARKING_APP_TAG_CODE));
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
     * @return 设备列表
     */
    public List<DeviceBaseArchivesInfoDTO> queryParkingDevices() {
        return deviceCenterStdClient.getListByTag(Collections.singleton(PARKING_APP_TAG_CODE));
    }

    /**
     * 服滤拥有车场设备标签的设备
     *
     * @param deviceIds 设备ID列表
     *
     * @return 有板车场b备标签的设备ID列表
     */
    public Set<Long> filterParkingTagDeviceIds(Set<Long> deviceIds) {
        Map<Long, List<CustomerTagVO>> deviceAllTagMap = this.deviceCenterTagStdClient.deviceAllTag(deviceIds);
        return deviceIds.stream().filter(deviceId -> deviceAllTagMap.containsKey(deviceId) &&
                                                     deviceAllTagMap.get(deviceId).stream()
                                                             .anyMatch(tag -> tag.getTagCode().equals(PARKING_APP_TAG_CODE)))
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
        var appDevices = queryParkingDevices();
        var deviceIdSet = appDevices.stream().map(DeviceBaseArchivesInfoDTO::getId)
                .filter(deviceIds::contains)
                .collect(Collectors.toSet());
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
     * 获取多设备单属性历史求和值。
     */
    public Map<Long, Integer> getDevicesSinglePropHistorySumValue(Set<Long> deviceIds, String propCode, Period period) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        var deviceIdSet = filterParkingTagDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceIdSet)) {
            return Collections.emptyMap();
        }
        var param = new MultiHistorySampleParam();
        param.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        param.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        param.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        param.setStep(60);
        param.setAggType("sum");
        var deviceHistoryMap = this.iotDevicePropClient.multiHistorySampleProp(param);
        if (MapUtils.isEmpty(deviceHistoryMap)) {
            return Collections.emptyMap();
        }
        var propValueMap = new HashMap<Long, Integer>();
        for (var entry : deviceHistoryMap.entrySet()) {
            var deviceTs = entry.getValue();
            if (deviceTs == null || CollectionUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
                continue;
            }
            for (var prop : deviceTs.getPropertyTsBoList()) {
                if (propCode.equals(prop.getPropertyCode()) && CollectionUtils.isNotEmpty(prop.getValueTsBoList())) {
                    var value = prop.getValueTsBoList().getFirst().getValue();
                    propValueMap.put(Long.valueOf(entry.getKey()), Integer.valueOf(value.toString()));
                }
            }
        }
        return propValueMap;
    }

    public Map<String, DeviceTSDTO> getDeviceTSHistoriesMap(String propCode,
                                                            Period period,
                                                            Set<Long> deviceIds,
                                                            Integer step,
                                                            String aggType) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyMap();
        }
        var deviceIdSet = filterParkingTagDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceIdSet)) {
            return Collections.emptyMap();
        }
        var param = new MultiHistorySampleParam();
        param.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(deviceIdSet, propCode));
        param.setStartTs(DateUtils.toTimestamp(period.getBegin()));
        param.setEndTs(DateUtils.toTimestamp(period.getEnd()));
        param.setStep(step);
        param.setAggType(aggType);
        return this.iotDevicePropClient.multiHistorySampleProp(param);
    }

    private String buildDevIdAndPropCodes(Set<Long> deviceIds, String propCode) {
        return deviceIds.stream().map(deviceId -> deviceId + "." + propCode).collect(Collectors.joining(","));
    }
}
