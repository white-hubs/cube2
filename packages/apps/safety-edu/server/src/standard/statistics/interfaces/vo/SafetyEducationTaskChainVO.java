package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo;

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

    /**
     * 同比
     */
    @Decimal
    private BigDecimal sameRatio;
    /**
     * 参与人数
     */

    private Long participants;
}
