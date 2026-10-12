package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service;

import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device.DevicePropHistoryDTO;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotDevicePropService {

    private final IotDevicePropClient iotDevicePropClient;

    public List<DevicePropHistoryDTO> getDevicePropHistoriesByPropCode(String deviceId,
                                                                       String propCode,
                                                                       Long startTime,
                                                                       Long endTime
                                                                      ) {
        HistorySampleParam param = new HistorySampleParam();
        param.setDeviceId(deviceId);
        param.setProperties(Collections.singletonList(propCode));
        param.setStartTs(startTime);
        param.setEndTs(endTime);
        param.setStep(1);
        param.setAggType("max");
        DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(param);
        if (Objects.isNull(deviceTSDTO) ||
            CollectionUtils.isEmpty(deviceTSDTO.getPropertyTsBoList())) {
            log.warn("获取设备采样数据为空,设备id:{},属性code:{}", deviceId, propCode);
            return Collections.emptyList();
        }
        PropertyTsDTO propertyTsDTO = deviceTSDTO.getPropertyTsBoList().get(0);
        return propertyTsDTO.getValueTsBoList().stream()
                .map(propertyTs ->
                             DevicePropHistoryDTO.builder()
                                     .key(propertyTs.getKey())
                                     .value(propertyTs.getValue().toString())
                                     .uploadTime(DateUtils.parse(propertyTs.getTs()))
                                     .build()
                    ).collect(Collectors.toList());
    }
}
