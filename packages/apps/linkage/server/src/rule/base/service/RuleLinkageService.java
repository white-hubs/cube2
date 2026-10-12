package com.guhecloud.cube.biz.linkage.server.rule.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;

import java.util.Collection;
import java.util.List;

public interface RuleLinkageService extends IService<RuleLinkage> {

    List<RuleLinkage> listByRuleId(Long ruleId);

    List<RuleLinkage> listByRuleIds(Collection<Long> ruleIds);

    void removeByRuleId(Long ruleId);
}
