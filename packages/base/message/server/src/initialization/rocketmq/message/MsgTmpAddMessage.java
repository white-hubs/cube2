package com.guhecloud.cube.base.message2.server.initialization.rocketmq.message;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板新增消息
 *
 * @author lanchun
 * @date 2023/7/4 10:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgTmpAddMessage {

    public final static String TOPIC = "MSG_TMP_ADD";

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
