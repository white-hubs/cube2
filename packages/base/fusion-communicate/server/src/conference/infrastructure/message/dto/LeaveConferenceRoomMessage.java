package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveConferenceRoomMessage {

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 离开成员
     */
    private Long member;
}
