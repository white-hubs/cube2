package com.guhecloud.cube.biz.linkage.server.rule.strategy.dto;

import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageActionModifyParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 规则解析参数
 *
 * @author lanchun
 * @date 2022/6/22 10:26
 */
@Getter
@Setter
public class RuleBusinessIncreaseHandlerRequest extends RuleBusinessRequest {

    public RuleBusinessIncreaseHandlerRequest(BusinessTypeEnum businessType) {
        super(businessType);
    }

    /**
     * 规则配置
     */
    private Rule rule;

    /**
     * 动作类型
     */
    private List<LinkageActionModifyParam> linkageEmergencyActionModifies;
}
