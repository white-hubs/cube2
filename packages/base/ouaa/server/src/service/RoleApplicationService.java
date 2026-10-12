package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;

import java.util.List;

public interface RoleApplicationService extends IService<RoleApplication> {

    Long save(Long roleId, Long terminalId, Long applicationId);

    void remove(Long roleId, Long terminalId, Long applicationId);

    void removeByRole(Long roleId);

    void removeByTerminalApplication(Long terminalId, Long applicationId);

    List<RoleApplication> listByRoleTerminal(Long roleId, Long terminalId);

    List<RoleApplication> listByTerminalApplication(Long terminalId, Long applicationId);

    List<RoleApplication> listByTenant(Long tenant);
}
