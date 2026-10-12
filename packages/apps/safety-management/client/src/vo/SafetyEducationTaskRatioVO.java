package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 17:04
 * @Description:
 */
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
