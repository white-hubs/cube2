package com.guhecloud.cube.base.message2.client.param;

import lombok.Builder;
import lombok.Data;

/**
 * 消息发送对象参数
 *
 * @author lanchun
 * @date 2021/11/15 18:52
 */
@Data
@Builder
public class MsgSendTargetParam {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号码
     */
    private String userPhone;

    /**
     * 发送邮箱
     */
    private String userEmail;

    /**
     * 接收者信息
     */
    private String receiver;
}
