package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRuleTriggerRecord;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.po.DeviceNotifyRuleTriggerRecordPO;

public class DeviceNotifyRuleTriggerRecordEntityTOPOConvert {

    private DeviceNotifyRuleTriggerRecordEntityTOPOConvert() {}

    public static DeviceNotifyRuleTriggerRecordPO deviceNotifyRuleTriggerRecordToPO(DeviceNotifyRuleTriggerRecord entity) {
        if (entity == null) {
            return null;
        }
        var po = new DeviceNotifyRuleTriggerRecordPO();
        po.setId(entity.getId());
        po.setRuleId(entity.getRuleId());
        po.setDeviceId(entity.getDeviceId());
        po.setDeviceCode(entity.getDeviceCode());
        po.setDeviceName(entity.getDeviceName());
        po.setProductCategoryName(entity.getProductCategoryName());
        po.setProdName(entity.getProdName());
        po.setTriggerContent(entity.getTriggerContent());
        po.setTriggerTime(entity.getTriggerTime());
        po.setReadStatus(entity.getReadStatus());
        return po;
    }
}
