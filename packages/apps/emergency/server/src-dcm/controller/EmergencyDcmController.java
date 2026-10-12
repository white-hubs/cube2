package com.guhecloud.cube.biz.emergency.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.biz.emergency.dcm.service.EmergencyDcmService;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyEventTypeDcmHelper;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyRuleDcmHelper;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyTypeDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_EVENT_TYPE;
import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_RULE;
import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_EM_TYPE;
import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.biz.emergency.dcm.support.EmergencyDcmSupport.PROVIDER_EMERGENCY;

@Slf4j
@RestController
@RequestMapping("/emergency/dcm/config")
public class EmergencyDcmController extends AbstractBaseDcmController {

    private final EmergencyDcmService emergencyDcmService;

    public EmergencyDcmController(EmergencyDcmService emergencyDcmService) {
        this.emergencyDcmService = emergencyDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-emergency.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_EMERGENCY, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerSystemDcmCaps(PROVIDER_EMERGENCY, CONFIG_EM_TYPE,
                              emergencyDcmService::fetchRuntimeEmergencyTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, EmergencyTypeDcmHelper.createDcmRciConverter()),
                              emergencyDcmService::applyEmergencyTypeConfig);

        registerTenantDcmCaps(PROVIDER_EMERGENCY, CONFIG_EM_EVENT_TYPE,
                              emergencyDcmService::fetchRuntimeEmergencyEventTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, EmergencyEventTypeDcmHelper.createDcmRciConverter()),
                              emergencyDcmService::applyEmergencyEventTypeConfig);

        registerTenantDcmCaps(PROVIDER_EMERGENCY, CONFIG_EM_RULE,
                              emergencyDcmService::fetchRuntimeEmergencyRuleConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, EmergencyRuleDcmHelper.createDcmRciConverter()),
                              emergencyDcmService::applyEmergencyRuleConfig);
    }
}
