package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GadxSendRequestDTO {

    /**
     * 分割，数量需与手机号保持一致，如果发送的相同，
     * 则只需 要一个内容即可，每个内容必须有以【**】开头的签名（签 名需要备案）
     * 注意!目前仅支持多个号码发送相同内容，不支持多个号码发送多个内容
     */
    private String contents;

    /**
     * 手机号，多个英文逗号分割，最多3000个
     */
    private String mobiles;

    /**
     * 签名
     */
    private String sign;
}
