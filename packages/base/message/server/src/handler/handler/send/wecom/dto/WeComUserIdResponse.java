package com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeComUserIdResponse extends WeComBaseResponse {

    /**
     * 用户id
     */
    private String userid;
}
