package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.events;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.Machines;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 机组手术室变更事件
 */
@Getter
public class MachinesUpdateEvent extends ApplicationEvent {
    private final Machines machines;

    public MachinesUpdateEvent(Object source, Machines machines){
        super(source);
        this.machines = machines;
    }
}
