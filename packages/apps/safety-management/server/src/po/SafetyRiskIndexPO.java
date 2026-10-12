package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全管理风险指数
 * @date 2023/10/12 15:22
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "safety_risk_index")
public class SafetyRiskIndexPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 风险指数
     */
    private Integer riskIndex;
    /**
     * 统计周期
     */
    private String statisticalPeriod;
    /**
     * 统计时间
     */
    private String statisticalDate;
    /**
     * 市场租户
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 风险明细json
     */
    private String riskIndexDetailJson;
}
