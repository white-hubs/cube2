package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 场景接收配置
 *
 * @author lanchun
 * @date 2023/6/26 14:22
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgSceneReceiveUser extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 场景接收配置主键
     */
    private Long satMsgSceneReceiveId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}
