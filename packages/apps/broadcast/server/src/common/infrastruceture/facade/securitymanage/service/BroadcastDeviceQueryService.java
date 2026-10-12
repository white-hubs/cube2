package com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.service;

import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.response.BroadcastDeviceResponse;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastDeviceQueryService {

    private final static String BROADCAST_APP_CODE = "BROADCAST_CENTER";

    private final SecurityDeviceClient securityDeviceClient;

    public List<BroadcastDeviceResponse> queryBroadcastDevices() {
        ModelUnderDeviceParam param = new ModelUnderDeviceParam();
        param.setApplicationCode(BROADCAST_APP_CODE);
        List<SecurityDeviceVo> securityDeviceVos = this.securityDeviceClient.getDeviceListByModel(param);
        if (CollectionUtils.isEmpty(securityDeviceVos)) {
            return Collections.emptyList();
        }
        return securityDeviceVos.stream().map(securityDeviceVo ->
                                                      ObjectUtils.copying(securityDeviceVo, BroadcastDeviceResponse.class)
                                             ).collect(Collectors.toList());
    }

    public List<BroadcastDeviceResponse> queryBroadcastDevicesByIds(List<Long> deviceIds) {
        InsideSecurityDeviceParam param = new InsideSecurityDeviceParam();
        param.setDeviceIds(deviceIds);
        param.setApplicationCode(BROADCAST_APP_CODE);
        List<DeviceMonitoringVo> deviceMonitoringVos = this.securityDeviceClient.getDeviceMonitoringList(param);
        if (CollectionUtils.isEmpty(deviceMonitoringVos)) {
            return Collections.emptyList();
        }
        return deviceMonitoringVos.stream().map(securityDeviceVo ->
                                                        ObjectUtils.copying(securityDeviceVo, BroadcastDeviceResponse.class)
                                               ).collect(Collectors.toList());
    }
}
