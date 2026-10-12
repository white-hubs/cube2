package com.guhecloud.cube.base.alarm.dcm.support;

import com.guhecloud.cube.base.alarm.dcm.entity.DcmAlarmReason;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmReasonConfigPO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.base.alarm.dcm.support.AlarmDcmSupport.CONFIG_ALARM_REASON;

public class AlarmReasonDcmHelper extends AbstractSimpleDcmHelper<AlarmReasonConfigPO, DcmAlarmReason> {

    public AlarmReasonDcmHelper() {
        super(AlarmReasonConfigPO.class, DcmAlarmReason.class);
    }

    public static IDcmRciConverter<DcmAlarmReason> createDcmRciConverter() {
        return new AlarmReasonDcmHelper();
    }

    @Override
    public String getBizKey(AlarmReasonConfigPO alarmReasonConfigPO) {
        return String.format("%s", alarmReasonConfigPO.getCode());
    }

    @Override
    public void updateBizWithDcm(AlarmReasonConfigPO alarmReasonConfigPO, DcmAlarmReason dcmAlarmReason) {
        alarmReasonConfigPO.setReason(dcmAlarmReason.getReason());
        alarmReasonConfigPO.setInitialized(
                dcmAlarmReason.getInitialized() == null ? Boolean.TRUE : dcmAlarmReason.getInitialized());
        alarmReasonConfigPO.setRemark(dcmAlarmReason.getRemark() == null ? "" : dcmAlarmReason.getRemark());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_ALARM_REASON;
    }
}
