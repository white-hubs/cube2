package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleConditionService extends ServiceImpl<EmergencyRuleConditionMapper, EmergencyRuleConditionPO> {

    public void removeByRuleIds(Collection<Long> ruleIds) {
        this.baseMapper.delete(Wrappers.<EmergencyRuleConditionPO>lambdaQuery()
                                       .in(EmergencyRuleConditionPO::getEmergencyRuleId, ruleIds));
    }

    public List<EmergencyRuleConditionPO> listByProductId(Long productId) {
        return this.baseMapper.selectList(Wrappers.<EmergencyRuleConditionPO>lambdaQuery()
                                                  .eq(EmergencyRuleConditionPO::getDeviceProdId, productId));
    }
}
