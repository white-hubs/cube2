package com.guhecloud.cube.srv.gen.maintain.dcm.fault.support;

import com.guhecloud.cube.srv.gen.maintain.dcm.fault.entity.DcmFaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DcmFaultTypeHelper extends AbstractSimpleDcmHelper<FaultType, DcmFaultType> {

    public DcmFaultTypeHelper() {
        super(FaultType.class, DcmFaultType.class);
    }

    public static IDcmRciConverter<DcmFaultType> createDcmRciConverter() {
        return new DcmFaultTypeHelper();
    }

    @Override
    public String getBizKey(FaultType bizEntity) {
        return bizEntity.getName();
    }

    @Override
    public void updateBizWithDcm(FaultType input, DcmFaultType dcmEntity) {

    }

    @Override
    protected String getConfigType() {
        return DcmFaultConstant.CONFIG_TYPE_FAULT_TYPE;
    }
}
