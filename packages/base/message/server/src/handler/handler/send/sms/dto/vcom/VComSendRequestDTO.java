package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom;

import lombok.Data;

@Data
public class VComSendRequestDTO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 扩展小号，加在通道码号后面
     */
    private String ext;

    /**
     * 消息id
     */
    private Long msgid;

    /**
     * 模板 ID
     */
    private Integer templateId;

    /**
     * 用户子账户名称
     */
    private String childUsername;

    /**
     * 自定义任务标识（建议唯一，便于查询与匹配）
     */
    private String apiClientMark;
}
