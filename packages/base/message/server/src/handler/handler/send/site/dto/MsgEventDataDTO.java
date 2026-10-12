package com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgEventDataDTO {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 消息对象
     */
    private EventDataDTO eventDataDTO;

    public MsgEventDataDTO(String msgType, EventDataDTO eventDataDTO) {
        this.msgType = msgType;
        this.eventDataDTO = eventDataDTO;
    }
}
