package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * 关闭房间事件
 *
 * @author lanchun
 * @date 2025/2/13 14:52
 */
@Getter
public class CloseConferenceRoomEvent extends ApplicationEvent {

    private static final long serialVersionUID = -6999328710152312339L;
    private final ConferenceRoom conferenceRoom;

    private final Set<ConferenceUser> conferenceRoomUsers;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CloseConferenceRoomEvent(Object source, ConferenceRoom conferenceRoom, Set<ConferenceUser> conferenceRoomUsers) {
        super(source);
        this.conferenceRoom = conferenceRoom;
        this.conferenceRoomUsers = conferenceRoomUsers;
    }
}
