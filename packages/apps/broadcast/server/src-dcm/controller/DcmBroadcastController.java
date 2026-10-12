package com.guhecloud.cube.biz.subsystem.broadcast.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.biz.subsystem.broadcast.dcm.service.DcmBroadcastService;
import com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastTemplateHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastConstant.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastConstant.CONFIG_TYPE_BROADCAST;
import static com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastConstant.DCM_RESOURCE_PATH;
import static com.guhecloud.cube.biz.subsystem.broadcast.dcm.support.DcmBroadcastConstant.PROVIDER_BROADCAST;

/**
 * 广播中心声明式配置
 */
@Slf4j
@RestController
@RequestMapping("/broadcast/dcm/config")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DcmBroadcastController extends AbstractBaseDcmController {

    private final DcmBroadcastService dcmBroadcastService;

    @Override
    protected String getBuiltinDcmResourcePath() {
        return DCM_RESOURCE_PATH;
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_BROADCAST, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_BROADCAST, CONFIG_TYPE_BROADCAST,
                              dcmBroadcastService::fetchRuntimeBroadcastTemplateConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, DcmBroadcastTemplateHelper.createDcmRciConverter()),
                              dcmBroadcastService::applyBroadcastTemplateConfig);

    }
}
