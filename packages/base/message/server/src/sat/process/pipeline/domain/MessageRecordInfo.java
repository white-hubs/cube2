package com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgSendStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 消息记录信息
 *
 * @author lanchun
 * @date 2023/5/9 11:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRecordInfo {

    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息编号
     */
    private String msgSceneCode;

    /**
     * 消息名称
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
     * 模板配置
     */
    private String tmpConfig;

    /**
     * 消息模板版本
     */
    private String msgTmpVersion;

    /**
     * 消息参数
     */
    private Map<String, String> msgParams;

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
     * 通道类型
     */
    private ChannelEnum channelCode;

    /**
     * 厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 发送状态
     */
    private MsgSendStatusEnum sendStatus;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 发送的租户信息
     */
    private Integer tenant;

    /**
     * 接收者信息
     */
    private List<MessageRecordTargetInfo> messageRecordTargetInfos;
}
