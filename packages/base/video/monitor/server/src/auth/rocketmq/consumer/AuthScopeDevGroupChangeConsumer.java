package com.guhecloud.cube.biz.vmonitor.server.auth.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.event.DeviceGroupChange;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceGroupVO;
import com.guhecloud.cube.biz.vmonitor.server.auth.constants.Const;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.service.CameraSecurityDeviceQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.CONSUMER_GROUP + "_dev", topic = DeviceGroupChange.TOPIC)
public class AuthScopeDevGroupChangeConsumer implements RocketMQListener<DeviceGroupChange> {

    private final AuthScopeService authScopeService;

    private final CameraSecurityDeviceQueryService cameraSecurityDeviceQueryService;

    @Override
    public void onMessage(DeviceGroupChange message) {
        DeviceGroupVO deviceGroup = cameraSecurityDeviceQueryService.getDeviceGroupById(message.getId());
        this.authScopeService.batchUpdateAuthScopeNameByScopeId(deviceGroup.getId(), deviceGroup.getName());
    }
}
