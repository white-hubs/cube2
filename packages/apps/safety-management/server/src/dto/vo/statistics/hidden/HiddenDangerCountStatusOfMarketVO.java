package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 市场隐患处理状态数统计
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HiddenDangerCountStatusOfMarketVO extends HiddenDangerCountStatusVO {
    private String tenantID;
    private String tenantName;
}
