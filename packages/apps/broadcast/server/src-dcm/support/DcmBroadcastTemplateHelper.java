package com.guhecloud.cube.biz.subsystem.broadcast.dcm.support;

import com.guhecloud.cube.biz.subsystem.broadcast.dcm.entity.DcmBroadcastTemplate;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DcmBroadcastTemplateHelper extends AbstractSimpleDcmHelper<BroadcastTemplatePO, DcmBroadcastTemplate> {
    public DcmBroadcastTemplateHelper() {
        super(BroadcastTemplatePO.class, DcmBroadcastTemplate.class);
    }

    public static IDcmRciConverter<DcmBroadcastTemplate> createDcmRciConverter() {
        return new DcmBroadcastTemplateHelper();
    }

    @Override
    public String getBizKey(BroadcastTemplatePO broadcastTemplatePO) {
        return broadcastTemplatePO.getName();
    }

    @Override
    public void updateBizWithDcm(BroadcastTemplatePO broadcastTemplatePO, DcmBroadcastTemplate dcmBroadcastTemplate) {
        broadcastTemplatePO.setContent(dcmBroadcastTemplate.getContent() == null ? "" : dcmBroadcastTemplate.getContent());
        broadcastTemplatePO.setDefaulted(dcmBroadcastTemplate.getDefaulted() == null || dcmBroadcastTemplate.getDefaulted());
    }

    @Override
    protected String getConfigType() {
        return DcmBroadcastConstant.CONFIG_TYPE_BROADCAST;
    }
}
