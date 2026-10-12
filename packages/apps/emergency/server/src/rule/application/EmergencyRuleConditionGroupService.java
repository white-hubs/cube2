package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class EmergencyRuleConditionGroupService extends ServiceImpl<EmergencyRuleConditionGroupMapper, EmergencyRuleConditionGroupPO> {

    public void removeByRuleIds(Collection<Long> ruleIds) {
        this.baseMapper.delete(Wrappers.<EmergencyRuleConditionGroupPO>lambdaQuery()
                                       .in(EmergencyRuleConditionGroupPO::getEmergencyRuleId, ruleIds));
    }
}
