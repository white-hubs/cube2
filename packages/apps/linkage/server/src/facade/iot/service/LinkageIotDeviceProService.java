package com.guhecloud.cube.biz.linkage.server.facade.iot.service;

import com.guhecloud.cube.biz.linkage.server.facade.iot.enumeration.AttributePermissionEnum;
import com.guhecloud.cube.biz.linkage.server.facade.iot.response.DeviceAttributeRunningResponse;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.util.DevicePropUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageIotDeviceProService {

    private final IotDeviceClient iotDeviceClient;

    public List<DeviceAttributeRunningResponse> getDeviceAttributeRunning(Long deviceId) {
        DeviceDetailParam param = new DeviceDetailParam();
        param.setWithPropertySpec(true);
        param.setWithSnapShot(true);
        param.setDeviceId(deviceId.toString());
        DeviceDetailDTO detail = this.iotDeviceClient.detail(param);
        if (Objects.nonNull(detail)) {
            List<DeviceAttributeRunningResponse> deviceAttributeRunningResponses = detail.getProductPropVoList().stream()
                    .map(productPropDTO ->
                                 getDeviceAttributeRunningResponse(detail, productPropDTO)
                        ).collect(Collectors.toList());
            return deviceAttributeRunningResponses;
        }
        return Collections.emptyList();
    }

    public DeviceAttributeRunningResponse getDeviceAttributeRunningResponse(DeviceDetailDTO detail, ProductPropDTO productPropDTO) {
        Optional<KeyValueWithTsDTO> propertyValueOptional = detail.getSnapshots().stream()
                .filter(keyValueWithTsDTO -> productPropDTO.getPropCode().equals(keyValueWithTsDTO.getKey()))
                .findFirst();
        DeviceAttributeRunningResponse deviceAttributeRunningResponse = DeviceAttributeRunningResponse.builder()
                .attributeCode(productPropDTO.getPropCode())
                .attributeName(productPropDTO.getPropName())
                .permission(AttributePermissionEnum.valueOf(DevicePropUtils.convertReadWriteEnum(productPropDTO.getDataWrite()).name()))
                .dataType(productPropDTO.getDataType())
                .attributeUnitName(productPropDTO.getUnitName())
                .dataConstraint(productPropDTO.getDataConstraint())
                .build();
        propertyValueOptional.ifPresent(propertyValue -> {
            deviceAttributeRunningResponse.setOriginalValue(propertyValue.getValue());
            deviceAttributeRunningResponse.setTranslatedValue(DevicePropUtils.iotPropVal2Chinese(productPropDTO.getDataType(),
                                                                                                 propertyValue.getValue(),
                                                                                                 productPropDTO.getDataConstraint()));
        });
        return deviceAttributeRunningResponse;
    }

    public String getDeviceAttributeTranslated(Long deviceId, String prodCode, String prodValue) {
        List<DeviceAttributeRunningResponse> deviceAttributeRunning = this.getDeviceAttributeRunning(deviceId);
        Optional<DeviceAttributeRunningResponse> attributeRunningResponse = deviceAttributeRunning.stream()
                .filter(deviceAttributeRunningResponse -> prodCode.equals(deviceAttributeRunningResponse.getAttributeCode()))
                .findAny();
        if (attributeRunningResponse.isPresent()) {
            DeviceAttributeRunningResponse deviceAttributeRunningResponse = attributeRunningResponse.get();
            return DevicePropUtils.iotPropVal2Chinese(deviceAttributeRunningResponse.getDataType(),
                                                      prodValue,
                                                      deviceAttributeRunningResponse.getDataConstraint());
        }
        return StringUtils.EMPTY_STRING;
    }
}
