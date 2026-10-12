package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyConfigPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleConditionExpPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleConditionPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRulePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyConfig;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleCondition;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleConditionExp;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PO ===> to Entity
 */
public class DeviceNotifyRulePOTOEntityConvert {

    private DeviceNotifyRulePOTOEntityConvert() {

    }

    public static DeviceNotifyRule deviceNotifyRuleToEntity(DeviceNotifyRulePO po) {
        if (po == null) {
            return null;
        }
        return DeviceNotifyRule.builder()
                .id(po.getId())
                .deviceId(po.getDeviceId())
                .iotRuleId(po.getIotRuleId())
                .enabled(po.getEnabled())
                .tenant(po.getTenant())
                .build();
    }

    /**
     * 将DeviceNotifyRulePO转换为DeviceNotifyRule实体
     *
     * @param po 设备通知规则数据库对象
     *
     * @return 设备通知规则实体
     */
    public static DeviceNotifyRule deviceNotifyRuleToEntity(DeviceNotifyRulePO po, List<DeviceNotifyRuleConditionPO> conditionPOS,
                                                            List<DeviceNotifyRuleConditionExpPO> expPOS,
                                                            DeviceNotifyConfigPO configPO
                                                           ) {
        if (po == null) {
            return null;
        }

        return DeviceNotifyRule.builder()
                .id(po.getId())
                .deviceId(po.getDeviceId())
                .iotRuleId(po.getIotRuleId())
                .enabled(po.getEnabled())
                .conditions(deviceNotifyRuleConditionToEntity(conditionPOS, expPOS))
                .notifyConfigs(deviceNotifyConfigToEntities(configPO))
                .tenant(po.getTenant())
                .build();
    }

    /**
     * 将DeviceNotifyRuleConditionPO转换为DeviceNotifyRuleCondition实体
     *
     * @param pos 设备通知规则条件数据库对象列表
     *
     * @return 设备通知规则条件实体列表
     */
    public static List<DeviceNotifyRuleCondition> deviceNotifyRuleConditionToEntity(List<DeviceNotifyRuleConditionPO> pos,
                                                                                    List<DeviceNotifyRuleConditionExpPO> expPOS
                                                                                   ) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        var expMap = expPOS.stream()
                .collect(Collectors.groupingBy(DeviceNotifyRuleConditionExpPO::getConditionId));
        return pos.stream()
                .map(po -> deviceNotifyRuleConditionToEntity(po, expMap.getOrDefault(po.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    /**
     * 将DeviceNotifyRuleConditionPO转换为DeviceNotifyRuleCondition实体
     *
     * @param po 设备通知规则条件数据库对象
     *
     * @return 设备通知规则条件实体
     */
    public static DeviceNotifyRuleCondition deviceNotifyRuleConditionToEntity(DeviceNotifyRuleConditionPO po,
                                                                              List<DeviceNotifyRuleConditionExpPO> expPOS
                                                                             ) {
        if (po == null) {
            return null;
        }

        return DeviceNotifyRuleCondition.builder()
                .conditionType(po.getConditionType())
                .deviceEventId(po.getDeviceEventId())
                .deviceEventCode(po.getDeviceEventCode())
                .deviceEventName(po.getDeviceEventName())
                .deviceEventType(po.getDeviceEventType())
                .logicalOp(po.getLogicalOp())
                .conditionExps(deviceNotifyRuleConditionExpToEntity(expPOS))
                .build();
    }

    /**
     * 将DeviceNotifyRuleConditionExpPO转换为DeviceNotifyRuleConditionExp实体
     *
     * @param pos 设备通知规则条件表达式数据库对象列表
     *
     * @return 设备通知规则条件表达式实体列表
     */
    public static List<DeviceNotifyRuleConditionExp> deviceNotifyRuleConditionExpToEntity(List<DeviceNotifyRuleConditionExpPO> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream()
                .map(DeviceNotifyRulePOTOEntityConvert::deviceNotifyRuleConditionExpToEntity)
                .collect(Collectors.toList());
    }

    /**
     * 将DeviceNotifyRuleConditionExpPO转换为DeviceNotifyRuleConditionExp实体
     *
     * @param po 设备通知规则条件表达式数据库对象
     *
     * @return 设备通知规则条件表达式实体
     */
    public static DeviceNotifyRuleConditionExp deviceNotifyRuleConditionExpToEntity(DeviceNotifyRuleConditionExpPO po) {
        if (po == null) {
            return null;
        }

        return DeviceNotifyRuleConditionExp.builder()
                .propCode(po.getPropCode())
                .propName(po.getPropName())
                .propUnit(po.getPropUnit())
                .op(po.getOp())
                .oprand(po.getOprand())
                .delayTime(po.getDelayTime())
                .startDelayTime(po.getStartDelayTime())
                .endDelayTime(po.getEndDelayTime())
                .build();
    }

    /**
     * 将DeviceNotifyConfigPO转换为DeviceNotifyConfig实体
     *
     * @param po 设备通知配置数据库对象
     *
     * @return 设备通知配置实体
     */
    public static List<DeviceNotifyConfig> deviceNotifyConfigToEntities(DeviceNotifyConfigPO po) {
        if (po == null || StringUtils.isBlank(po.getNotifyConfig())) {
            return null;
        }
        return JsonUtils.deserialize(
                po.getNotifyConfig(),
                new TypeReference<>() {
                });
    }
}
