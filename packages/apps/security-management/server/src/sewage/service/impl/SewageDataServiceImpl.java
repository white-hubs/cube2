package com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.dao.SewageDataMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.dao.SewageDeviceConfigMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.entity.SewageData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.entity.SewageDeviceConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.service.SewageDataService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SewageDataServiceImpl extends ServiceImpl<SewageDataMapper, SewageData> implements SewageDataService {

    private final IotDeviceClient iotDeviceClient;

    private final SewageDeviceConfigMapper sewageDeviceConfigMapper;

    @Override
    public Map<String, Object> getDataWaterTreatment() {
        Map<String, Object> remap = new HashMap<>();
        List<SewageDeviceConfig> sewageDeviceConfigs = sewageDeviceConfigMapper.selectList(null);
        if (CollectionUtils.isNotEmpty(sewageDeviceConfigs)) {
            DeviceDetailParam deviceAttributeParam = new DeviceDetailParam();
            deviceAttributeParam.setDeviceId(sewageDeviceConfigs.get(0).getDeviceId().toString());
            deviceAttributeParam.setWithSnapShot(true);
            DeviceDetailDTO detail = iotDeviceClient.detail(deviceAttributeParam);
            List<KeyValueWithTsDTO> snapshots = detail.getSnapshots();
            if (CollectionUtils.isNotEmpty(snapshots)) {
                Map<String, KeyValueWithTsDTO> collect = snapshots.stream().collect(Collectors.toMap(KeyValueWithTsDTO::getKey, s -> s));
                for (SewageDeviceConfig sewageDeviceConfig : sewageDeviceConfigs) {
                    KeyValueWithTsDTO keyValueWithTsDTO = collect.get(sewageDeviceConfig.getAttribute());
                    if (Objects.nonNull(keyValueWithTsDTO)) {
                        if (Objects.nonNull(keyValueWithTsDTO.getValue())) {
                            remap.put(sewageDeviceConfig.getAttribute(), keyValueWithTsDTO.getValue());
                        }

                    }
                }

            }
        }
        return remap;
    }
}
