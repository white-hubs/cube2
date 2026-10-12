package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SafetyEducationTaskChainVO {

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 环比
     */
    @Decimal
    private BigDecimal chainRatio;
}
