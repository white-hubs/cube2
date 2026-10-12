package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RefuseJoinConferenceRoomEvent extends ApplicationEvent {

    private final ConferenceRoom conferenceRoom;

    private final Long userId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RefuseJoinConferenceRoomEvent(Object source, ConferenceRoom conferenceRoom, Long userId) {
        super(source);
        this.conferenceRoom = conferenceRoom;
        this.userId = userId;
    }
}
