package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@EventBusListen(order = 1)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisAuthorityServiceImpl {

    private final RoleMenuFunctionService roleMenuFunctionService;

    private final RoleService roleService;

    /**
     * 初始化角色权限
     *
     * @param event
     *
     * @throws Exception
     */
    @Subscribe
    @AllowConcurrentEvents
    public void loadRule(SysReadEvent event) throws Exception {
        List<Long> roleIds = roleService.list().stream().map(Role::getId).collect(Collectors.toList());
        roleMenuFunctionService.publishRoleAuthority(roleIds);
    }
}
