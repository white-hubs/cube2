package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JoinConferenceRoomValidator extends BaseConferenceRoomValidator {

    @Override
    protected void validate(ConferenceRoom conferenceRoom) {
        Set<ConferenceInviteUser> conferenceInviteUsers = conferenceRoom.getConferenceInviteUsers();
        boolean invitedBy = conferenceInviteUsers.stream()
                .anyMatch(user -> user.getUserId().equals(SecurityContextHolder.getPrincipal().getId()));
        Assert.isPermissionDenied(!invitedBy, "未邀请,无法自行进入房间");
        boolean userJoinedRoom = conferenceRoom.getConferenceUsers().stream()
                .anyMatch(user -> user.getId().equals(SecurityContextHolder.getPrincipal().getId()) &&
                                  Objects.isNull(user.getDepartureTime()));
        Assert.isConflict(userJoinedRoom, "用户已加入房间请勿重复加入");
    }
}
