package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author lanchun
 * @date 2023/5/15 15:20
 */
@Builder
@Data
public class JgMsgRecipientRequestDTO {

    /**
     * 发送人电话
     */
    private String mobile;

    /**
     * 模板参数
     */
    private Map<String, String> temp_para;

}
