package com.guhecloud.cube.biz.linkage.server.facade.iot.event;

import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.GetDeviceAttributeRunningRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.response.DeviceAttributeRunningResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class GetDeviceAttributeRunningDetailEvent extends QueryEvent<List<DeviceAttributeRunningResponse>> {

    private final GetDeviceAttributeRunningRequest deviceAttributeRunningRequest;

    public GetDeviceAttributeRunningDetailEvent(Object source, GetDeviceAttributeRunningRequest deviceAttributeRunningRequest) {
        super(source);
        this.deviceAttributeRunningRequest = deviceAttributeRunningRequest;
    }
}
