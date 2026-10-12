package com.guhecloud.cube.biz.work.center.dcm.service.impl;

import com.guhecloud.cube.biz.work.center.dcm.service.WorkDcmService;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkBusinessUrlDcmHelper;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkTagDcmHelper;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkTagReferBusinessTypeDcmHelper;
import com.guhecloud.cube.biz.work.center.server.service.WorkBusinessUrlService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagReferBusinessTypeService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchRuntimeConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WorkDcmServiceImpl implements WorkDcmService {

    private WorkTagService workTagService;

    private WorkTagReferBusinessTypeService workTagReferBusinessTypeService;

    private final WorkBusinessUrlService workBusinessUrlService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeWorkTagConfig() {
        return fetchRuntimeConfig(workTagService, new WorkTagDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyWorkTagConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, workTagService, new WorkTagDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeWorkTagRefBusinessTypeConfig() {
        return fetchRuntimeConfig(workTagReferBusinessTypeService, new WorkTagReferBusinessTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyWorkTagRefBusinessTypeConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, workTagReferBusinessTypeService, new WorkTagReferBusinessTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeWorkBusinessUrConfig() {
        return fetchRuntimeConfig(workBusinessUrlService, new WorkBusinessUrlDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyWorkBusinessUrlConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, workBusinessUrlService, new WorkBusinessUrlDcmHelper());
    }
}
