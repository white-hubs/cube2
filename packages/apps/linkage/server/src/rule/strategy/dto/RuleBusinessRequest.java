package com.guhecloud.cube.biz.linkage.server.rule.strategy.dto;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;

public abstract class RuleBusinessRequest {

    private BusinessTypeEnum businessType;

    public RuleBusinessRequest(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

}
