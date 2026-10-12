package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleDeviceAlarmDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;

public class RuleDeviceConditionFactory {

    public static RuleDeviceCondition getRuleDeviceCondition(Rule rule, RuleDeviceAlarmDTO ruleDeviceAlarmDTO,
                                                             RuleDeviceConditionDTO ruleDeviceAlarmConfigDTO
                                                            ) {
        return RuleDeviceCondition
                .builder()
                .id(IdWorker.getId())
                .ruleId(rule.getId())
                .areaId(ruleDeviceAlarmConfigDTO.getAreaId())
                .areaName(ruleDeviceAlarmConfigDTO.getAreaName())
                .deviceProdCategoryCode(ruleDeviceAlarmDTO.getDeviceProdCategoryCode())
                .deviceProdCategoryCodeConcat(ruleDeviceAlarmDTO.getDeviceProdCategoryCodeConcat())
                .deviceProdCategoryName(ruleDeviceAlarmDTO.getDeviceProdCategoryName())
                .deviceProdId(ruleDeviceAlarmDTO.getDeviceProdId())
                .deviceProdName(ruleDeviceAlarmDTO.getDeviceProdName())
                .deviceId(ruleDeviceAlarmConfigDTO.getId())
                .deviceCode(ruleDeviceAlarmConfigDTO.getCode())
                .deviceName(ruleDeviceAlarmConfigDTO.getName())
                .propCode(ruleDeviceAlarmDTO.getPropCode())
                .propName(ruleDeviceAlarmDTO.getPropName())
                .op(ruleDeviceAlarmDTO.getOp())
                .oprand(ruleDeviceAlarmDTO.getOprand())
                .delayTime(ruleDeviceAlarmDTO.getDelayTime())
                .build();
    }

    public static RuleDeviceCondition getRuleDeviceCondition(Rule rule, RuleDeviceConditionDTO ruleDeviceCondition) {
        return RuleDeviceCondition
                .builder()
                .id(IdWorker.getId())
                .ruleId(rule.getId())
                .areaId(ruleDeviceCondition.getAreaId())
                .areaName(ruleDeviceCondition.getAreaName())
                .deviceProdCategoryCode(ruleDeviceCondition.getDeviceProdCategoryCode())
                .deviceProdCategoryCodeConcat(ruleDeviceCondition.getDeviceProdCategoryCodeConcat())
                .deviceProdCategoryName(ruleDeviceCondition.getDeviceProdCategoryName())
                .deviceProdId(ruleDeviceCondition.getDeviceProdId())
                .deviceProdName(ruleDeviceCondition.getDeviceProdName())
                .deviceId(ruleDeviceCondition.getId())
                .deviceCode(ruleDeviceCondition.getCode())
                .deviceName(ruleDeviceCondition.getName())
                .propCode(ruleDeviceCondition.getPropCode())
                .propName(ruleDeviceCondition.getPropName())
                .op(ruleDeviceCondition.getOp())
                .oprand(ruleDeviceCondition.getOprand())
                .delayTime(ruleDeviceCondition.getDelayTime())
                .build();
    }
}
