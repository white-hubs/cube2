package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  15:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class RuleParam {
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 区域id
     */
    private String deviceIds;
    /**
     * 背景图
     */
    private Long file;
    /**
     * 指标设置
     */
    private List<RuleNormParam> ruleNormList;
    /**
     * 下发联动规则
     */
    private Long iotRuleId;
    /**指标区域*/
    private String areaOptions;
}
