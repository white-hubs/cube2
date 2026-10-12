package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VComLoginResponseDTO {

    /**
     * 时间，格式为 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime expired;

    /**
     * 显示用户状态，为“正常”时可以发送短信
     */
    private String status;

    /**
     * 生成的 Token
     */
    private String tokenkey;
}
