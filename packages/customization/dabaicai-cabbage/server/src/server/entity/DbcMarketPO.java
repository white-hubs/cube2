package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜市场
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_market")
@EqualsAndHashCode(callSuper = true)
public class DbcMarketPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 市场编号
     */
    private Integer marketId;
    private String marketName;
    /**
     * 市场id对应租户id
     */
    private Integer tenantMarketId;
}
