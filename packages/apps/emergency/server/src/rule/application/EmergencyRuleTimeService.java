package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleTimeMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyRuleTimeService extends ServiceImpl<EmergencyRuleTimeMapper, EmergencyRuleTimePO> {

    public Optional<EmergencyRuleTimePO> getEmergencyRuleTimeByRuleGroupId(Long ruleGroupId) {
        List<EmergencyRuleTimePO> emergencyRuleTimePOS = this.baseMapper.selectList(Wrappers.<EmergencyRuleTimePO>lambdaQuery()
                                                                                            .eq(EmergencyRuleTimePO::getGroupId, ruleGroupId));
        return CollectionUtils.isEmpty(emergencyRuleTimePOS) ? Optional.empty() : Optional.of(emergencyRuleTimePOS.getFirst());
    }

    public void removeByRuleIds(Collection<Long> ruleIds) {
        this.baseMapper.delete(Wrappers.<EmergencyRuleTimePO>lambdaQuery().in(EmergencyRuleTimePO::getEmergencyRuleId, ruleIds));
    }
}
