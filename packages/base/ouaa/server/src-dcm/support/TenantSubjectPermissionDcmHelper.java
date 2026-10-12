package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.SubjectPermission;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.exception.DcmRuntimeException;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_SUBJECT_PERMISSION;

public class TenantSubjectPermissionDcmHelper extends AbstractGroupedDcmHelper<SubjectPermissionPO, SubjectPermission> {

    private final RmdpHelperSupportRepo supportRepo;

    public TenantSubjectPermissionDcmHelper(Collection<Product> products,
                                            Collection<Role> roles
                                           ) {
        super(SubjectPermissionPO.class, SubjectPermission.class);
        supportRepo = new RmdpHelperSupportRepo(products, roles,
                                                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TenantSubjectPermissionDcmHelper createDcmRciConverter() {
        return new TenantSubjectPermissionDcmHelper(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    protected Object generateGroupKey(SubjectPermission dcmEntity) {
        return GroupKey.builder()
                .subjectType(dcmEntity.getSubjectType())
                .subjectCode(dcmEntity.getSubjectCode())
                .build();
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey,
                                                            Collection<? extends SubjectPermission> dcmEntities
                                                           ) {
        if (groupKey instanceof GroupKey) {
            GroupKey groupKeyObj = (GroupKey) groupKey;
            RequestedConfigItem rci = getRciTemplate();
            rci.setIsSystem(Boolean.FALSE);
            rci.setKwargs("type", groupKeyObj.getSubjectType());
            rci.setKwargs("code", groupKeyObj.getSubjectCode());
            rci.setVargs(new ArrayList<>(dcmEntities));
            return rci;
        } else {
            return null;
        }
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends SubjectPermission> dcmEntities, RequestedConfigItem rci) {
        if (rci.getKwargs().containsKey("type") && rci.getKwargs().containsKey("code")) {
            String subjectType = rci.getKwargs().get("type");

            if (!SubjectTypeEnum.ROLE.name().equals(subjectType)) {
                throw new DcmRuntimeException("Subject Type is not supported: " + subjectType);
            }

            String subjectCode = rci.getKwargs("code", null);
            for (SubjectPermission dcmEntity : dcmEntities) {
                dcmEntity.setSubjectCode(subjectCode);
                dcmEntity.setSubjectType(subjectType);
            }
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(SubjectPermissionPO bizEntity, SubjectPermission dcmEntity) {
        String subjectType = dcmEntity.getSubjectType();
        if (!SubjectTypeEnum.ROLE.name().equals(subjectType)) {
            throw new DcmRuntimeException("Subject Type is not supported: " + subjectType);
        }
        String subjectCode = dcmEntity.getSubjectCode();
        String[] parts = subjectCode.split(":");
        if (parts.length != 2) {
            throw new DcmRuntimeException("Invalid subject code: " + subjectCode);
        }
        String productCode = parts[0];
        String roleName = parts[1];
        Long roleId = supportRepo.getRoleIdByKey(new RmdpHelperSupportRepo.RoleKey(productCode, roleName));
        bizEntity.setSubjectId(String.valueOf(roleId));
        bizEntity.setSubjectType(SubjectTypeEnum.ROLE);
        bizEntity.setPermissionExtentConfig(dcmEntity.getExtentConfig());
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(SubjectPermission dcmEntity, SubjectPermissionPO bizEntity) {
        if (!SubjectTypeEnum.ROLE.equals(bizEntity.getSubjectType())) {
            throw new DcmRuntimeException("Subject Type is not supported: " + bizEntity.getSubjectType().name());
        }
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(Long.valueOf(bizEntity.getSubjectId()));
        dcmEntity.setSubjectCode(roleKey.getProdCode() + ":" + roleKey.getRoleName());
        dcmEntity.setSubjectType(SubjectTypeEnum.ROLE.name());
        dcmEntity.setExtentConfig(bizEntity.getPermissionExtentConfig());
    }

    @Override
    public String getBizKey(SubjectPermissionPO bizEntity) {
        if (!Objects.equals(SubjectTypeEnum.ROLE, bizEntity.getSubjectType())) {
            throw new DcmRuntimeException("Unsupported subject type: " + bizEntity.getSubjectType().name());
        }
        RmdpHelperSupportRepo.RoleKey roleKey = supportRepo.getRoleKey(Long.valueOf(bizEntity.getSubjectId()));
        return String.format("%s:%s:%s:%s", bizEntity.getSubjectType()
                .name(), roleKey.getProdCode(), roleKey.getRoleName(), bizEntity.getPermissionCode());
    }

    @Override
    public void updateBizWithDcm(SubjectPermissionPO input, SubjectPermission dcmEntity) {
        input.setPermissionExtentConfig(dcmEntity.getExtentConfig());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_SUBJECT_PERMISSION;
    }

    @Builder
    @Data
    private static class GroupKey {
        private String subjectType;
        private String subjectCode;
    }

    @Override
    protected Comparator<SubjectPermission> getDcmComparator() {
        return Comparator.comparing(SubjectPermission::getSubjectType)
                .thenComparing(SubjectPermission::getSubjectCode)
                .thenComparing(SubjectPermission::getPermissionCode);
    }
}
