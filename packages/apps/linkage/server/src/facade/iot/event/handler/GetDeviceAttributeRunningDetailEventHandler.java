package com.guhecloud.cube.biz.linkage.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.linkage.server.facade.iot.event.GetDeviceAttributeRunningDetailEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.response.DeviceAttributeRunningResponse;
import com.guhecloud.cube.biz.linkage.server.facade.iot.service.LinkageIotDeviceProService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetDeviceAttributeRunningDetailEventHandler {

    private final LinkageIotDeviceProService linkageIotDeviceProService;

    @EventListener
    public void getDeviceAttributeRunningDetail(final GetDeviceAttributeRunningDetailEvent event) {
        List<DeviceAttributeRunningResponse> deviceAttributeRunning = this.linkageIotDeviceProService
                .getDeviceAttributeRunning(event.getDeviceAttributeRunningRequest().getDeviceId());
        event.accept(deviceAttributeRunning);
    }
}
