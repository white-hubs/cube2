package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConferenceInviteUser {

    /**
     * 邀请唯一标识
     */
    private final Long id;

    /**
     * 用户主键
     */
    private final Long userId;

    /**
     * 邀请时间
     */
    private final LocalDateTime inviteTime;

    public ConferenceInviteUser(Long id, Long userId, LocalDateTime inviteTime) {
        this.id = id;
        this.userId = userId;
        this.inviteTime = inviteTime;
    }
}
