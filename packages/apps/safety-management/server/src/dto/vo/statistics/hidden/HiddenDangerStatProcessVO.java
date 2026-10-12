package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden;

import lombok.Data;

import java.util.Map;

/**
 * 市场隐患数按 status 分类统计
 */
@Data
public class HiddenDangerStatProcessVO {
    private String tenantID;
    private String tenantName;
    /**
     * Map<status, count>
     */
    private Map<String, Integer> countStatus;
}
