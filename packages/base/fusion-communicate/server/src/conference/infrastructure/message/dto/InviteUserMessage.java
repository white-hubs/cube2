package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserMessage {

    /**
     * 发起用户
     */
    private Long callingUser;

    /**
     * 通话类型(区分单人or多人)
     */
    private String callingType;

    /**
     * 媒体类型
     */
    private String mediaType;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 邀请的成员列表
     */
    private Set<Long> inviteMembers;
}
