package com.guhecloud.cube.biz.emergency.server.facade.iot.service;

import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.response.IotDevicePropResponse;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotDeviceQueryService {

    private final IotDevicePropClient iotDevicePropClient;

    public Optional<IotDevicePropResponse> getDevicePropValue(Long deviceId, String propCode) {
        DevicePropParam devicePropParam = new DevicePropParam();
        devicePropParam.setDeviceId(deviceId);
        devicePropParam.setPropertyCodes(propCode);
        List<KeyValueWithTsDTO> keyValueWithTsDTOS = this.iotDevicePropClient.deviceProp(devicePropParam);
        if (CollectionUtils.isEmpty(keyValueWithTsDTOS)) {
            return Optional.empty();
        }
        KeyValueWithTsDTO keyValueWithTsDTO = keyValueWithTsDTOS.get(0);
        return Optional.of(IotDevicePropResponse.builder()
                                   .propCode(keyValueWithTsDTO.getKey())
                                   .propValue(
                                           Objects.nonNull(keyValueWithTsDTO.getValue()) ? keyValueWithTsDTO.getValue().toString() : null)
                                   .build());
    }
}
