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
public class RemoveConferenceRoomUserMessage {

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 踢出成员
     */
    private Set<Long> members;
}
