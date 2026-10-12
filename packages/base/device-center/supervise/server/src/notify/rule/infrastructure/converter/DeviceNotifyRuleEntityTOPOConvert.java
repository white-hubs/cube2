package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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

import java.util.List;

/**
 * entity ====> to po
 */
public class DeviceNotifyRuleEntityTOPOConvert {

    private DeviceNotifyRuleEntityTOPOConvert() {}

    /**
     * 将DeviceNotifyRule实体转换为DeviceNotifyRulePO
     *
     * @param entity 设备通知规则实体
     *
     * @return 设备通知规则数据库对象
     */
    public static DeviceNotifyRulePO deviceNotifyRuleToPO(DeviceNotifyRule entity) {
        if (entity == null) {
            return null;
        }
        DeviceNotifyRulePO po = new DeviceNotifyRulePO();
        po.setId(entity.getId());
        po.setIotRuleId(entity.getIotRuleId());
        po.setDeviceId(entity.getDeviceId());
        po.setEnabled(entity.getEnabled());
        return po;
    }

    /**
     * 将DeviceNotifyRuleCondition实体转换为DeviceNotifyRuleConditionPO
     *
     * @param entity 设备通知规则条件实体
     * @param ruleId 设备通知规则ID
     *
     * @return 设备通知规则条件数据库对象
     */
    public static DeviceNotifyRuleConditionPO deviceNotifyRuleConditionToPO(DeviceNotifyRuleCondition entity, Long ruleId) {
        if (entity == null) {
            return null;
        }
        return DeviceNotifyRuleConditionPO.builder()
                .id(IdWorker.getId())
                .conditionType(entity.getConditionType())
                .deviceEventId(entity.getDeviceEventId())
                .deviceEventCode(entity.getDeviceEventCode())
                .deviceEventName(entity.getDeviceEventName())
                .deviceEventType(entity.getDeviceEventType())
                .logicalOp(entity.getLogicalOp())
                .ruleId(ruleId)
                .build();
    }

    /**
     * 将DeviceNotifyRuleConditionExp实体转换为DeviceNotifyRuleConditionExpPO
     *
     * @param entity      设备通知规则条件表达式实体
     * @param ruleId      设备通知规则ID
     * @param conditionId 设备通知规则条件ID
     *
     * @return 设备通知规则条件表达式数据库对象
     */
    public static DeviceNotifyRuleConditionExpPO deviceNotifyRuleConditionExpToPO(DeviceNotifyRuleConditionExp entity, Long ruleId,
                                                                                  Long conditionId
                                                                                 ) {
        if (entity == null) {
            return null;
        }

        return DeviceNotifyRuleConditionExpPO.builder()
                .propCode(entity.getPropCode())
                .propName(entity.getPropName())
                .propUnit(entity.getPropUnit())
                .op(entity.getOp())
                .oprand(entity.getOprand())
                .delayTime(entity.getDelayTime())
                .startDelayTime(entity.getStartDelayTime())
                .endDelayTime(entity.getEndDelayTime())
                .ruleId(ruleId)
                .conditionId(conditionId)
                .build();
    }

    /**
     * 将DeviceNotifyConfig实体转换为DeviceNotifyConfigPO
     *
     * @param entities 设备通知配置实体
     * @param ruleId   设备通知规则ID
     * @param deviceId 设备ID
     *
     * @return 设备通知配置数据库对象
     */
    public static DeviceNotifyConfigPO deviceNotifyConfigToPO(List<DeviceNotifyConfig> entities, Long ruleId, Long deviceId) {
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        DeviceNotifyConfigPO po = new DeviceNotifyConfigPO();
        po.setRuleId(ruleId);
        po.setDeviceId(deviceId);
        po.setNotifyConfig(JsonUtils.serialize(entities));
        return po;
    }
}
