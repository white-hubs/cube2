package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LeaveConferenceRoomValidator extends BaseConferenceRoomValidator {

    @Override
    protected void validate(ConferenceRoom conferenceRoom) {
        Set<ConferenceUser> conferenceUsers = conferenceRoom.getConferenceUsers();
        boolean invitedBy = conferenceUsers.stream()
                .anyMatch(user -> user.getId().equals(SecurityContextHolder.getPrincipal().getId()));
        Assert.isNotAcceptable(!invitedBy, "未在房间内");
    }
}
