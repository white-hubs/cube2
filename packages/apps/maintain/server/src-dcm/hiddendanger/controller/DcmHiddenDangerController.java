package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.service.DcmHiddenDangerService;
import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerCategoryHelper;
import com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerTypeHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.CONFIG_TYPE_HIDDEN_DANGER_CATEGORY;
import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.CONFIG_TYPE_HIDDEN_DANGER_TYPE;
import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.DCM_RESOURCE_PATH;
import static com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.support.DcmHiddenDangerConstant.PROVIDER_HIDDEN_DANGER;

/**
 * 隐患整改声明式配置
 */
@Slf4j
@RestController
@RequestMapping("/hidden-danger/dcm/config")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DcmHiddenDangerController extends AbstractBaseDcmController {

    private final DcmHiddenDangerService dcmHiddenDangerService;

    @Override
    protected String getBuiltinDcmResourcePath() {
        return DCM_RESOURCE_PATH;
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_HIDDEN_DANGER, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerTenantDcmCaps(PROVIDER_HIDDEN_DANGER, CONFIG_TYPE_HIDDEN_DANGER_CATEGORY,
                              dcmHiddenDangerService::fetchRuntimeHiddenDangerCategoryConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, DcmHiddenDangerCategoryHelper.createDcmRciConverter()),
                              dcmHiddenDangerService::applyHiddenDangerCategoryConfig);
        registerTenantDcmCaps(PROVIDER_HIDDEN_DANGER, CONFIG_TYPE_HIDDEN_DANGER_TYPE,
                              dcmHiddenDangerService::fetchRuntimeHiddenDangerTypeConfig,
                              (to, from, partial) -> DcmSupport.compare1dRCIs(to, from, partial, DcmHiddenDangerTypeHelper.createDcmRciConverter()),
                              dcmHiddenDangerService::applyHiddenDangerTypeConfig);
    }
}
