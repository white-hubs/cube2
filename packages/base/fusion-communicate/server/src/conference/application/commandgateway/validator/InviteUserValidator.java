package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InviteUserValidator extends BaseConferenceRoomValidator {

    @Override
    protected void validate(ConferenceRoom conferenceRoom) {

    }
}
