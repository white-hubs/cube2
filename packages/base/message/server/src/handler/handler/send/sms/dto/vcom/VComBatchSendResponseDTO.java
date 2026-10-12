package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.vcom;

import lombok.Data;

@Data
public class VComBatchSendResponseDTO {

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 用户任务 ID（建议唯一，便于查询与匹配）
     */
    private String apiClientMark;

    /**
     * 提交结果
     * 100 用户账户欠费
     * 102 全局投诉黑名单
     * 103 用户投诉黑名单
     * 104 账户已停用
     * 105 模板不匹配
     * 106 模板不存在
     * 107 号码无效
     * 108 提交失败
     * 109 发送频次超限
     */
    private Integer result;
}
