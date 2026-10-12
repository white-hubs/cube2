package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinConferenceRoomMessage {

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
     * 成员列表
     */
    private Long member;
}
