package com.guhecloud.cube.biz.ouaa.server.aspect;

import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.param.RoleAssignUsersParam;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.UserPermissionRefreshMessage;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class UserAuthorityRefreshAspect {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserOrganizationService userOrganizationService;

    /**
     * 客户端请求对象上下文
     */
    private static final ThreadLocal<Map<Long, Pair<Set<Long>, Long>>> USER_ROLE_IDS = new ThreadLocal<>();

    @Pointcut("execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.UserUnderTenantController.updateByTenant(..))||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.RoleController.usersRoleEdit(..)))"
    )
    public void point() {
        // This method is used to define the pointcut expression for updateByTenant method in UserUnderTenantController
    }

    @Before(value = "point()")
    public void before(JoinPoint joinPoint) {
        List<Long> userIdList = this.getUserIdList(joinPoint);
        //获取用户修改前的角色
        List<UserRoleDTO> userRoleDTOS = this.userRoleService.listUserRoles(userIdList);
        List<UserOrganizationDTO> userOrganizationDTOS = this.userOrganizationService.listUserOrganizations(userIdList);

        Map<Long, Set<Long>> userRoleIdMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userRoleDTOS)) {
            userRoleIdMap = userRoleDTOS.stream()
                    .collect(Collectors.groupingBy(UserRoleDTO::getUserId,
                                                   Collectors.mapping(UserRoleDTO::getId, Collectors.toSet())));

        }
        Map<Long, Long> userOrganizationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userOrganizationDTOS)) {
            userOrganizationMap = userOrganizationDTOS.stream()
                    .collect(Collectors.toMap(UserOrganizationDTO::getUserId, UserOrganizationDTO::getId, (k1, k2) -> k1));

        }
        Map<Long, Pair<Set<Long>, Long>> userInfo = new HashMap<>();
        for (Long userId : userIdList) {
            userInfo.put(userId, Pair.of(userRoleIdMap.getOrDefault(userId, new HashSet<>()),
                                         userOrganizationMap.getOrDefault(userId, 0L)));
        }
        USER_ROLE_IDS.set(userInfo);
    }

    private List<Long> getUserIdList(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        List<Long> userIdList = new ArrayList<>();
        if ("usersRoleEdit".equals(methodName)) {
            //获取当前角色下所有用户
            Long roleId = Long.valueOf(args[0].toString());
            Set<Long> userIds = userRoleService.listUserIdsByRoleIds(Collections.singletonList(roleId));
            if (!CollectionUtils.isEmpty(userIds)) {
                userIdList.addAll(userIds);
            }
            RoleAssignUsersParam roleAssignUsersParam = (RoleAssignUsersParam) args[1];
            if (Objects.nonNull(roleAssignUsersParam)
                && !CollectionUtils.isEmpty(roleAssignUsersParam.getUserIdList())) {
                userIdList.addAll(roleAssignUsersParam.getUserIdList());
            }
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        } else {
            userIdList.add(Long.valueOf(args[0].toString()));
        }
        return userIdList;
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        try {
            List<Long> userIdList = new ArrayList<>(USER_ROLE_IDS.get().keySet());
            if (CollectionUtils.isEmpty(userIdList)) {
                return;
            }
            //获取用户修改后的角色
            List<UserRoleDTO> userRoleDTOS = this.userRoleService.listUserRoles(userIdList);
            List<UserOrganizationDTO> userOrganizationDTOS = this.userOrganizationService.listUserOrganizations(userIdList);
            Map<Long, Set<Long>> userRoleIdMap = Optional.ofNullable(userRoleDTOS)
                    .orElse(Collections.emptyList()).stream()
                    .collect(Collectors.groupingBy(UserRoleDTO::getUserId,
                                                   Collectors.mapping(UserRoleDTO::getId, Collectors.toSet())));
            Map<Long, Long> userOrganizationMap = Optional.ofNullable(userOrganizationDTOS)
                    .orElse(Collections.emptyList()).stream()
                    .collect(Collectors.toMap(UserOrganizationDTO::getUserId, UserOrganizationDTO::getId, (k1, k2) -> k1));
            Set<Long> sendMqUserId = new HashSet<>();
            USER_ROLE_IDS.get().forEach((userId, setSetPair) -> {
                Set<Long> roleIds = userRoleIdMap.getOrDefault(userId, new HashSet<>());
                Long organizationId = userOrganizationMap.get(userId);
                Set<Long> beforeRoleIds = setSetPair.getLeft();
                Long beforeOrganizationId = setSetPair.getRight();
                //获取存在差异的角色
                Set<Long> afterDiffRoleIds = roleIds.stream().filter(roleId -> !beforeRoleIds.contains(roleId)).collect(Collectors.toSet());
                Set<Long> beforeDiffRoleIds = beforeRoleIds.stream().filter(roleId -> !roleIds.contains(roleId))
                        .collect(Collectors.toSet());
                if (!CollectionUtils.isEmpty(afterDiffRoleIds) || !CollectionUtils.isEmpty(beforeDiffRoleIds)
                    || !Objects.equals(beforeOrganizationId, organizationId)) {
                    sendMqUserId.add(userId);
                }
            });
            //发送更新权限消息
            if (!CollectionUtils.isEmpty(sendMqUserId)) {
                UserPermissionRefreshMessage userPermissionRefreshMessage = new UserPermissionRefreshMessage();
                userPermissionRefreshMessage.setUserId(sendMqUserId);
                RocketMQContextHolder.send(UserPermissionRefreshMessage.TOPIC, userPermissionRefreshMessage);
            }
        } catch (Exception e) {
            log.error("用户权限刷新后置处理失败", e);
        } finally {
            USER_ROLE_IDS.remove();
        }
    }
}
