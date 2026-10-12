package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParkingChangeEvent extends ApplicationEvent {

    private final ParkingInfo parkingInfo;

    public ParkingChangeEvent(Object source, ParkingInfo parkingInfo) {
        super(source);
        this.parkingInfo = parkingInfo;
    }
}
