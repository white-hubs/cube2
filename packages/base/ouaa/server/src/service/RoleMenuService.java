package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {

    Long save(Long roleId, Long terminalId, Long applicationId, Long menuId);

    void remove(Long roleId, Long terminalId, Long applicationId, Long menuId);

    void removeByRole(Long roleId);

    void removeByTerminalApplicationMenu(Long terminalId, Long applicationId, Long menuId);

    List<RoleMenu> listByRoleTerminalApplication(Long roleId, Long terminalId, Long applicationId);

    List<RoleMenu> listByRoleTerminalApplication(Long roleId, Long terminalId);

    List<RoleMenu> listByTerminalApplicationMenu(Long terminalId, Long applicationId, Long menuId);

    List<RoleMenu> listByTenant(Long tenant);

    List<RoleMenu> listByRoleTerminalApplicationBatch(Collection<Long> roleIds, Collection<Long> terminalIdList,
                                                      Collection<Long> applicationIdList
                                                     );

    List<RoleMenu> getBindMenus(@NonNull List<Long> roleIds);
}
