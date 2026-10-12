package com.guhecloud.cube.biz.metric.server.rule.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.metric.server.rule.repository.dao.RuleCoefficientMapper;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleCoefficientPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RuleCoefficientRepositoryImpl implements RuleCoefficientRepository {

    private final RuleCoefficientMapper scoreRangeMapper;

    public RuleCoefficientRepositoryImpl(RuleCoefficientMapper scoreRangeMapper) {this.scoreRangeMapper = scoreRangeMapper;}

    @Override
    public List<RuleCoefficientPO> getRuleCoefficients(Long ruleId) {
        return this.getRuleCoefficients(Collections.singleton(ruleId));
    }

    @Override
    public List<RuleCoefficientPO> getRuleCoefficients(Collection<Long> ruleIds) {
        return this.scoreRangeMapper.selectList(Wrappers.<RuleCoefficientPO>lambdaQuery()
                                                        .in(RuleCoefficientPO::getRuleId, ruleIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRuleCoefficients(List<RuleCoefficientPO> scoreRanges) {
        scoreRanges.forEach(scoreRangeMapper::insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRuleCoefficients(Long ruleId) {
        List<RuleCoefficientPO> scoreRanges = getRuleCoefficients(ruleId);
        if (CollectionUtils.isNotEmpty(scoreRanges)) {
            DatabaseContextHolder.softDelete(RuleCoefficientPO.class, scoreRanges.stream().map(RuleCoefficientPO::getId)
                    .collect(Collectors.toSet()));
        }
    }
}
