package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRuleTriggerRecord;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleTriggerRecordVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DeviceNotifyRuleTriggerRecordEntityTOVOConvert {

    private DeviceNotifyRuleTriggerRecordEntityTOVOConvert() {}

    public static DeviceNotifyRuleTriggerRecordVO deviceNotifyRuleTriggerRecordToVO(DeviceNotifyRuleTriggerRecord entity) {
        if (entity == null) {
            return null;
        }
        var vo = new DeviceNotifyRuleTriggerRecordVO();
        vo.setId(entity.getId());
        vo.setRuleId(entity.getRuleId());
        vo.setDeviceId(entity.getDeviceId());
        vo.setDeviceCode(entity.getDeviceCode());
        vo.setDeviceName(entity.getDeviceName());
        vo.setProductCategoryName(entity.getProductCategoryName());
        vo.setProdName(entity.getProdName());
        vo.setTriggerContent(entity.getTriggerContent());
        vo.setTriggerTime(entity.getTriggerTime());
        vo.setReadStatus(entity.getReadStatus());
        return vo;
    }

    public static List<DeviceNotifyRuleTriggerRecordVO> deviceNotifyRuleTriggerRecordToVOS(List<DeviceNotifyRuleTriggerRecord> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(DeviceNotifyRuleTriggerRecordEntityTOVOConvert::deviceNotifyRuleTriggerRecordToVO).toList();
    }
}
