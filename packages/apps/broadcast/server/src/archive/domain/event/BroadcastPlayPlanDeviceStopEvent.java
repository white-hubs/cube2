package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class BroadcastPlayPlanDeviceStopEvent extends ApplicationEvent {

    private final List<BroadcastPlayPlan> broadcastPlayPlans;

    public BroadcastPlayPlanDeviceStopEvent(Object source, List<BroadcastPlayPlan> broadcastPlayPlans) {
        super(source);
        this.broadcastPlayPlans = broadcastPlayPlans;
    }
}
