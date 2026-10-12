package com.guhecloud.cube.base.message2.server.base.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息发送人记录列表
 *
 * @author lanchun
 * @date 2022/6/8 17:08
 */
@Data
public class MsgRecordTargetVO {

    private Long id;

    /**
     * 消息通道类型
     */
    private ChannelEnum channelCode;

    /**
     * 消息通道类型
     */
    private String channelTypeName;

    /**
     * 消息主键
     */
    private Long msgRecordId;

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
     * 是否发送成功
     */
    private Boolean sendStatus;

    /**
     * 发送状态描述
     */
    private String sendStatusDesc;

    /**
     * 是否已读
     */
    private Boolean hasRead;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送次数
     */
    private Integer sendTimes = 1;
}
