package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ConferenceRoomInviteUserFactory {

    private ConferenceRoomInviteUserFactory() {
    }

    public static ConferenceInviteUser createConferenceInviteUser(final Long userId) {
        return new ConferenceInviteUser(IdWorker.getId(), userId, DateUtils.now());
    }

    public static Set<ConferenceInviteUser> createConferenceInviteUser(final Set<Long> userIds) {
        return userIds.stream().map(ConferenceRoomInviteUserFactory::createConferenceInviteUser).collect(Collectors.toSet());
    }
}
