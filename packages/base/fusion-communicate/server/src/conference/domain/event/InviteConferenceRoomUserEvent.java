package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class InviteConferenceRoomUserEvent extends ApplicationEvent {

    private static final long serialVersionUID = 7878247310773734777L;

    private final ConferenceRoom conferenceRoom;

    private final Long inviteUserId;

    private final Set<ConferenceInviteUser> inviteUsers;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public InviteConferenceRoomUserEvent(Object source, ConferenceRoom conferenceRoom, Long inviteUserId,
                                         Set<ConferenceInviteUser> inviteUsers
                                        ) {
        super(source);
        this.conferenceRoom = conferenceRoom;
        this.inviteUserId = inviteUserId;
        this.inviteUsers = inviteUsers;
    }
}
