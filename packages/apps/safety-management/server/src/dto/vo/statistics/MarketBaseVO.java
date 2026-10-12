package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 市场基本信息
 * @date 2023/9/20 9:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MarketBaseVO {
    /**
     * 市场租户号
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场信息
     */
    private String marketInfo;
}
