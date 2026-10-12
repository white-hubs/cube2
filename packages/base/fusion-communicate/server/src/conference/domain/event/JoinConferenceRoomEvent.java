package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户加入房间事件
 *
 * @author lanchun
 * @date 2025/2/13 14:52
 */
@Getter
public class JoinConferenceRoomEvent extends ApplicationEvent {

    private static final long serialVersionUID = 7878247310773734777L;
    private final ConferenceRoom conferenceRoom;

    private final ConferenceUser joinConferenceRoomUser;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public JoinConferenceRoomEvent(Object source, ConferenceRoom conferenceRoom, ConferenceUser joinConferenceRoomUser) {
        super(source);
        this.conferenceRoom = conferenceRoom;
        this.joinConferenceRoomUser = joinConferenceRoomUser;
    }
}
