package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 发起会议房间事件
 *
 * @author lanchun
 * @date 2025/2/17 10:44
 */
@Getter
public class ProposeConferenceRoomEvent extends ApplicationEvent {

    private final ConferenceRoom conferenceRoom;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ProposeConferenceRoomEvent(Object source, ConferenceRoom conferenceRoom) {
        super(source);
        this.conferenceRoom = conferenceRoom;
    }
}
