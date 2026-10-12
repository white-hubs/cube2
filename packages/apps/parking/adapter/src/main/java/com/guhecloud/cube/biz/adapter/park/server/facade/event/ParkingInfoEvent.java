package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParkingInfoEvent extends ApplicationEvent {

    public ParkingInfoEvent(Object source) {
        super(source);
    }
}
