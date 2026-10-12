package com.guhecloud.cube.biz.work.center.dcm.support;

import com.guhecloud.cube.biz.work.center.dcm.entity.DcmWorkTag;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTag;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

public class WorkTagDcmHelper extends AbstractSimpleDcmHelper<WorkTag, DcmWorkTag> {

    public WorkTagDcmHelper() {
        super(WorkTag.class, DcmWorkTag.class);
    }

    public static IDcmRciConverter<DcmWorkTag> createDcmRciConverter() {
        return new WorkTagDcmHelper();
    }

    @Override
    public String getBizKey(WorkTag workTag) {
        return workTag.getCode();
    }

    @Override
    public void updateBizWithDcm(WorkTag workTag, DcmWorkTag dcmWorkTag) {
        workTag.setName(dcmWorkTag.getName() == null ? "" : dcmWorkTag.getName());
    }

    @Override
    protected String getConfigType() {
        return WorkDcmSupport.CONFIG_TYPE_WORK_TAG;
    }
}
