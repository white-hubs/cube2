package com.guhecloud.cube.biz.emergency.dcm.support;

import com.guhecloud.cube.biz.emergency.dcm.entity.DcmEmergencyType;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_TYPE;

public class EmergencyTypeDcmHelper extends AbstractSimpleDcmHelper<EmergencyType, DcmEmergencyType> {

    public EmergencyTypeDcmHelper() {
        super(EmergencyType.class, DcmEmergencyType.class);
    }

    public static IDcmRciConverter<DcmEmergencyType> createDcmRciConverter() {
        return new EmergencyTypeDcmHelper();
    }

    @Override
    public String getBizKey(EmergencyType bizEntity) {
        return String.format("%s", bizEntity.getApplicationCode());
    }

    @Override
    public void updateBizWithDcm(EmergencyType input, DcmEmergencyType dcmEntity) {
        input.setCode(dcmEntity.getApplicationCode());
        input.setName(dcmEntity.getApplicationName());
        input.setApplicationName(dcmEntity.getApplicationName());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_EM_TYPE;
    }
}
