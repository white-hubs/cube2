package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BroadcastPlayPlanEvent extends ApplicationEvent {

    private final BroadcastPlayPlan broadcastPlayPlan;

    public BroadcastPlayPlanEvent(Object source, BroadcastPlayPlan broadcastPlayPlan) {
        super(source);
        this.broadcastPlayPlan = broadcastPlayPlan;
    }
}
