package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.Data;

/**
 * 风险源状态计数
 */
@Data
public class RiskSourceCountStatusVO {
    private String status;

    private Integer count;
}
