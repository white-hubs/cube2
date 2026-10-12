package com.guhecloud.cube.biz.emergency.server.rule.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionDeviceMapper;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmergencyRuleConditionDeviceService extends ServiceImpl<EmergencyRuleConditionDeviceMapper, EmergencyRuleConditionDevicePO> {

    public Set<Long> getRoleIdsByDeviceIds(Collection<Long> deviceIds) {
        return this.listByDeviceIds(deviceIds)
                .stream()
                .map(EmergencyRuleConditionDevicePO::getEmergencyRuleId)
                .collect(Collectors.toSet());
    }

    public List<EmergencyRuleConditionDevicePO> listByDeviceIds(Collection<Long> deviceIds) {
        return CollectionUtils.isEmpty(deviceIds) ? Collections.emptyList() : this.lambdaQuery()
                .in(EmergencyRuleConditionDevicePO::getDeviceId, deviceIds)
                .list();
    }

    public List<EmergencyRuleConditionDevicePO> listByProductId(Long productId) {
        return this.lambdaQuery().eq(EmergencyRuleConditionDevicePO::getDeviceProdId, productId).list();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByDeviceIds(@NonNull Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = this.listByDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(emergencyRuleConditionDevicePOS)) {
            return;
        }
        DatabaseContextHolder.softDelete(this, emergencyRuleConditionDevicePOS);
    }

}
