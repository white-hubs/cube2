package com.guhecloud.cube.biz.ouaa.dcm.support;

import com.guhecloud.cube.biz.ouaa.dcm.entity.Permission;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionPO;
import com.guhecloud.platform.dcm.support.AbstractSimpleDcmHelper;

import java.util.Comparator;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.dcm.support.OuaaDcmSupport.CONFIG_TYPE_PERMISSION;

public class PermissionDcmHelper extends AbstractSimpleDcmHelper<PermissionPO, Permission> {

    public PermissionDcmHelper() {
        super(PermissionPO.class, Permission.class);
    }

    @Override
    public String getBizKey(PermissionPO permissionPO) {
        return permissionPO.getCode();
    }

    @Override
    public void updateBizWithDcm(PermissionPO permissionPO, Permission permission) {
        Objects.requireNonNull(permissionPO.getId());
        permissionPO.setType(permission.getType());
        permissionPO.setName(permission.getName());
        permissionPO.setDescription(permission.getDescription());
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_PERMISSION;
    }

    @Override
    protected Comparator<Permission> getDcmComparator() {
        return Comparator
                .comparing(Permission::getType)
                .thenComparing(Permission::getCode);
    }

}
