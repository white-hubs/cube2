package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RolePermission;

import java.util.List;

public interface RolePermissionService extends IService<RolePermission> {

    Long save(Long roleId, Long permissionId);

    void remove(Long roleId, Long permissionId);

    void removeByRole(Long roleId);

    void removeByPermission(Long permissionId);

    List<RolePermission> listByRole(Long roleId);

    List<RolePermission> listByPermission(Long permissionId);

}
