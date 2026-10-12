package com.guhecloud.cube.base.message2.server.base.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息列表返回参数
 *
 * @author lanchun
 * @date 2021/11/16 15:53
 */
@Data
public class MsgRecordPageVO {

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
     * 消息参数
     */
    private String msgParams;

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
     * 消息类型
     */
    private String msgType;

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
     * 厂家编号
     */
    private String manufacturerCode;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 过期时间
     */
    private LocalDateTime expirationTime;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已读
     */
    private Boolean hasRead;

    /**
     * 跳转地址
     */
    private String url;
}
