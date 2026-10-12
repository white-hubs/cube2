package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息记录对象数据模型
 *
 * @author lanchun
 * @date 2021/11/9 17:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MsgRecordTarget extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

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

    /**
     * 来源租户
     */
    private Integer sourceTenant;

}
