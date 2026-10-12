package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.platform.base.standard.exception.Assert;

import java.util.Objects;

public abstract class BaseConferenceRoomValidator {

    public void valid(ConferenceRoom conferenceRoom) {
        Assert.isNotFound(Objects.isNull(conferenceRoom) ||
                          Objects.nonNull(conferenceRoom.getConference().getClosingTime()), "房间不存在或已被解散");
        validate(conferenceRoom);
    }

    protected abstract void validate(ConferenceRoom conferenceRoom);
}
