package com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.sat.enumeration.MsgTemplateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 消息发送任务信息
 *
 * @author lanchun
 * @date 2023/3/10 10:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {

    /**
     * 发送记录id(全局的一个业务记录主键)
     */
    private Long id;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 接收者列表
     */
    private List<MessageReceiverInfo> messageReceiverInfos;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 发送渠道
     */
    private ChannelEnum channelCode;

    /**
     * 发送厂家
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 模板类型
     */
    private MsgTemplateTypeEnum tempType;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 模板内容参数
     */
    private Map<String, String> templateContentParams;

    /**
     * 发送账号信息
     */
    private String sendAccountInfo;

    /**
     * 模板配置
     */
    private String tmpConfig;

    /**
     * 业务主体信息
     */
    private Object businessPrincipal;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 配置租户
     */
    private Integer configTenant;
}
