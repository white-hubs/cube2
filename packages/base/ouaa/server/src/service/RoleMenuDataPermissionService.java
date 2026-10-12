package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuDataPermission;
import com.guhecloud.cube.biz.ouaa.server.enumeration.DataPermissionEnum;
import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public interface RoleMenuDataPermissionService extends IService<RoleMenuDataPermission> {

    Long save(Long roleId, Long terminalId, Long applicationId, Long menuId, String permission, String businessKeys);

    Long save(Long roleId, Long terminalId, Long applicationId, Long menuId, DataPermissionEnum dataPermission);

    void remove(Long roleId, Long terminalId, Long applicationId, Long menuId, String permission);

    List<DataPermissionVO> getRoleMenuDataPermission(List<Long> roleIds, Long terminalId,
                                                     Long applicationId,
                                                     Long menuId
                                                    );

    Collection<RoleMenuDataPermission> listByTenant(Long tenant);

    void removeByRoleMenuIds(Collection<Long> recordIds);

    List<RoleMenuDataPermission> getBindDataPermission(@NonNull List<Long> roleIds);
}
