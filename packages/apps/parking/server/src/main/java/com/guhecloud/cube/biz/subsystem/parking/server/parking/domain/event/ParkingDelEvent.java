package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ParkingDelEvent extends ApplicationEvent {

    private final List<ParkingInfo> parkingInfos;

    public ParkingDelEvent(Object source, List<ParkingInfo> parkingInfos) {
        super(source);
        this.parkingInfos = parkingInfos;
    }
}
