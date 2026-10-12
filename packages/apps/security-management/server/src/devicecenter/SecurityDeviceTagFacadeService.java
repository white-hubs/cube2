package com.guhecloud.cube.srv.gen.securitymanagement.server.devicecenter;

import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityDeviceTagFacadeService {

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    public List<Long> filterTagDevices(String applicationCode, List<String> tagCodes) {
        String appTag = SecurityManagementCommonUtil.applicationConstants + applicationCode;
        return new ArrayList<>(deviceCenterTagStdClient.queryBusinessCustomerTagDevices(appTag, tagCodes));
    }
}
