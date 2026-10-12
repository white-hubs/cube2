package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全风险指数
 * @date 2023/10/16 9:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RiskIndexVO {
    /**
     * 安全风险指数分数
     */
    private Integer riskIndex;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场租户号
     */
    private Integer marketTenant;
}
