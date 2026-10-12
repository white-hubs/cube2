package com.guhecloud.cube.base.message2.server.base.rocketmq.message;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgSendStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgRecordMqMessage {

    public final static String TOPIC = "MSG_RECORD_2";

    private Long id;

    /**
     * 消息场景编号
     */
    private String msgSceneCode;

    /**
     * 消息场景名称
     */
    private String msgSceneName;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息模板主键
     */
    private Long msgTmpId;

    /**
     * 消息模板版本
     */
    private String msgTmpVersion;

    /**
     * 消息参数
     */
    private String msgParams;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务主键
     */
    private String businessId;

    /**
     * 业务主体信息
     */
    private String businessPrincipal;

    /**
     * 消息通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 消息厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 发送状态
     */
    private MsgSendStatusEnum sendStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 过期时间
     */
    private LocalDateTime expirationTime;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 发送端
     */
    private List<MsgRecordTerminalMqMessage> msgRecordTerminals;

    /**
     * 发送用户
     */
    private List<MsgRecordUserMqMessage> msgRecordUser;

    /**
     * 租户信息
     */
    private Integer tenant;
}
