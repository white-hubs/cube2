package com.guhecloud.cube.biz.work.center.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.biz.work.center.dcm.service.WorkDcmService;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkBusinessUrlDcmHelper;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkTagDcmHelper;
import com.guhecloud.cube.biz.work.center.dcm.support.WorkTagReferBusinessTypeDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.CONFIG_TYPE_WORK_BUSINESS_URL;
import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.CONFIG_TYPE_WORK_TAG;
import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.CONFIG_TYPE_WORK_TAG_REF_BUSINESS_TYPE;
import static com.guhecloud.cube.biz.work.center.dcm.support.WorkDcmSupport.PROVIDER_WORK;

@Slf4j
@RestController
@RequestMapping("/work/dcm/config")
public class WorkDcmController extends AbstractBaseDcmController {

    private final WorkDcmService workDcmService;

    public WorkDcmController(WorkDcmService workDcmService) {
        this.workDcmService = workDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-work.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_WORK, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerSystemDcmCaps(PROVIDER_WORK, CONFIG_TYPE_WORK_TAG,
                              workDcmService::fetchRuntimeWorkTagConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, WorkTagDcmHelper.createDcmRciConverter()),
                              workDcmService::applyWorkTagConfig);
        registerSystemDcmCaps(PROVIDER_WORK, CONFIG_TYPE_WORK_TAG_REF_BUSINESS_TYPE,
                              workDcmService::fetchRuntimeWorkTagRefBusinessTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, WorkTagReferBusinessTypeDcmHelper.createDcmRciConverter()),
                              workDcmService::applyWorkTagRefBusinessTypeConfig);
        registerSystemDcmCaps(PROVIDER_WORK, CONFIG_TYPE_WORK_BUSINESS_URL,
                              workDcmService::fetchRuntimeWorkBusinessUrConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, WorkBusinessUrlDcmHelper.createDcmRciConverter()),
                              workDcmService::applyWorkBusinessUrlConfig);
    }

}
