package com.guhecloud.cube.base.flow.dcm.controller;

import com.guhecloud.cube.base.flow.dcm.service.FlowDcmService;
import com.guhecloud.cube.base.flow.dcm.support.FlowDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.base.flow.dcm.support.FlowDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.base.flow.dcm.support.FlowDcmSupport.CONFIG_TYPE_FORM;
import static com.guhecloud.cube.base.flow.dcm.support.FlowDcmSupport.PROVIDER_FLOW;

/**
 * 流引擎申明式配置
 *
 * @Author: lijie
 * @CreateTime: 2024-04-25  14:53
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/flow/dcm/config")
public class FlowDcmController extends AbstractBaseDcmController {
    private final FlowDcmService flowDcmService;

    public FlowDcmController(FlowDcmService flowDcmService) {
        this.flowDcmService = flowDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-flow-form.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        Map<String, List<String>> supportedProviderMap = new HashMap<>(1);
        supportedProviderMap.put(PROVIDER_FLOW, CONFIG_TYPES_ORDERED);
        return supportedProviderMap;
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_FLOW, CONFIG_TYPE_FORM,
                              flowDcmService::fetchRuntimeAppGroupConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, FlowDcmHelper.createDcmRciConverter()),
                              flowDcmService::applyAppGroupConfig);
    }

}
