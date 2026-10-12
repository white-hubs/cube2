package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.entity.RuleTiming;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.param.RuleTimingQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.vo.RuleTimingPageVO;

public interface RuleTimingService extends IService<RuleTiming> {

    Page<RuleTimingPageVO> page(RuleTimingQueryParam param);

    RuleTiming getByRuleId(Long ruleId);

    void removeByRuleId(Long ruleId);
}
