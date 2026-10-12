package com.guhecloud.cube.biz.emergency.dcm.support;

import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_EVENT_TYPE;

public class EmergencyEventTypeDcmHelper extends AbstractSimpleDcmHelper<EmergencyEventType, DcmEmergencyEventType> {

    public EmergencyEventTypeDcmHelper() {
        super(EmergencyEventType.class, DcmEmergencyEventType.class);
    }

    public static IDcmRciConverter<DcmEmergencyEventType> createDcmRciConverter() {
        return new EmergencyEventTypeDcmHelper();
    }

    @Override
    public String getBizKey(EmergencyEventType bizEntity) {
        return String.format("%s", bizEntity.getName());
    }

    @Override
    public void updateBizWithDcm(EmergencyEventType input, DcmEmergencyEventType dcmEntity) {
        input.setName(dcmEntity.getName());
        input.setInitialized(dcmEntity.getInitialized() == null ? Boolean.TRUE : dcmEntity.getInitialized());
        input.setRemark(dcmEntity.getRemark() == null ? "" : dcmEntity.getRemark());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_EM_EVENT_TYPE;
    }
}
