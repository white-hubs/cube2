package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreateConferenceRoomEvent extends ApplicationEvent implements Supplier<String>, Consumer<String> {

    private String roomNumber;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CreateConferenceRoomEvent(Object source) {
        super(source);
    }

    @Override
    public void accept(String s) {
        this.roomNumber = s;
    }

    @Override
    public String get() {
        return this.roomNumber;
    }
}
