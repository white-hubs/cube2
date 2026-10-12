package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.ConferenceUserTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;

public class ConferenceRoomUserFactory {

    public static ConferenceUser createConferenceRoomUser(final Long userId) {
        ConferenceUser conferenceUser = new ConferenceUser(userId,
                                                           ConferenceUserTypeEnum.ORDINARY_MEMBERS);
        conferenceUser.setMembershipTime(DateUtils.now());
        return conferenceUser;
    }
}
