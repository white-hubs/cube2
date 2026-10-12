package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SafetyEducationMarketTaskStatisticsVO {

    /**
     * 租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 任务数量
     */
    private Integer totalNum;

    /**
     * 环比
     */
    @Decimal
    private BigDecimal chainRatio;
}
