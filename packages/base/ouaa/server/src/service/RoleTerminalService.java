package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;

import java.util.Collection;
import java.util.List;

public interface RoleTerminalService extends IService<RoleTerminal> {

    Long save(Long roleId, Long terminalId);

    void remove(Long roleId, Long terminalId);

    void removeByRole(Long roleId);

    void removeByTerminal(Long terminalId);

    List<RoleTerminal> listByRole(Long roleId);

    List<RoleTerminal> listByRoleIds(Collection<Long> roleIds);

    List<RoleTerminal> listByTerminal(Long terminalId);

    List<RoleTerminal> listByTenant(Long tenant);
}
