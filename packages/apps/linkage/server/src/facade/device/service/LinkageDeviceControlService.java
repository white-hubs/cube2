package com.guhecloud.cube.biz.linkage.server.facade.device.service;

import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.biz.linkage.server.facade.device.event.PropertyControlEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageDeviceControlService {

    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlStdClient;

    public void controlAttribute(PropertyControlEvent propertyControlEvent) {
        DevicePropWriteCmd deviceAttrCtlCommand = new DevicePropWriteCmd();
        propertyControlEvent.getPropertyControlRequests().stream().forEach(propertyControlRequest -> {
            deviceAttrCtlCommand.setDeviceId(Long.valueOf(propertyControlRequest.getDeviceId()));
            deviceAttrCtlCommand.setCommandSeqOptional(Long.valueOf(propertyControlRequest.getCommandSeq()));
            DevicePropWriteCmd.PropWriteValue propWriteValue = new DevicePropWriteCmd.PropWriteValue();
            propWriteValue.setCode(propertyControlRequest.getPropCode());
            propWriteValue.setExpectedValue(propertyControlRequest.getPropValue());
            deviceAttrCtlCommand.setPropWriteValues(Collections.singleton(propWriteValue));
            this.deviceCenterAttrCtlStdClient.controlAttribute(deviceAttrCtlCommand);
        });
    }
}
