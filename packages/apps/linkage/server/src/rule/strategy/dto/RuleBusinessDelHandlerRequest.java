package com.guhecloud.cube.biz.linkage.server.rule.strategy.dto;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 规则删除处理
 *
 * @author lanchun
 * @date 2022/6/23 11:01
 */
@Getter
@Setter
public class RuleBusinessDelHandlerRequest extends RuleBusinessRequest {

    public RuleBusinessDelHandlerRequest(BusinessTypeEnum businessType) {
        super(businessType);
    }

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 规则编号
     */
    private String ruleCode;
}
