package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.VehicleBottomMonitorRecordRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VehicleBottomMonitorRecordEvent extends ApplicationEvent {

    private final VehicleBottomMonitorRecordRequest vehicleBottomMonitorRecordRequest;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public VehicleBottomMonitorRecordEvent(Object source, VehicleBottomMonitorRecordRequest vehicleBottomMonitorRecordRequest) {
        super(source);
        this.vehicleBottomMonitorRecordRequest = vehicleBottomMonitorRecordRequest;
    }
}
