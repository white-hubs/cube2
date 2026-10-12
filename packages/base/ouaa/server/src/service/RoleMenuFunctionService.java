package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuFunction;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public interface RoleMenuFunctionService extends IService<RoleMenuFunction> {

    Long save(Long roleId, Long terminalId, Long applicationId, Long menuId, Long functionId);

    List<RoleMenuFunction> listRoleMenuFunctions(Long roleId);

    List<RoleMenuFunction> listRoleMenuFunctions(Long roleId, Long menuId);

    List<RoleMenuFunction> listRoleMenuFunctions(Collection<Long> roleIds);

    void remove(Long roleId, Long menuId, Long terminalId, Long applicationId, Long functionId);

    void publishRoleAuthority(Collection<Long> roleIds);

    List<MenuFunctionVO> getRoleMenuFunctions(List<Long> roleIds,
                                              Long terminalId,
                                              Long applicationId,
                                              Long menuId
                                             );

    List<RoleMenuFunction> listByTenant(Long tenantId);

    void removeByRoleMenuIds(Collection<Long> recordIds);

    List<RoleMenuFunction> getBindFunctions(@NonNull List<Long> roleIds);
}
