package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg;

import lombok.Data;

import java.util.List;

@Data
public class JgMsgBatchSendRequestDTO {

    /**
     * 签名ID
     */
    private String sign_id;

    /**
     * 模板ID
     */
    private Integer temp_id;

    /**
     * 标签
     */
    private String tag;

    /**
     * 接收者列表
     */
    private List<JgMsgRecipientRequestDTO> recipients;

}
