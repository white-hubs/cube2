package com.guhecloud.cube.base.videogw.server.facade.device.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideogwDeviceCenterQueryService {

    private final DeviceCenterStdClient deviceCenterStdClient;

    public String getDeviceThirdpartyId(String deviceId) {
        DeviceDetailDTO detail = this.deviceCenterStdClient.getDeviceById(Long.valueOf(deviceId));
        if (Objects.isNull(detail)) {
            return StringUtils.EMPTY_STRING;
        }
        return detail.getBaseArchivesInfo().getThirdPartyId();
    }
}
