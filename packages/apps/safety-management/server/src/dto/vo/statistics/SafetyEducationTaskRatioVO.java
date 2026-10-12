package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SafetyEducationTaskRatioVO {

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 周期内总数
     */
    private Integer periodTotalNum;

    /**
     * 占比
     */
    private BigDecimal ratio;
}
