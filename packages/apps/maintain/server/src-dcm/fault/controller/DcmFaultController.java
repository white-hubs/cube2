package com.guhecloud.cube.srv.gen.maintain.dcm.fault.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.srv.gen.maintain.dcm.fault.service.DcmFaultService;
import com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultTypeHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultConstant.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultConstant.CONFIG_TYPE_FAULT_TYPE;
import static com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultConstant.DCM_RESOURCE_PATH;
import static com.guhecloud.cube.srv.gen.maintain.dcm.fault.support.DcmFaultConstant.PROVIDER_FAULT;

/**
 * 故障类型声明式配置
 */
@Slf4j
@RestController
@RequestMapping("/fault-type/dcm/config")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DcmFaultController extends AbstractBaseDcmController {

    private final DcmFaultService dcmFaultService;

    @Override
    protected String getBuiltinDcmResourcePath() {
        return DCM_RESOURCE_PATH;
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_FAULT, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_FAULT, CONFIG_TYPE_FAULT_TYPE,
                              dcmFaultService::fetchRuntimeFaultTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, DcmFaultTypeHelper.createDcmRciConverter()),
                              dcmFaultService::applyFaultTypeConfig);
    }
}
