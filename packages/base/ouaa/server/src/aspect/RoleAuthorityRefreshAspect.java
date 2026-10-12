package com.guhecloud.cube.biz.ouaa.server.aspect;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class RoleAuthorityRefreshAspect {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private RoleTerminalService roleTerminalService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private SiteMessageSendClient siteMessageSendClient;

    @Pointcut("execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.deleteRole(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleTerminalMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.saveRoleTerminalApplicationMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleTerminalApplicationMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.saveRoleTerminalApplicationMenuMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.saveRoleTerminalApplicationMenuFunctionMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleRoleMenuFunctionMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.saveRoleTerminalApplicationMenuDataPermissionMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleTerminalApplicationMenuDataPermissionMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleTerminalApplicationMenuMapping(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.saveRoleAllTerminalApplicationMenuMappings(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.removeRoleAllTerminalApplicationMenuMappings(..))"
    )
    public void point() {
        // This method is used to define the pointcut expression for some method in RoleController
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        // 获取被切入的方法的参数和返回值等信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        //由于接口统一第一个参数都是角色id
        Long roleId = Long.valueOf(args[0].toString());
        //通知的终端
        Set<String> terminals = new HashSet<>();
        //删除角色的情况
        if ("deleteRole".equals(method.getName())) {
            //获取角色关联的访问端信息
            List<RoleTerminal> roleTerminals = this.roleTerminalService.listByRole(roleId);
            terminals = roleTerminals.stream().map(roleTerminal ->
                                                           this.terminalService.getById(roleTerminal.getTerminalId()).getCode()
                                                  ).collect(Collectors.toSet());
        } else {
            //从第2个参数取出终端id
            Long terminalId = Long.valueOf(args[1].toString());
            Terminal terminal = this.terminalService.getById(terminalId);
            terminals.add(terminal.getCode());
        }
        //获取角色关联的用户信息
        Set<Long> userIds = this.userRoleService.listUserIdsByRoleIds(Collections.singleton(roleId));
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        //过滤掉租户管理员用户
        UserTenant userTenantAdmin = this.userTenantService.getUserTenantAdmin(SessionContextHolder.getRequester().getTenant());
        Set<Long> user = userIds.stream().filter(userId -> !userId.equals(userTenantAdmin.getUserId())).collect(Collectors.toSet());
        SiteSendRequest siteSendRequest = SiteSendRequest.builder()
                .userIds(user)
                .eventType("AUTHORITY_REFRESH_MESSAGE")
                .msgType("AUTHORITY_REFRESH")//权限刷新消息
                .content("ROLE_AUTHORITY_REFRESH")
                .terminals(terminals)
                .build();
        this.siteMessageSendClient.send(siteSendRequest);
    }
}
