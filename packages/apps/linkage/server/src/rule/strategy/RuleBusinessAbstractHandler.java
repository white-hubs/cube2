package com.guhecloud.cube.biz.linkage.server.rule.strategy;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessRequest;

public abstract class RuleBusinessAbstractHandler implements RuleBusinessHandler {

    private BusinessTypeEnum businessType;

    public RuleBusinessAbstractHandler(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    @Override
    public boolean supports(RuleBusinessRequest request) {
        return this.businessType == request.getBusinessType();
    }
}
