package com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板消息发送返回参数
 *
 * @author lanchun
 * @date 2025/1/2 11:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicServiceSendResponse {

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 错误码
     */
    @JsonProperty(value = "errcode")
    private String errCode;

    /**
     * 错误描述
     */
    @JsonProperty(value = "errmsg")
    private String errMsg;

    /**
     * 消息id
     */
    @JsonProperty(value = "msgid")
    private String msgId;
}
