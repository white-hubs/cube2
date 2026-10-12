package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 小散工程排名统计
 *
 * @author lanchun
 * @date 2024/4/8 17:46
 */
@Data
public class LooseWorksRankVO {

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 小散工程总数
     */
    private Integer totalNum;

    /**
     * 环比率
     */
    @Decimal
    private BigDecimal chainRatio;

}
