package com.guhecloud.cube.biz.adapter.park.server.facade.event;

import com.guhecloud.cube.biz.adapter.park.server.facade.request.ChargeTotalRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChargeTotalQueryEvent extends ApplicationEvent {

    private final ChargeTotalRequest chargeTotalRequest;

    public ChargeTotalQueryEvent(Object source, ChargeTotalRequest chargeTotalRequest) {
        super(source);
        this.chargeTotalRequest = chargeTotalRequest;
    }
}
