package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.DcmRoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
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

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_ROLE_TERMINAL;

public class RoleTerminalDcmHelper extends AbstractGroupedDcmHelper<RoleTerminal, DcmRoleTerminal> {

    public static final String KW_KEY_PRODUCT = "prodCode";
    public static final String KW_KEY_ROLE = "roleName";

    private RmdpHelperSupportRepo supportRepo;

    public RoleTerminalDcmHelper(Collection<Product> products, Collection<Role> roles, Collection<Terminal> terminals) {
        super(RoleTerminal.class, DcmRoleTerminal.class);
        supportRepo = new RmdpHelperSupportRepo(products, roles, terminals, Collections.emptyList(), Collections.emptyList());

    }

    public static IDcmRciConverter<DcmRoleTerminal> createDcmRciConverter() {
        return new RoleTerminalDcmHelper(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    // ================ Simple Dcm Helper Functions ===================
    @Override
    public String getBizKey(RoleTerminal bizEntity) {
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(bizEntity.getRoleId());
        Objects.requireNonNull(roleKey, "role id invalid");
        RmdpHelperSupportRepo.TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        Objects.requireNonNull(terminalKey, "terminal id invalid");
        if (!Objects.equals(roleKey.getProdCode(), terminalKey.getProdCode())) {
            throw new RuntimeException("role/terminal product not match");
        }
        return String.format("%s:%s:%s", roleKey.getProdCode(), roleKey.getRoleName(), terminalKey.getTerminalCode());
    }

    @Override
    public void updateBizWithDcm(RoleTerminal input, DcmRoleTerminal dcmEntity) {
        // 除开key 外， 没有别的信息需要更新.
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_ROLE_TERMINAL;
    }

    // ============ Grouped DcmHelper Information =================

    @Override
    protected Object generateGroupKey(DcmRoleTerminal dcmEntity) {
        return new DcmRoleTerminalGroupKey(dcmEntity.getProdCode(), dcmEntity.getRoleName());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DcmRoleTerminal> dcmEntities) {
        RequestedConfigItem requestedConfigItem = getRciTemplate();
        DcmRoleTerminalGroupKey key = (DcmRoleTerminalGroupKey) groupKey;
        requestedConfigItem.setKwargs(KW_KEY_PRODUCT, key.getProdCode());
        requestedConfigItem.setKwargs(KW_KEY_ROLE, key.getRoleName());
        requestedConfigItem.setVargs(new ArrayList<>(dcmEntities));
        return requestedConfigItem;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DcmRoleTerminal> dcmEntities, RequestedConfigItem rci) {
        String prodCode = rci.getKwargs(KW_KEY_PRODUCT, null);
        Objects.requireNonNull(prodCode, "product info invalid");
        String roleName = rci.getKwargs(KW_KEY_ROLE, null);
        Objects.requireNonNull(roleName, "role info invalid");
        for (DcmRoleTerminal dcmRoleTerminal : dcmEntities) {
            dcmRoleTerminal.setProdCode(prodCode);
            dcmRoleTerminal.setRoleName(roleName);
        }
    }

    @Override
    protected DcmRoleTerminal dcmFromString(String terminal) {
        return new DcmRoleTerminal(null, null, terminal);
    }

    @Override
    protected void updateBizGroupInfoWithDcm(RoleTerminal bizEntity, DcmRoleTerminal dcmEntity) {
        RmdpHelperSupportRepo.RoleKey roleKey = new RmdpHelperSupportRepo.RoleKey(dcmEntity.getProdCode(), dcmEntity.getRoleName());
        RmdpHelperSupportRepo.TerminalKey terminalKey = new RmdpHelperSupportRepo.TerminalKey(dcmEntity.getProdCode(), dcmEntity.getTerminalCode());
        // nothing really need to update
        bizEntity.setTerminalId(supportRepo.getTerminalIdByKey(terminalKey));
        bizEntity.setRoleId(supportRepo.getRoleIdByKey(roleKey));
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DcmRoleTerminal dcmEntity, RoleTerminal bizEntity) {
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(bizEntity.getRoleId());
        RmdpHelperSupportRepo.TerminalKey terminalKey = supportRepo.getTerminalKey(bizEntity.getTerminalId());
        dcmEntity.setProdCode(roleKey.getProdCode());
        dcmEntity.setRoleName(roleKey.getRoleName());
        dcmEntity.setTerminalCode(terminalKey.getTerminalCode());
    }

    // =============== Tenanted support ==================
    @Override
    protected boolean isTenanted() {
        return true;
    }

    @Override
    protected String getTenantInfo(RoleTerminal entity) {
        Role role = supportRepo.getRole(entity.getRoleId());
        return String.valueOf(role.getTenant());
    }

    @Data
    @AllArgsConstructor
    static
    class DcmRoleTerminalGroupKey {
        private String prodCode;
        private String roleName;
    }

    @Override
    protected Comparator<DcmRoleTerminal> getDcmComparator() {
        return Comparator.comparing(DcmRoleTerminal::getRoleName)
                .thenComparing(DcmRoleTerminal::getProdCode)
                .thenComparing(DcmRoleTerminal::getTerminalCode);
    }
}
