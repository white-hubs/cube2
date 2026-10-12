package com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeComMsgSendResponseDTO extends WeComBaseResponse {

    /**
     * 不合法的用户id
     */
    private String invaliduser;

    /**
     * 没有基础接口许可
     */
    private String unlicenseduser;

    /**
     * 消息id
     */
    private String msgid;

}
