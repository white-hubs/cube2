package com.guhecloud.cube.base.message2.server.sat.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

/**
 * 租户消息模板配置
 *
 * @author lanchun
 * @date 2023/3/7 10:25
 */
@Data
public class SatMsgTmpPageVO {

    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 场景编码
     */
    private String msgSceneCode;

    /**
     * 场景名称
     */
    private String msgSceneName;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 模板是否可用
     */
    private Boolean available;

    /**
     * 配置信息
     */
    private String config;
}
