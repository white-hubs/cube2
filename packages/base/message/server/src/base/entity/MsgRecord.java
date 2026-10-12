package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgSendStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息记录数据模型
 *
 * @author lanchun
 * @date 2021/11/9 17:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgRecord extends BasicEnhancedTenantEntity<Long> {

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
}
