package com.guhecloud.cube.biz.ouaa.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.biz.ouaa.dcm.service.OuaaDcmService;
import com.guhecloud.cube.biz.ouaa.dcm.support.AppDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.AppGroupDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.CardDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.MenuDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.PermissionDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.PositionDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.ProdAppDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.ProductDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.RoleAppDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.RoleDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.RoleMenuDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.RoleTerminalDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.TenantAppDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.TenantProductDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.TenantSubjectPermissionDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.TerminalAppDcmHelper;
import com.guhecloud.cube.biz.ouaa.dcm.support.TerminalDcmHelper;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPES_ORDERED;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_APP_GROUP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_CARD;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_MENU;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PERMISSION;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_POSITION;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PRODUCT;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PROD_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_MENU;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_TERMINAL;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_SUBJECT_PERMISSION;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TENANT_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TENANT_PROD;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TERMINAL;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_TERMINAL_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.PROVIDER_OUAA;

@Slf4j
@RestController
@RequestMapping("/ouaa/dcm/config")
public class OuaaDcmController extends AbstractBaseDcmController {

    private final OuaaDcmService ouaaDcmService;

    public OuaaDcmController(OuaaDcmService ouaaDcmService) {
        this.ouaaDcmService = ouaaDcmService;
    }

    @Override
    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-ouaa.yml";
    }

    @Override
    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return ImmutableMap.of(PROVIDER_OUAA, CONFIG_TYPES_ORDERED);
    }

    @Override
    protected void registerDcmCaps() {
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_APP_GROUP,
                              ouaaDcmService::fetchRuntimeAppGroupConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, AppGroupDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyAppGroupConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_APP,
                              ouaaDcmService::fetchRuntimeAppConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, AppDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyAppConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_PRODUCT,
                              ouaaDcmService::fetchRuntimeProductConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, ProductDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyProductConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_TERMINAL,
                              ouaaDcmService::fetchRuntimeTerminalConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, TerminalDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyTerminalConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_TERMINAL_APP,
                              ouaaDcmService::fetchRuntimeTerminalAppConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, TerminalAppDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyTerminalAppConfig
                             );
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_CARD,
                              ouaaDcmService::fetchRuntimeCardConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, CardDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyCardConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_PROD_APP,
                              ouaaDcmService::fetchRuntimeProdAppConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, ProdAppDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyProdAppConfig);
        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_MENU,
                              ouaaDcmService::fetchRuntimeMenuConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, MenuDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyMenuConfig);

        registerSystemDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_PERMISSION,
                              ouaaDcmService::fetchRuntimePermission,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial,
                                                               new PermissionDcmHelper()),
                              ouaaDcmService::applyPermissionConfig);

        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_TENANT_PROD,
                              ouaaDcmService::fetchRuntimeTenantProdConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, TenantProductDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyTenantProdConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_TENANT_APP,
                              ouaaDcmService::fetchRuntimeTenantAppConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, TenantAppDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyTenantAppConfig);

        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_POSITION,
                              ouaaDcmService::fetchRuntimePositionConfig,
                              (to, from, partial) ->
                                      DcmSupport.compare1dRCIs(to, from, partial, PositionDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyPositionConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_ROLE,
                              ouaaDcmService::fetchRuntimeRoleConfig,
                              (t, f, p) ->
                                      DcmSupport.compare1dRCIs(t, f, p, RoleDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyRoleConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_ROLE_TERMINAL,
                              ouaaDcmService::fetchRuntimeRoleTerminalConfig,
                              (t, f, p) ->
                                      DcmSupport.compare1dRCIs(t, f, p, RoleTerminalDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyRoleTerminalConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_ROLE_APP,
                              ouaaDcmService::fetchRuntimeRoleAppConfig,
                              (t, f, p) ->
                                      DcmSupport.compare1dRCIs(t, f, p, RoleAppDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyRoleAppConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_ROLE_MENU,
                              ouaaDcmService::fetchRuntimeRoleMenuConfig,
                              (t, f, p) ->
                                      DcmSupport.compare1dRCIs(t, f, p, RoleMenuDcmHelper.createDcmRciConverter()),
                              ouaaDcmService::applyRoleMenuConfig);
        registerTenantDcmCaps(PROVIDER_OUAA, CONFIG_TYPE_SUBJECT_PERMISSION,
                              ouaaDcmService::fetchRuntimeTenantSubjectPermission,
                              ((t, f, p) ->
                                      DcmSupport.compare1dRCIs(t, f, p, TenantSubjectPermissionDcmHelper.createDcmRciConverter())),
                              ouaaDcmService::applyTenantSubjectPermissionConfig);
    }

}
