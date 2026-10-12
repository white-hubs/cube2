package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmRoleMenu;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.MenuKey;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.RoleKey;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo.RoleMenuKey;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_MENU;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_APP_CODE;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_PARENT;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_ROLE_NAME;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_TERMINAL_CODE;

public class RoleMenuDcmHelper extends AbstractGroupedDcmHelper<RoleMenu, DcmRoleMenu> {
    private final RmdpHelperSupportRepo supportRepo;
    private final RoleMenuService roleMenuService;

    public RoleMenuDcmHelper(Collection<Product> products, Collection<Role> roles,
                             Collection<Terminal> terminals, Collection<Application> applications,
                             Collection<Menu> menus,
                             RoleMenuService roleMenuService
                            ) {
        super(RoleMenu.class, DcmRoleMenu.class);
        supportRepo = new RmdpHelperSupportRepo(products, roles, terminals, applications, menus);
        this.roleMenuService = roleMenuService;
    }

    public static IDcmRciConverter<DcmRoleMenu> createDcmRciConverter() {
        return new RoleMenuDcmHelper(
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), null);
    }

    @Override
    protected boolean isTenanted() {
        return true;
    }

    @Override
    protected String getTenantInfo(RoleMenu entity) {
        Role role = supportRepo.getRole(entity.getRoleId());
        return String.valueOf(role.getTenant());
    }

    @Override
    public String getBizKey(RoleMenu bizEntity) {
        RoleMenuKey roleMenuKey = supportRepo.getRoleMenuKey(bizEntity.getRoleId(), bizEntity.getMenuId());
        return String.format("%s:%s:%s:%s:%s:%s", roleMenuKey.getProdCode(), roleMenuKey.getRoleName(),
                             roleMenuKey.getTerminalCode(), roleMenuKey.getAppCode(),
                             roleMenuKey.getParentCode(), roleMenuKey.getMenuCode());
    }

    @Override
    public void updateBizWithDcm(RoleMenu input, DcmRoleMenu dcmEntity) {
        // 关系表， 无需更新
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ROLE_MENU;
    }

    @Override
    protected Object generateGroupKey(DcmRoleMenu dcmEntity) {
        return new DcmRoleMenuGroupKey(dcmEntity.getProdCode(), dcmEntity.getRoleName(),
                                       dcmEntity.getTerminalCode(), dcmEntity.getAppCode(), dcmEntity.getParentCode());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmRoleMenu> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        DcmRoleMenuGroupKey key = (DcmRoleMenuGroupKey) groupKey;
        rci.setKwargs(OuaaDcmSupport.KW_KEY_PROD_CODE, key.getProdCode());
        rci.setKwargs(KW_KEY_ROLE_NAME, key.getRoleName());
        rci.setKwargs(KW_KEY_TERMINAL_CODE, key.getTerminalCode());
        rci.setKwargs(KW_KEY_APP_CODE, key.getAppCode());
        if (key.getParentCode() != null && !key.getParentCode().isEmpty()) {
            rci.setKwargs(KW_KEY_PARENT, key.getParentCode());
        }
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected DcmRoleMenu dcmFromString(String object) {
        DcmRoleMenu dra = null;
        try {
            dra = super.dcmFromString(object);
        } catch (RuntimeException ignore) {
            // ignore
        }
        if (dra == null) {
            dra = DcmRoleMenu.builder().menuCode(object).build();
        }
        return dra;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmRoleMenu> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(OuaaDcmSupport.KW_KEY_PROD_CODE, null);
        Objects.requireNonNull(prodCode, "product name is null");
        String roleName = rci.getKwargs(KW_KEY_ROLE_NAME, null);
        Objects.requireNonNull(roleName, "role name is null");
        String terminalCode = rci.getKwargs(KW_KEY_TERMINAL_CODE, null);
        Objects.requireNonNull(terminalCode, "terminal code is null");
        String appCode = rci.getKwargs(KW_KEY_APP_CODE, null);
        Objects.requireNonNull(appCode, "app code is null");
        String parentCode = rci.getKwargs(KW_KEY_PARENT, "");

        for (DcmRoleMenu drm : dcmEntities) {
            drm.setProdCode(prodCode);
            drm.setRoleName(roleName);
            drm.setTerminalCode(terminalCode);
            drm.setAppCode(appCode);
            drm.setParentCode(parentCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(RoleMenu bizEntity, DcmRoleMenu dcmEntity) {
        Long roleId = supportRepo.getRoleIdByKey(new RoleKey(dcmEntity.getProdCode(), dcmEntity.getRoleName()));
        bizEntity.setRoleId(roleId);

        MenuKey menuKey = MenuKey.builder()
                .prodCode(dcmEntity.getProdCode())
                .terminalCode(dcmEntity.getTerminalCode())
                .appCode(dcmEntity.getAppCode())
                .parentCode(dcmEntity.getParentCode())
                .menuCode(dcmEntity.getMenuCode())
                .build();
        Long menuId = supportRepo.getMenuIdByKey(menuKey);
        Menu menu = supportRepo.getMenuById(menuId);
        bizEntity.setTerminalId(menu.getTerminalId());
        bizEntity.setApplicationId(menu.getApplicationId());
        bizEntity.setMenuId(menuId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmRoleMenu dcmEntity, RoleMenu bizEntity) {
        RoleMenuKey roleMenuKey = supportRepo.getRoleMenuKey(bizEntity.getRoleId(), bizEntity.getMenuId());
        dcmEntity.setProdCode(roleMenuKey.getProdCode());
        dcmEntity.setRoleName(roleMenuKey.getRoleName());
        dcmEntity.setTerminalCode(roleMenuKey.getTerminalCode());
        dcmEntity.setAppCode(roleMenuKey.getAppCode());
        dcmEntity.setParentCode(roleMenuKey.getParentCode());
        dcmEntity.setMenuCode(roleMenuKey.getMenuCode());
    }

    /**
     * 前置条件: biz entities 都是有id的
     */
    public int batchUpdate(Collection<RoleMenu> bizEntities, Collection<DcmRoleMenu> dcmEntities) {
        // 批量保存:简单删除, 再全量保存
        List<Long> recordIds = bizEntities.stream().map(RoleMenu::getId).collect(Collectors.toList());
        roleMenuService.updateBatchById(bizEntities, 100);
        return bizEntities.size();
    }

    public int batchRemove(Collection<RoleMenu> bizEntities, Collection<DcmRoleMenu> dcmEntities) {
        log.debug("batch remove {} roleMenu/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        List<Long> prodIds2Remove = bizEntities.stream().map(RoleMenu::getId).collect(Collectors.toList());
        roleMenuService.removeByIds(prodIds2Remove);
        log.debug("batch removed {} records", bizEntities.size());
        return prodIds2Remove.size();
    }

    public int batchAdd(Collection<RoleMenu> bizEntities, Collection<DcmRoleMenu> dcmEntities) {
        log.debug("batch add {} products/{} dcmEntities.", bizEntities.size(), dcmEntities.size());
        // 批量新增
        roleMenuService.saveBatch(bizEntities, 100);
        return bizEntities.size();
    }

    @Data
    @AllArgsConstructor
    private static class DcmRoleMenuGroupKey {
        String prodCode;
        String roleName;
        String terminalCode;
        String appCode;
        String parentCode;
    }

    @Override
    protected Comparator<DcmRoleMenu> getDcmComparator() {
        return Comparator.comparing(DcmRoleMenu::getRoleName)
                .thenComparing(DcmRoleMenu::getProdCode)
                .thenComparing(DcmRoleMenu::getTerminalCode)
                .thenComparing(DcmRoleMenu::getAppCode)
                .thenComparing(x -> x.getParentCode() == null ? "" : x.getParentCode())
                .thenComparing(DcmRoleMenu::getMenuCode);
    }
}
