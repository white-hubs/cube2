package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 消息场景发送通道配置
 *
 * @author lanchun
 * @date 2023/6/26 14:23
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgSceneReceiveChannel extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 场景接收配置主键
     */
    private Long satMsgSceneReceiveId;

    /**
     * 通道编号
     */
    @Unique(composites = { "satMsgSceneReceiveId" }, message = "场景下已勾选此通道,请勿重复勾选")
    private ChannelEnum channelCode;
}
