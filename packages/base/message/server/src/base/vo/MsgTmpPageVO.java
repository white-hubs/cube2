package com.guhecloud.cube.base.message2.server.base.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

@Data
public class MsgTmpPageVO {

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
     * 消息类型
     */
    private String msgType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 消息场景编号
     */
    private String msgSceneCode;

    /**
     * 消息场景名称
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
}
