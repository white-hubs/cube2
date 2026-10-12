package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局消息模板基础数据模型
 *
 * @author lanchun
 * @date 2021/11/9 18:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgTmp extends BasicEnhancedEntity<Long> {

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
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
