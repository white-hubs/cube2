package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 小散工程统计
 *
 * @author lanchun
 * @date 2024/4/8 16:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorksStatisticsVO {

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 环比率
     */
    @Decimal
    private BigDecimal chainRatio;

    /**
     * 进行中数量
     */
    private Integer underwayNum;

    /**
     * 进行中环比率
     */
    @Decimal
    private BigDecimal underwayChainRatio;

    /**
     * 异常数量
     */
    private Integer abnormalNum;

    /**
     * 异常环比率
     */
    @Decimal
    private BigDecimal abnormalChainRatio;
}
