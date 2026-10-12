package com.guhecloud.cube.biz.ouaa.dcm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationGroupService;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.CardService;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuDataPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchRuntimeConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchTenantedRuntimeConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.treeApplyDcmConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OuaaDcmServiceImpl implements OuaaDcmService {
    private ApplicationGroupService applicationGroupService;

    private ApplicationService applicationService;

    private ProductService productService;

    private TerminalService terminalService;

    private CardService cardService;

    private ProductApplicationService productApplicationService;

    private MenuService menuService;

    private RoleService roleService;

    private RoleTerminalService roleTerminalService;

    private RoleMenuDataPermissionService roleMenuDataPermissionService;

    private RoleApplicationService roleApplicationService;

    private RoleMenuService roleMenuService;

    private TerminalApplicationService terminalApplicationService;

    private PositionService positionService;

    private FunctionService functionService;

    private MenuFunctionService menuFunctionService;

    private RoleMenuFunctionService roleMenuFunctionService;

    private TenantProductService tenantProductService;

    private TenantApplicationService tenantApplicationService;

    private PermissionService permissionService;

    private SubjectPermissionService subjectPermissionService;

    @Override
    public List<RequestedConfigItem> fetchRuntimeAppGroupConfig() {
        return fetchRuntimeConfig(applicationGroupService, new AppGroupDcmHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyAppGroupConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, applicationGroupService, new AppGroupDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeAppConfig() {
        return fetchRuntimeConfig(applicationService, new AppDcmHelper(applicationGroupService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyAppConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, applicationService, new AppDcmHelper(applicationGroupService.list()));
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeProductConfig() {
        return fetchRuntimeConfig(productService, new ProductDcmHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyProductConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, productService, new ProductDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTerminalConfig() {
        return fetchRuntimeConfig(terminalService, new TerminalDcmHelper(productService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyTerminalConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, terminalService, new TerminalDcmHelper(productService.list()));
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeCardConfig() {
        return fetchRuntimeConfig(cardService, new CardDcmHelper(productService.list(), terminalService.list(),
                                                                 applicationService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyCardConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, cardService,
                              new CardDcmHelper(productService.list(), terminalService.list(), applicationService.list()));
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeProdAppConfig() {
        return fetchRuntimeConfig(productApplicationService,
                                  new ProdAppDcmHelper(productService.list(), applicationService.list()));
    }

    @Override
    public List<RequestedConfigItem> applyProdAppConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, productApplicationService, new ProdAppDcmHelper(productService.list(), applicationService.list()));
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeMenuConfig() {
        return fetchRuntimeConfig(menuService,
                                  new MenuDcmHelper(productService.list(), terminalService.list(),
                                                    applicationService.list(), menuService.list()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyMenuConfig(List<RequestedConfigItem> diffs2Apply) {
        return treeApplyDcmConfig(diffs2Apply, menuService, new MenuDcmHelper(
                productService.list(), terminalService.list(),
                applicationService.list(), menuService.list()));
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeRoleConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(
                roleService, new RoleDcmHelper(productService.list()))));
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyRoleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () ->
                applyDcmConfig(diffs2Apply, roleService,
                               new RoleDcmHelper(productService.list())));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeRoleTerminalConfig(Long tenant) {
        List<RequestedConfigItem> rcis = new ArrayList<>();
        executeWithTenant(tenant, () ->
                rcis.addAll(
                        fetchRuntimeConfig(
                                roleTerminalService.listByTenant(tenant),
                                new RoleTerminalDcmHelper(
                                        productService.list(),
                                        roleService.list(),
                                        terminalService.list()))));
        return rcis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyRoleTerminalConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, roleTerminalService,
                                                       new RoleTerminalDcmHelper(productService.list(), roleService.list(), terminalService.list())));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeRoleAppConfig(Long tenant) {
        List<RequestedConfigItem> rcis = new ArrayList<>();
        executeWithTenant(tenant, () -> rcis.addAll(
                fetchRuntimeConfig(roleApplicationService.listByTenant(tenant), newRoleAppDcmHelper())));
        return rcis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyRoleAppConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, roleApplicationService, newRoleAppDcmHelper()));
        return diffs2Apply;
    }

    private RoleAppDcmHelper newRoleAppDcmHelper() {
        return new RoleAppDcmHelper(productService.list(), roleService.list(), terminalService.list(), applicationService.list());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeRoleMenuConfig(Long tenant) {
        try {
            List<RequestedConfigItem> rcis = new ArrayList<>();
            executeWithTenant(tenant, () -> {
                rcis.addAll(fetchRuntimeConfig(roleMenuService.listByTenant(tenant), newRoleMenuDcmHelper()));
            });
            return rcis;
        } catch (Exception e) {
            log.error("fetch role menu config error: {}", e, e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyRoleMenuConfig(List<RequestedConfigItem> diffs2Apply, Long tenantId) {
        executeWithTenant(tenantId, () -> {
            RoleMenuDcmHelper dcmHelper = newRoleMenuDcmHelper();
            DcmSupport.applyDcmConfig(diffs2Apply,
                                      () -> roleMenuService.listByTenant(tenantId),
                                      (b, d) -> dcmHelper.batchAdd(b, d),
                                      (b, d) -> dcmHelper.batchUpdate(b, d),
                                      (b, d) -> dcmHelper.batchRemove(b, d),
                                      dcmHelper);
        });
        return diffs2Apply;
    }

    private RoleMenuDcmHelper newRoleMenuDcmHelper() {
        return new RoleMenuDcmHelper(productService.list(), roleService.list(),
                                     terminalService.list(), applicationService.list(), menuService.list(),
                                     roleMenuService);
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimePositionConfig(Long tenant) {
        return fetchTenantedRuntimeConfig(tenant, positionService, new PositionDcmHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyPositionConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, positionService, new PositionDcmHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTerminalAppConfig() {
        return fetchRuntimeConfig(terminalApplicationService, newTerminalAppDcmHelper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyTerminalAppConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, terminalApplicationService, newTerminalAppDcmHelper());
    }

    private TerminalAppDcmHelper newTerminalAppDcmHelper() {
        return new TerminalAppDcmHelper(productService.list(), terminalService.list(), applicationService.list());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTenantProdConfig(Long tenantId) {
        return fetchRuntimeConfig(tenantProductService.listByTenant(tenantId.intValue()),
                                  newTenantProductHelper());
    }

    private TenantProductDcmHelper newTenantProductHelper() {
        return new TenantProductDcmHelper(productService.list(), tenantProductService);
    }

    @Override
    public List<RequestedConfigItem> applyTenantProdConfig(List<RequestedConfigItem> diffs, Long tenantId) {
        TenantProductDcmHelper helper = newTenantProductHelper();
        return DcmSupport.applyDcmConfig(diffs,
                                         () -> tenantProductService.listByTenant(tenantId.intValue()),
                                         (b, d) -> helper.batchAdd(b, d, tenantId),
                                         (b, d) -> helper.batchUpdate(b, d, tenantId),
                                         (b, d) -> helper.batchRemove(b, d, tenantId),
                                         helper
                                        );
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTenantAppConfig(Long tenantId) {
        return fetchRuntimeConfig(tenantApplicationService.listByTenant(tenantId.intValue()),
                                  newTenantAppHelper());
    }

    private TenantAppDcmHelper newTenantAppHelper() {
        return new TenantAppDcmHelper(productService.list(), applicationService.list(), tenantApplicationService);
    }

    @Override
    public List<RequestedConfigItem> applyTenantAppConfig(List<RequestedConfigItem> diffs, Long tenantId) {
        TenantAppDcmHelper helper = newTenantAppHelper();
        Integer tenant = tenantId.intValue();
        return DcmSupport.applyDcmConfig(diffs,
                                         () -> tenantApplicationService.listByTenant(tenant),
                                         (b, d) -> helper.batchAdd(b, d, tenant),
                                         (b, d) -> helper.batchUpdate(b, d, tenant),
                                         (b, d) -> helper.batchRemove(b, d, tenant),
                                         helper
                                        );
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimePermission() {
        return fetchRuntimeConfig(permissionService, new PermissionDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyPermissionConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, permissionService, new PermissionDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeTenantSubjectPermission(Long tenant) {
        List<RequestedConfigItem> rcis = new ArrayList<>();
        executeWithTenant(tenant, () -> {
            rcis.addAll(fetchRuntimeConfig(subjectPermissionService.list(
                                                   Wrappers.lambdaQuery(SubjectPermissionPO.class).eq(SubjectPermissionPO::getSubjectType, SubjectTypeEnum.ROLE)),
                                           new TenantSubjectPermissionDcmHelper(productService.list(), roleService.list())));
        });
        return rcis;
    }

    @Override
    public List<RequestedConfigItem> applyTenantSubjectPermissionConfig(List<RequestedConfigItem> diffs2Apply, Long tenantId) {
        executeWithTenant(tenantId, () -> {
            TenantSubjectPermissionDcmHelper dcmHelper = new TenantSubjectPermissionDcmHelper(productService.list(), roleService.list());
            DcmSupport.applyDcmConfig(diffs2Apply,
                                      () -> subjectPermissionService.list(
                                              Wrappers.lambdaQuery(SubjectPermissionPO.class)
                                                      .eq(SubjectPermissionPO::getSubjectType, SubjectTypeEnum.ROLE)),
                                      (bizEntities, dcmEntities) -> subjectPermissionService.saveBatch(bizEntities) ? bizEntities.size()
                                                                                                                    : 0,
                                      (bizEntities, dcmEntities) -> subjectPermissionService.updateBatchById(bizEntities)
                                                                    ? bizEntities.size() : 0,
                                      (bizEntities, dcmEntities) -> subjectPermissionService.removeByIds(bizEntities.stream()
                                                                                                                 .map(Entity::getId)
                                                                                                                 .collect(Collectors.toList()))
                                                                    ? bizEntities.size() : 0,

                                      dcmHelper);
        });
        return diffs2Apply;
    }
}
