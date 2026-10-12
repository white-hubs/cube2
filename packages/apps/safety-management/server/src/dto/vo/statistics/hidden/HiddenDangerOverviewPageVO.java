package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden;

import lombok.Data;

/**
 * @author wql
 * @describe 隐患处理概览统计
 * @date 2023/9/20 14:25
 */
@Data
public class HiddenDangerOverviewPageVO {
    /**
     * 市场租户号
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 隐患数量
     */
    private Integer hiddenDangerNum;
}
