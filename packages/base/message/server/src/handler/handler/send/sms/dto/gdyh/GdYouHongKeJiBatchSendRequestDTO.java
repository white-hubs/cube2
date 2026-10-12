package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GdYouHongKeJiBatchSendRequestDTO {

    /**
     * send
     */
    private String action;

    /**
     * 用户帐号，由系统管理员提供
     */
    private String account;

    /**
     * 用户密码, 由系统管理员提供
     */
    private String password;

    /**
     * 多个号码之间用半角逗号隔开，建议不超过1000个号码(注：相同号码去重)
     */
    private String mobile;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 虚拟接入号
     */
    private String extno;

    /**
     * 定时时间
     */
    private LocalDateTime atTime;
}
