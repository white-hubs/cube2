package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 租户消息模板配置
 *
 * @author lanchun
 * @date 2023/3/7 10:25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgTmp extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 运营端模板主键
     */
    private Long msgTmpId;

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
}
