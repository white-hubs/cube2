package com.guhecloud.cube.biz.mdata.server.supplier.entity;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2021/10/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskLevel extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 风险级别类别
     */
    private RiskLevelEnum type;
    /**
     * 风险级别数值
     */
    private String value;

    public RiskLevel(RiskLevelEnum type, String value) {
        this.type = type;
        this.value = value;
    }
}
