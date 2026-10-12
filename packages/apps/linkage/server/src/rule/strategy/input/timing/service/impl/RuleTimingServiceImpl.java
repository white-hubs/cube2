package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dao.RuleTimingMapper;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.entity.RuleTiming;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.param.RuleTimingQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.service.RuleTimingService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.vo.RuleTimingPageVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTimingServiceImpl extends ServiceImpl<RuleTimingMapper, RuleTiming> implements RuleTimingService {

    @Override
    public Page<RuleTimingPageVO> page(RuleTimingQueryParam param) {
        return this.baseMapper.page(param, PagingUtils.convert(param));
    }

    @Override
    public RuleTiming getByRuleId(@NonNull Long ruleId) {
        return this.baseMapper.selectOne(Wrappers.<RuleTiming>lambdaQuery().eq(RuleTiming::getRuleId, ruleId));
    }

    @Override
    public void removeByRuleId(@NonNull Long ruleId) {
        RuleTiming ruleTiming = this.getByRuleId(ruleId);
        if (Objects.nonNull(ruleTiming)) {
            DatabaseContextHolder.softDelete(this, ruleTiming);
        }
    }
}
