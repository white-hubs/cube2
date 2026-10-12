package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyConfigPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleConditionExpPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleConditionPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRulePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleEntityTOPOConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRulePOTOEntityConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.mapper.DeviceNotifyConfigMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.mapper.DeviceNotifyRuleConditionExpMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.mapper.DeviceNotifyRuleConditionMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.mapper.DeviceNotifyRuleMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备通知规则仓库实现
 */
@Repository
@RequiredArgsConstructor
public class DeviceNotifyRuleRepository {

    private final DeviceNotifyRuleMapper deviceNotifyRuleMapper;
    private final DeviceNotifyRuleConditionMapper deviceNotifyRuleConditionMapper;
    private final DeviceNotifyRuleConditionExpMapper deviceNotifyRuleConditionExpMapper;
    private final DeviceNotifyConfigMapper deviceNotifyConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long save(DeviceNotifyRule deviceNotifyRule) {
        // 1. 保存主规则
        var rulePO = DeviceNotifyRuleEntityTOPOConvert.deviceNotifyRuleToPO(deviceNotifyRule);
        // 查找是否已存在规则
        var existingRule = deviceNotifyRuleMapper.selectOne(Wrappers.<DeviceNotifyRulePO>lambdaQuery()
                                                                    .eq(DeviceNotifyRulePO::getDeviceId, deviceNotifyRule.getDeviceId()));
        if (existingRule != null) {
            // 更新现有规则
            rulePO.setId(existingRule.getId());
            rulePO.setTenant(existingRule.getTenant());
            rulePO.setDeleted(existingRule.getDeleted());
            EntityAuditor.populateCreateAudit(rulePO,
                                              existingRule.getCreateBy(),
                                              existingRule.getCreatorId(),
                                              existingRule.getCreatorType(),
                                              existingRule.getCreateTime());
            deviceNotifyRuleMapper.updateById(rulePO);
            // 删除旧的条件和表达式
            deleteRuleAssociatedData(existingRule.getId());
        } else {
            // 插入新规则
            deviceNotifyRuleMapper.insert(rulePO);
        }
        var ruleId = rulePO.getId();
        // 2. 保存规则条件
        if (CollectionUtils.isNotEmpty(deviceNotifyRule.getConditions())) {
            for (var condition : deviceNotifyRule.getConditions()) {
                var conditionPO = DeviceNotifyRuleEntityTOPOConvert.deviceNotifyRuleConditionToPO(condition, ruleId);
                deviceNotifyRuleConditionMapper.insert(conditionPO);
                Long conditionId = conditionPO.getId();
                // 3. 保存条件表达式
                if (CollectionUtils.isNotEmpty(condition.getConditionExps())) {
                    for (var exp : condition.getConditionExps()) {
                        var deviceNotifyRuleConditionExpPO = DeviceNotifyRuleEntityTOPOConvert.deviceNotifyRuleConditionExpToPO(exp, ruleId, conditionId);
                        deviceNotifyRuleConditionExpMapper.insert(deviceNotifyRuleConditionExpPO);
                    }
                }
            }
        }
        // 4. 保存通知配置
        if (CollectionUtils.isNotEmpty(deviceNotifyRule.getNotifyConfigs())) {
            var configPO = DeviceNotifyRuleEntityTOPOConvert.deviceNotifyConfigToPO(deviceNotifyRule.getNotifyConfigs(), ruleId, rulePO.getDeviceId());
            deviceNotifyConfigMapper.insert(configPO);
        }
        deviceNotifyRule.setId(ruleId);
        return ruleId;
    }

    public DeviceNotifyRule findByDeviceId(Long deviceId) {
        // 1. 查询主规则
        var rulePO = deviceNotifyRuleMapper.selectOne(Wrappers.<DeviceNotifyRulePO>lambdaQuery()
                                                              .eq(DeviceNotifyRulePO::getDeviceId, deviceId));
        if (rulePO == null) {
            return null;
        }
        // 2. 查询规则条件
        return getDeviceNotifyRuleDetail(rulePO);
    }

    public DeviceNotifyRule findById(Long id) {
        // 1. 查询主规则
        var rulePO = deviceNotifyRuleMapper.selectById(id);
        if (rulePO == null) {
            return null;
        }
        return getDeviceNotifyRuleDetail(rulePO);
    }

    public DeviceNotifyRule findByIotId(Long iotId
                                       ) {
        // 1. 查询主规则
        var rulePO = deviceNotifyRuleMapper.selectOne(Wrappers.<DeviceNotifyRulePO>lambdaQuery()
                                                              .eq(DeviceNotifyRulePO::getIotRuleId, iotId));
        if (rulePO == null) {
            return null;
        }
        return getDeviceNotifyRuleDetail(rulePO);
    }

    public List<DeviceNotifyRule> findPageByDeviceIds(Collection<Long> deviceIds) {
        return deviceNotifyRuleMapper.selectList(Wrappers.<DeviceNotifyRulePO>lambdaQuery()
                                                         .in(DeviceNotifyRulePO::getDeviceId, deviceIds))
                .stream()
                .map(DeviceNotifyRulePOTOEntityConvert::deviceNotifyRuleToEntity)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByDeviceId(Long deviceId) {
        // 查询主规则
        var rulePO = deviceNotifyRuleMapper.selectOne(Wrappers.<DeviceNotifyRulePO>lambdaQuery()
                                                              .eq(DeviceNotifyRulePO::getDeviceId, deviceId));
        if (rulePO != null) {
            Long ruleId = rulePO.getId();
            // 删除所有关联数据
            deleteRuleAssociatedData(ruleId);
            // 删除主规则
            DatabaseContextHolder.softDelete(DeviceNotifyRulePO.class, ruleId);
        }
    }

    /**
     * 删除规则的所有关联数据
     *
     * @param ruleId 规则ID
     */
    private void deleteRuleAssociatedData(Long ruleId) {
        // 删除条件表达式
        var expWrapper = Wrappers.<DeviceNotifyRuleConditionExpPO>lambdaUpdate()
                .set(DeviceNotifyRuleConditionExpPO::getDeleted, System.currentTimeMillis())
                .eq(DeviceNotifyRuleConditionExpPO::getRuleId, ruleId);
        deviceNotifyRuleConditionExpMapper.update(expWrapper);

        // 删除规则条件
        var conditionWrapper = Wrappers.<DeviceNotifyRuleConditionPO>lambdaUpdate()
                .set(DeviceNotifyRuleConditionPO::getDeleted, System.currentTimeMillis())
                .eq(DeviceNotifyRuleConditionPO::getRuleId, ruleId);
        deviceNotifyRuleConditionMapper.delete(conditionWrapper);

        // 删除通知配置
        var configWrapper = Wrappers.<DeviceNotifyConfigPO>lambdaUpdate()
                .set(DeviceNotifyConfigPO::getDeleted, System.currentTimeMillis())
                .eq(DeviceNotifyConfigPO::getRuleId, ruleId);
        deviceNotifyConfigMapper.delete(configWrapper);
    }

    private DeviceNotifyRule getDeviceNotifyRuleDetail(DeviceNotifyRulePO rulePO) {
        // 2. 查询规则条件
        var conditionPOs = deviceNotifyRuleConditionMapper.selectList(Wrappers
                                                                              .<DeviceNotifyRuleConditionPO>lambdaQuery()
                                                                              .eq(DeviceNotifyRuleConditionPO::getRuleId, rulePO.getId()));
        // 3. 查询条件表达式
        var expPOs = deviceNotifyRuleConditionExpMapper.selectList(Wrappers
                                                                           .<DeviceNotifyRuleConditionExpPO>lambdaQuery()
                                                                           .eq(DeviceNotifyRuleConditionExpPO::getRuleId, rulePO.getId()));
        // 4. 查询通知配置
        var configPO = deviceNotifyConfigMapper.selectOne(Wrappers.<DeviceNotifyConfigPO>lambdaQuery()
                                                                  .eq(DeviceNotifyConfigPO::getRuleId, rulePO.getId()));
        // 组装领域对象并返回
        return DeviceNotifyRulePOTOEntityConvert.deviceNotifyRuleToEntity(rulePO, conditionPOs, expPOs, configPO);
    }
}