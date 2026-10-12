package com.guhecloud.cube.biz.work.center.dcm.support;

import com.guhecloud.cube.biz.work.center.dcm.entity.DcmWorkBusinessUrl;
import com.guhecloud.cube.biz.work.center.server.entity.WorkBusinessUrl;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;

import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.CONFIG_TYPE_WORK_BUSINESS_URL;

public class WorkBusinessUrlDcmHelper extends AbstractSimpleDcmHelper<WorkBusinessUrl, DcmWorkBusinessUrl> {

    public WorkBusinessUrlDcmHelper() {
        super(WorkBusinessUrl.class, DcmWorkBusinessUrl.class);
    }

    public static IDcmRciConverter<DcmWorkBusinessUrl> createDcmRciConverter() {
        return new WorkBusinessUrlDcmHelper();
    }

    @Override
    public String getBizKey(WorkBusinessUrl workBusinessUrl) {
        return String.format("%s:%s", workBusinessUrl.getBusinessType(), workBusinessUrl.getTerminal());
    }

    @Override
    public void updateBizWithDcm(WorkBusinessUrl workBusinessUrl, DcmWorkBusinessUrl dcmWorkBusinessUrl) {
        workBusinessUrl.setBusinessTypeName(
                dcmWorkBusinessUrl.getBusinessTypeName() == null ? "" : dcmWorkBusinessUrl.getBusinessTypeName());
        workBusinessUrl.setUrl(dcmWorkBusinessUrl.getUrl() == null ? "" : dcmWorkBusinessUrl.getUrl());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_WORK_BUSINESS_URL;
    }
}
