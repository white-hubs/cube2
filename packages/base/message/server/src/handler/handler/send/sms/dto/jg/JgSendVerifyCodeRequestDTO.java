package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg;

import lombok.Data;

@Data
public class JgSendVerifyCodeRequestDTO {
    /**
     * 签名ID
     */
    private String sign_id;
    private String mobile;
    private Integer temp_id;
}
