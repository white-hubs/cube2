package com.guhecloud.cube.srv.gen.official.management.server.message.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class FocusWorkDeleteEvent extends ApplicationEvent {

    private final Set<Long> ids;

    private final Set<String> processInstanceId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public FocusWorkDeleteEvent(Object source, Set<Long> ids, Set<String> processInstanceId) {
        super(source);
        this.ids = ids;
        this.processInstanceId = processInstanceId;
    }
}
