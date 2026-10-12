package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg;

import lombok.Data;

import java.util.Map;

/**
 * 短信发送对象结果
 *
 * @author lanchun
 * @date 2023/5/15 15:23
 */
@Data
public class JgMsgRecipientResponseDTO {

    /**
     * 消息记录ID
     */
    private String msg_id;

    /**
     * 发送号码
     */
    private String mobile;

    /**
     * 错误码
     */
    private String error_code;

    /**
     * 错误详情
     */
    private String error_message;

    /**
     * 只有发送错误时,才有该参数
     */
    private Map<String, String> temp_para;

}
