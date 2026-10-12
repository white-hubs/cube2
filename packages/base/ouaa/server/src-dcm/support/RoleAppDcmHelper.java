package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmRoleApp;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_APP;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_ROLE_NAME;
import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.KW_KEY_TERMINAL_CODE;

public class RoleAppDcmHelper extends AbstractGroupedDcmHelper<RoleApplication, DcmRoleApp> {
    private final RmdpHelperSupportRepo supportRepo;

    public RoleAppDcmHelper(Collection<Product> products, Collection<Role> roles, Collection<Terminal> terminals,
                            Collection<Application> applications
                           ) {
        super(RoleApplication.class, DcmRoleApp.class);
        supportRepo = new RmdpHelperSupportRepo(products, roles, terminals, applications, Collections.emptyList());
    }

    public static IDcmRciConverter<DcmRoleApp> createDcmRciConverter() {
        return new RoleAppDcmHelper(
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    @Override
    protected boolean isTenanted() {
        return true;
    }

    @Override
    protected String getTenantInfo(RoleApplication entity) {
        return String.valueOf(supportRepo.getRole(entity.getRoleId()).getTenant());
    }

    @Override
    public String getBizKey(RoleApplication bizEntity) {
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(bizEntity.getRoleId());
        RmdpHelperSupportRepo.TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        return String.format("%s:%s:%s:%s", roleKey.getProdCode(), roleKey.getRoleName(),
                             terminalKey.getTerminalCode(), appCode);
    }

    @Override
    public void updateBizWithDcm(RoleApplication input, DcmRoleApp dcmEntity) {
        // 关系表， 无需更新
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ROLE_APP;
    }

    @Override
    protected Object generateGroupKey(DcmRoleApp dcmEntity) {
        return new DcmRoleAppGroupKey(dcmEntity.getProdCode(), dcmEntity.getRoleName(),
                                      dcmEntity.getTerminalCode());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmRoleApp> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        DcmRoleAppGroupKey key = (DcmRoleAppGroupKey) groupKey;
        rci.setKwargs(OuaaDcmSupport.KW_KEY_PROD_CODE, key.getProdCode());
        rci.setKwargs(KW_KEY_ROLE_NAME, key.getRoleName());
        rci.setKwargs(KW_KEY_TERMINAL_CODE, key.getTerminalCode());
        rci.setVargs(new ArrayList<>(dcmEntities));
        return rci;
    }

    @Override
    protected DcmRoleApp dcmFromString(String object) {
        DcmRoleApp dra = null;
        try {
            dra = super.dcmFromString(object);
        } catch (RuntimeException ignore) {
            // ignore
        }
        if (dra == null) {
            dra = new DcmRoleApp(null, null, null, object);
        }
        return dra;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmRoleApp> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(OuaaDcmSupport.KW_KEY_PROD_CODE, null);
        Objects.requireNonNull(prodCode, "product name is null");
        String roleName = rci.getKwargs(KW_KEY_ROLE_NAME, null);
        Objects.requireNonNull(roleName, "role name is null");
        String terminalCode = rci.getKwargs(KW_KEY_TERMINAL_CODE, null);
        Objects.requireNonNull(terminalCode, "terminal code is null");
        for (DcmRoleApp dra : dcmEntities) {
            dra.setProdCode(prodCode);
            dra.setRoleName(roleName);
            dra.setTerminalCode(terminalCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(RoleApplication bizEntity, DcmRoleApp dcmEntity) {
        Long roleId = supportRepo.getRoleIdByKey(new RmdpHelperSupportRepo.RoleKey(dcmEntity.getProdCode(), dcmEntity.getRoleName()));
        Long terminalId = supportRepo.getTerminalIdByKey(new RmdpHelperSupportRepo.TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode()));
        Long appId = supportRepo.getAppIdByCode(dcmEntity.getAppCode());
        bizEntity.setRoleId(roleId);
        bizEntity.setTerminalId(terminalId);
        bizEntity.setApplicationId(appId);
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmRoleApp dcmEntity, RoleApplication bizEntity) {
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(bizEntity.getRoleId());
        RmdpHelperSupportRepo.TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        String appCode = supportRepo.getAppCode(bizEntity.getApplicationId());
        dcmEntity.setProdCode(roleKey.getProdCode());
        dcmEntity.setRoleName(roleKey.getRoleName());
        dcmEntity.setTerminalCode(terminalKey.getTerminalCode());
        dcmEntity.setAppCode(appCode);
    }

    @Data
    @AllArgsConstructor
    private static class DcmRoleAppGroupKey {
        String prodCode;
        String roleName;
        String terminalCode;
    }

    @Override
    protected Comparator<DcmRoleApp> getDcmComparator() {
        return Comparator.comparing(DcmRoleApp::getRoleName)
                .thenComparing(DcmRoleApp::getProdCode)
                .thenComparing(DcmRoleApp::getTerminalCode)
                .thenComparing(DcmRoleApp::getAppCode);
    }
}
