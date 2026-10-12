package com.guhecloud.cube.biz.linkage.server.rule.strategy.dto;

import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RuleBusinessEventHandlerRequest extends RuleBusinessRequest {

    public RuleBusinessEventHandlerRequest(BusinessTypeEnum businessType) {
        super(businessType);
    }

    /**
     * 规则
     */
    private Rule rule;

    /**
     * 触发时间
     */
    private LocalDateTime triggerTime;

    /***
     * 触发的json对象
     */
    private String triggerJson;
}
