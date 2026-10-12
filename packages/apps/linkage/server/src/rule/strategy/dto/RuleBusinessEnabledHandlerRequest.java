package com.guhecloud.cube.biz.linkage.server.rule.strategy.dto;

import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 规则状态
 *
 * @author lanchun
 * @date 2022/6/29 17:14
 */
@Getter
@Setter
public class RuleBusinessEnabledHandlerRequest extends RuleBusinessRequest {

    public RuleBusinessEnabledHandlerRequest(BusinessTypeEnum businessType) {
        super(businessType);
    }

    /**
     * 规则对象
     */
    private Rule rule;

    /**
     * 旧编号
     */
    private String oldCode;
}
