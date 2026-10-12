package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class IotParkingInfoEvent extends ApplicationEvent {

    private Integer projectId;
    private String prodCode;

    public IotParkingInfoEvent(Object source, Integer projectId, String prodCode) {
        super(source);
        this.projectId = projectId;
        this.prodCode = prodCode;
    }
}
