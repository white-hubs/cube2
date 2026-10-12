package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户其它端已加入消息
 *
 * @author lanchun
 * @date 2025/2/21 9:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOtherTerminalJoinRoomMessage {

    /**
     * 房间号
     */
    private String roomNumber;

}
