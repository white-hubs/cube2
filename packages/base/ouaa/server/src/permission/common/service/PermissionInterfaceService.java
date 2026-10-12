package com.guhecloud.cube.biz.ouaa.server.permission.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionInterfacePO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.PermissionInterfaceModify;

import java.util.List;
import java.util.Set;

public interface PermissionInterfaceService extends IService<PermissionInterfacePO> {

    void save(PermissionInterfaceModify permissionInterfaceModify);

    List<PermissionInterfacePO> getPermissionInterfacesByCode(String code);

    List<PermissionInterfacePO> getPermissionInterfacesByCodes(Set<String> codes);

    void removeByPermissionCode(String code);
}
