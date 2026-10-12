package com.guhecloud.cube.biz.linkage.server.rule.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.rule.base.dao.RuleLinkageMapper;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleLinkageServiceImpl extends ServiceImpl<RuleLinkageMapper, RuleLinkage> implements RuleLinkageService {

    @Override
    public List<RuleLinkage> listByRuleId(@NonNull Long ruleId) {
        return this.lambdaQuery().eq(RuleLinkage::getRuleId, ruleId).list();
    }

    @Override
    public List<RuleLinkage> listByRuleIds(@NonNull Collection<Long> ruleIds) {
        return CollectionUtils.isEmpty(ruleIds) ? Collections.emptyList() : this.lambdaQuery().in(RuleLinkage::getRuleId, ruleIds).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(@NonNull Long ruleId) {
        List<RuleLinkage> ruleLinkageEmergencies = this.listByRuleId(ruleId);
        if (!CollectionUtils.isEmpty(ruleLinkageEmergencies)) {
            DatabaseContextHolder.softDelete(this, ruleLinkageEmergencies);
        }
    }
}
