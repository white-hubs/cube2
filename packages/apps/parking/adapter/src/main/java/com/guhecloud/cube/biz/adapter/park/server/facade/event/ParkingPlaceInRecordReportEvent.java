package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPlaceRecordRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParkingPlaceInRecordReportEvent extends ApplicationEvent {

    private final ParkingPlaceRecordRequest parkingPlaceRecordRequest;

    public ParkingPlaceInRecordReportEvent(Object source, ParkingPlaceRecordRequest parkingPlaceRecordRequest) {
        super(source);
        this.parkingPlaceRecordRequest = parkingPlaceRecordRequest;
    }
}
