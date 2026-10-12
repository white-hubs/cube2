package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  15:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class RulePageVO {
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 关联区域个数
     */
    private String areaNumber;
    /**
     * 产品id
     */
    private String productId;
    private String deviceIds;
    /**
     * 关联指标个数
     */
    private String ruleNormNumber;

}
