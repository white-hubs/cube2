package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionExpMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EmergencyRuleConditionExpService extends ServiceImpl<EmergencyRuleConditionExpMapper, EmergencyRuleConditionExpPO> {

    public void removeByRuleIds(Collection<Long> ruleIds) {
        this.baseMapper.delete(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                                       .in(EmergencyRuleConditionExpPO::getEmergencyRuleId, ruleIds));
    }
}
