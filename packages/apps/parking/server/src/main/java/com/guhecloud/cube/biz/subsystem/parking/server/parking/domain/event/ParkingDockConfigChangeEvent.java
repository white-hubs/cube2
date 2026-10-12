package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParkingDockConfigChangeEvent extends ApplicationEvent {

    private final ParkingInfo oldParkingInfo;

    private final ParkingInfo newParkingInfo;

    public ParkingDockConfigChangeEvent(Object source, ParkingInfo oldParkingInfo,
                                        ParkingInfo newParkingInfo
                                       ) {
        super(source);
        this.oldParkingInfo = oldParkingInfo;
        this.newParkingInfo = newParkingInfo;
    }
}
