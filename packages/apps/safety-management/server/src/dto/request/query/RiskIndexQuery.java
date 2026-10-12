package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 安全风险指数查询条件
 * @date 2023/10/16 9:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RiskIndexQuery {
    /**
     * 市场租户号
     */
    private Integer marketTenant;
    /**
     * 查询时间
     */
    private Period period;

    private String statisticalDate;
}
