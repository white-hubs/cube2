package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.cube.base.message2.server.sat.enumeration.SatMsgSceneReceiveModeEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 消息场景配置
 *
 * @author lanchun
 * @date 2023/6/26 14:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgSceneReceive extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 场景编号
     */
    @Unique(composites = { "deleted" }, message = "场景编号已存在")
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
     * 接收模式
     */
    private SatMsgSceneReceiveModeEnum receiveMode;
}
