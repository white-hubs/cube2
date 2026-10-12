package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device.DeviceInfoDTO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotDeviceService {

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final AreaClient areaClient;

    public DeviceInfoDTO queryDevices(Long id) {
        DeviceDetailDTO device = deviceCenterStdClient.getDeviceById(id);
        if (Objects.isNull(device)) {
            return null;
        }
        DeviceInfoDTO deviceInfoResponse = DeviceInfoDTO.builder()
                .id(device.getBaseArchivesInfo().getId())
                .code(device.getBaseArchivesInfo().getCode())
                .name(device.getBaseArchivesInfo().getName())
                .build();
        if (Objects.nonNull(device.getBaseArchivesInfo().getArea())) {
            AreaDTO area = areaClient.getAreaById(device.getBaseArchivesInfo().getArea().getKey());
            deviceInfoResponse.setAreaId(
                    Objects.nonNull(area) ? area.getSequence().substring(0, area.getSequence().lastIndexOf("-"))
                                          : StringUtils.EMPTY_STRING);
            deviceInfoResponse.setAreaName(device.getBaseArchivesInfo().getArea().getVal());
        }
        if (Objects.nonNull(device.getBaseArchivesInfo().getDepartment())) {
            deviceInfoResponse.setDepartmentId(device.getBaseArchivesInfo().getDepartment().getKey());
            deviceInfoResponse.setDepartmentName(device.getBaseArchivesInfo().getDepartment().getVal());
        }
        if (Objects.nonNull(device.getProductInfo()) && Objects.nonNull(device.getProductInfo().getProductCategory())) {
            DeviceProductInfoDTO.ProductCategory productCategory = device.getProductInfo().getProductCategory();
            deviceInfoResponse.setProdCategoryCode(productCategory.getCode());
            deviceInfoResponse.setProdCategoryName(productCategory.getName());
            deviceInfoResponse.setProdId(device.getProductInfo().getId());
            deviceInfoResponse.setProdName(device.getProductInfo().getName());
        }
        if (Objects.nonNull(device.getExpandInfo())) {
            DeviceExpandInfoDTO deviceExpandInfoDTO = device.getExpandInfo();
            deviceInfoResponse.setLntAndLat(deviceExpandInfoDTO.getLntAndLat());
            deviceInfoResponse.setLocationDes(deviceExpandInfoDTO.getLocationDes());
        }
        return deviceInfoResponse;
    }
}
