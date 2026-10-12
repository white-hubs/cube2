package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRuleTriggerRecord;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleTriggerRecordPO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DeviceNotifyRuleTriggerRecordPOTOEntityConvert {

    private DeviceNotifyRuleTriggerRecordPOTOEntityConvert() {}

    public static DeviceNotifyRuleTriggerRecord deviceNotifyRuleTriggerRecordToEntity(DeviceNotifyRuleTriggerRecordPO po) {
        if (po == null) {
            return null;
        }
        var entity = new DeviceNotifyRuleTriggerRecord();
        entity.setId(po.getId());
        entity.setRuleId(po.getRuleId());
        entity.setDeviceId(po.getDeviceId());
        entity.setDeviceCode(po.getDeviceCode());
        entity.setDeviceName(po.getDeviceName());
        entity.setProductCategoryName(po.getProductCategoryName());
        entity.setProdName(po.getProdName());
        entity.setTriggerContent(po.getTriggerContent());
        entity.setTriggerTime(po.getTriggerTime());
        entity.setReadStatus(po.getReadStatus());
        return entity;
    }

    public static List<DeviceNotifyRuleTriggerRecord> deviceNotifyRuleTriggerRecordToEntities(List<DeviceNotifyRuleTriggerRecordPO> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(DeviceNotifyRuleTriggerRecordPOTOEntityConvert::deviceNotifyRuleTriggerRecordToEntity).toList();
    }
}
