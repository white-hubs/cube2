package com.guhecloud.cube.base.message2.server.base.rocketmq.message;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MsgRecordUserMqMessage {

    private Long id;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 接收者标识(可能是手机号,用户id,openId等)
     */
    private String receiver;

    /**
     * 是否发送成功
     */
    private Boolean sendStatus;

    /**
     * 发送状态描述
     */
    private String sendStatusDesc;

    /**
     * 错误信息
     */
    private String errorDescribe;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 来源租户
     */
    private Integer sourceTenant;

}
