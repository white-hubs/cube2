package com.guhecloud.cube.biz.work.center.dcm.support;

import com.guhecloud.cube.biz.work.center.dcm.entity.DcmWorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.KW_KEY_WORK_TAG_CODE;

public class WorkTagReferBusinessTypeDcmHelper extends AbstractGroupedDcmHelper<WorkTagReferBusinessType, DcmWorkTagReferBusinessType> {

    public WorkTagReferBusinessTypeDcmHelper() {
        super(WorkTagReferBusinessType.class, DcmWorkTagReferBusinessType.class);
    }

    public static IDcmRciConverter<DcmWorkTagReferBusinessType> createDcmRciConverter() {
        return new WorkTagReferBusinessTypeDcmHelper();
    }

    @Override
    public String getBizKey(WorkTagReferBusinessType workTagReferBusinessType) {
        return String.format("%s:%s", workTagReferBusinessType.getWorkTagCode(), workTagReferBusinessType.getBusinessType());
    }

    @Override
    public void updateBizWithDcm(WorkTagReferBusinessType workTagReferBusinessType,
                                 DcmWorkTagReferBusinessType dcmWorkTagReferBusinessType
                                ) {

    }

    @Override
    protected String getConfigType() {
        return WorkDcmSupport.CONFIG_TYPE_WORK_TAG_REF_BUSINESS_TYPE;
    }

    @Override
    protected Object generateGroupKey(DcmWorkTagReferBusinessType dcmWorkTagReferBusinessType) {
        return dcmWorkTagReferBusinessType.getWorkTag();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey,
                                                            Collection<? extends DcmWorkTagReferBusinessType> dcmEntities
                                                           ) {
        RequestedConfigItem rci = getRciTemplate();
        rci.setKwargs(KW_KEY_WORK_TAG_CODE, (String) groupKey);
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmWorkTagReferBusinessType> dcmEntities, RequestedConfigItem rci) {
        String workTag = rci.getKwargs(KW_KEY_WORK_TAG_CODE, null);
        Objects.requireNonNull(workTag, "workTag code is null");
        dcmEntities.forEach(dcm -> dcm.setWorkTag(workTag));
    }

    @Override
    protected void updateBizGroupInfoWithDcm(WorkTagReferBusinessType workTagReferBusinessType,
                                             DcmWorkTagReferBusinessType dcmWorkTagReferBusinessType
                                            ) {
        workTagReferBusinessType.setWorkTagCode(dcmWorkTagReferBusinessType.getWorkTag());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmWorkTagReferBusinessType dcmWorkTagReferBusinessType,
                                             WorkTagReferBusinessType workTagReferBusinessType
                                            ) {
        dcmWorkTagReferBusinessType.setWorkTag(workTagReferBusinessType.getWorkTagCode());
    }

    @Override
    protected DcmWorkTagReferBusinessType dcmFromString(String object) {
        DcmWorkTagReferBusinessType dcmWorkTagReferBusinessType = new DcmWorkTagReferBusinessType();
        dcmWorkTagReferBusinessType.setBusinessType(object);
        return dcmWorkTagReferBusinessType;
    }

    @Override
    public DcmWorkTagReferBusinessType convertBiz2Dcm(WorkTagReferBusinessType workTagReferBusinessType) {
        return new DcmWorkTagReferBusinessType(workTagReferBusinessType.getWorkTagCode(), workTagReferBusinessType.getBusinessType());
    }

    @Override
    public WorkTagReferBusinessType convertDcm2Biz(DcmWorkTagReferBusinessType dcmEntity) {
        return WorkTagReferBusinessType.builder()
                .workTagCode(dcmEntity.getWorkTag())
                .businessType(dcmEntity.getBusinessType())
                .build();
    }
}
