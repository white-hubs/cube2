package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.service;

import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.request.GetDevicePropRequest;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.iot.response.DevicePropResponse;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastIotDevicePropQueryService {

    private final IotDevicePropClient iotDevicePropClient;

    public List<DevicePropResponse> getBroadcastDeviceProp(GetDevicePropRequest request) {
        try {
            MultiDevicePropParam multiDevicePropParam = new MultiDevicePropParam();
            multiDevicePropParam.setNotBeforeTs(-1L);
            multiDevicePropParam.setDevIdAndPropCodes(this.buildDevIdAndPropCodes(request.getDeviceIds(), request.getPropCode()));
            Map<String, List<KeyValueWithTsDTO>> multiProp = this.iotDevicePropClient.multiProp(multiDevicePropParam);
            if (MapUtils.isEmpty(multiProp)) {
                return Collections.emptyList();
            }
            List<DevicePropResponse> devicePropResponses = new ArrayList<>(multiProp.size());
            multiProp.forEach((deviceId, props) -> {
                if (!CollectionUtils.isEmpty(props)) {
                    List<DevicePropResponse.PropKeyValue> propKeyValues = new ArrayList<>(props.size());
                    props.forEach(prop -> {
                        propKeyValues.add(DevicePropResponse.PropKeyValue.builder()
                                                  .propCode(prop.getKey())
                                                  .propValue(String.valueOf(prop.getValue()))
                                                  .build());
                    });
                    devicePropResponses.add(DevicePropResponse.builder()
                                                    .id(Long.valueOf(deviceId))
                                                    .propKeyValues(propKeyValues)
                                                    .build());
                }
            });
            return devicePropResponses;
        } catch (Exception e) {
            log.error("获取设备属性失败,失败原因", e);
        }
        return Collections.emptyList();
    }

    public String buildDevIdAndPropCodes(Set<Long> deviceIds, String propCode) {
        return deviceIds.stream().map(deviceId -> deviceId + "." + propCode).collect(Collectors.joining(","));
    }
}
