package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyConfig;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleCondition;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleConditionExp;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyConfigVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleConditionExpVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleConditionVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.MessageTargetNotifyVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备通知规则实体转换为VO的转换器
 */
public class DeviceNotifyRuleEntityTOVOConvert {

    private DeviceNotifyRuleEntityTOVOConvert() {}

    /**
     * 将DeviceNotifyRule实体转换为DeviceNotifyRuleVO
     *
     * @param entity 设备通知规则实体
     *
     * @return 设备通知规则VO
     */
    public static DeviceNotifyRuleVO deviceNotifyRuleToVO(DeviceNotifyRule entity) {
        if (entity == null) {
            return null;
        }

        var vo = new DeviceNotifyRuleVO();
        vo.setId(entity.getId());
        vo.setDeviceId(entity.getDeviceId());
        vo.setEnabled(entity.getEnabled());
        vo.setRuleConditions(deviceNotifyRuleConditionToVOS(entity.getConditions()));
        vo.setMessageNotifies(CollectionUtils.isNotEmpty(entity.getNotifyConfigs()) ?
                              deviceNotifyConfigTOVOS(entity.getNotifyConfigs()) : null);
        return vo;
    }

    /**
     * 将DeviceNotifyRuleCondition实体转换为DeviceNotifyRuleConditionVO
     *
     * @param entities 设备通知规则条件实体
     *
     * @return 设备通知规则条件VO
     */
    public static List<DeviceNotifyRuleConditionVO> deviceNotifyRuleConditionToVOS(
            List<DeviceNotifyRuleCondition> entities
                                                                                  ) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(DeviceNotifyRuleEntityTOVOConvert::deviceNotifyRuleConditionToVO)
                .collect(Collectors.toList());
    }

    /**
     * 将DeviceNotifyRuleCondition实体转换为DeviceNotifyRuleConditionVO
     *
     * @param entity 设备通知规则条件实体
     *
     * @return 设备通知规则条件VO
     */
    public static DeviceNotifyRuleConditionVO deviceNotifyRuleConditionToVO(DeviceNotifyRuleCondition entity) {
        if (entity == null) {
            return null;
        }
        var vo = new DeviceNotifyRuleConditionVO();
        vo.setConditionType(entity.getConditionType());
        vo.setDeviceEventId(entity.getDeviceEventId());
        vo.setDeviceEventCode(entity.getDeviceEventCode());
        vo.setDeviceEventName(entity.getDeviceEventName());
        vo.setDeviceEventType(entity.getDeviceEventType());
        vo.setLogicalOp(entity.getLogicalOp());
        vo.setRuleConditionExps(deviceNotifyRuleConditionExpToVOS(entity.getConditionExps()));
        return vo;
    }

    /**
     * 将DeviceNotifyRuleConditionExp实体转换为DeviceNotifyRuleConditionExpVO
     *
     * @param entities 设备通知规则条件表达式实体
     *
     * @return 设备通知规则条件表达式VO
     */
    public static List<DeviceNotifyRuleConditionExpVO> deviceNotifyRuleConditionExpToVOS(
            List<DeviceNotifyRuleConditionExp> entities
                                                                                        ) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(DeviceNotifyRuleEntityTOVOConvert::deviceNotifyRuleConditionExpToVO)
                .collect(Collectors.toList());
    }

    /**
     * 将DeviceNotifyRuleConditionExp实体转换为DeviceNotifyRuleConditionExpVO
     *
     * @param entity 设备通知规则条件表达式实体
     *
     * @return 设备通知规则条件表达式VO
     */
    public static DeviceNotifyRuleConditionExpVO deviceNotifyRuleConditionExpToVO(DeviceNotifyRuleConditionExp entity) {
        if (entity == null) {
            return null;
        }
        var vo = new DeviceNotifyRuleConditionExpVO();
        vo.setPropCode(entity.getPropCode());
        vo.setPropName(entity.getPropName());
        vo.setPropUnit(entity.getPropUnit());
        vo.setOp(entity.getOp());
        vo.setOprand(entity.getOprand());
        vo.setDelayTime(entity.getDelayTime());
        vo.setStartDelayTime(entity.getStartDelayTime());
        vo.setEndDelayTime(entity.getEndDelayTime());
        return vo;
    }

    /**
     * 将DeviceNotifyConfig实体转换为DeviceNotifyConfigVO
     *
     * @param entities 设备通知配置实体
     *
     * @return 设备通知配置VO
     */
    public static List<DeviceNotifyConfigVO> deviceNotifyConfigTOVOS(List<DeviceNotifyConfig> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        return entities.stream().map(entity -> {
            var vo = new DeviceNotifyConfigVO();
            vo.setChannelTypes(entity.getChannelTypes());
            if (CollectionUtils.isNotEmpty(entity.getTargets())) {
                vo.setTargets(entity.getTargets().stream().map(target -> {
                    var targetVO = new MessageTargetNotifyVO();
                    targetVO.setTargetConfig(target.getTargetConfig());
                    targetVO.setMessageTargetNotify(target.getMessageTargetNotify());
                    return targetVO;
                }).collect(Collectors.toList()));
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
