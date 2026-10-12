package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleDisablePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleDisableMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class EmergencyRuleDisableService extends ServiceImpl<EmergencyRuleDisableMapper, EmergencyRuleDisablePO> {

    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleId(Long ruleId) {
        List<EmergencyRuleDisablePO> emergencyRuleDisablePOS = this.getEmergencyRuleDisableListByRuleIds(Collections.singleton(ruleId));
        if (!CollectionUtils.isEmpty(emergencyRuleDisablePOS)) {
            DatabaseContextHolder.softDelete(this, emergencyRuleDisablePOS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByRuleIds(Collection<Long> ruleIds) {
        List<EmergencyRuleDisablePO> emergencyRuleDisablePOS = this.getEmergencyRuleDisableListByRuleIds(ruleIds);
        if (!CollectionUtils.isEmpty(emergencyRuleDisablePOS)) {
            DatabaseContextHolder.softDelete(this, emergencyRuleDisablePOS);
        }
    }

    public List<EmergencyRuleDisablePO> getEmergencyRuleDisableList(LocalDateTime endTime
                                                                   ) {
        return this.baseMapper.selectList(Wrappers.<EmergencyRuleDisablePO>lambdaQuery()
                                                  .le(EmergencyRuleDisablePO::getOpenTime, endTime)
                                         );
    }

    public List<EmergencyRuleDisablePO> getEmergencyRuleDisableListByRuleIds(Collection<Long> ruleIds) {
        return this.baseMapper.selectList(Wrappers.<EmergencyRuleDisablePO>lambdaQuery()
                                                  .in(EmergencyRuleDisablePO::getRuleId, ruleIds)
                                         );
    }

}
