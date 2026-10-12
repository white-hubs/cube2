package com.guhecloud.cube.biz.metric.server.rule.repository;

import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleCoefficientPO;

import java.util.Collection;
import java.util.List;

public interface RuleCoefficientRepository {

    List<RuleCoefficientPO> getRuleCoefficients(Long ruleId);

    List<RuleCoefficientPO> getRuleCoefficients(Collection<Long> ruleIds);

    void saveRuleCoefficients(List<RuleCoefficientPO> scoreRanges);

    void removeRuleCoefficients(Long ruleId);
}
