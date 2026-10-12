package com.guhecloud.cube.biz.ouaa.server.permission;

import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.UserPermissionRefreshMessage;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class RolePermissionRefreshAspect {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserTenantService userTenantService;

    @Pointcut(
            "execution(* com.guhecloud.cube.biz.ouaa.server.permission.role.controller.RolePermissionController.updateRolePermission(..)) ||" +
            "execution(* com.guhecloud.cube.biz.ouaa.server.permission.role.controller.RolePermissionController.updateRolePermissionExtendConfig(..))"
    )
    public void point() {
        // This method is used to define the pointcut expression for some method in RoleController
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        // 获取被切入的方法的参数和返回值等信息
        Object[] args = joinPoint.getArgs();
        //由于接口统一第一个参数都是角色id
        Long roleId = Long.valueOf(args[0].toString());
        //获取角色关联的用户信息
        Set<Long> userIds = this.userRoleService.listUserIdsByRoleIds(Collections.singleton(roleId));
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        UserPermissionRefreshMessage userPermissionRefreshMessage = new UserPermissionRefreshMessage();
        //过滤掉租户管理员用户
        UserTenant userTenantAdmin = this.userTenantService.getUserTenantAdmin(SessionContextHolder.getRequester().getTenant());
        Set<Long> user = userIds.stream().filter(userId -> !userId.equals(userTenantAdmin.getUserId())).collect(Collectors.toSet());
        userPermissionRefreshMessage.setUserId(user);
        RocketMQContextHolder.send(userPermissionRefreshMessage.TOPIC, userPermissionRefreshMessage);
    }
}
