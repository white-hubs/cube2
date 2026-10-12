package com.guhecloud.cube.biz.ouaa.server.permission.cache;

import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.redis.authority.cache.PrincipalPermissionCache;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserPermissionCache {

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private SubjectPermissionService subjectPermissionService;

    @Autowired
    private PrincipalPermissionCache principalPermissionCache;

    public void loadUserPermissions() {
        //获取租户用户
        Map<Integer, List<UserTenant>> tenantUsers = this.userTenantService.list().stream()
                .collect(Collectors.groupingBy(UserTenant::getTenant));
        for (Map.Entry<Integer, List<UserTenant>> entry : tenantUsers.entrySet()) {
            Integer tenant = entry.getKey();
            List<UserTenant> users = entry.getValue();
            if (CollectionUtils.isEmpty(users)) {
                continue;
            }
            SessionContextHolder.requestTenant(tenant);
            cacheUserPermission(users.stream().map(UserTenant::getUserId).collect(Collectors.toSet()));
            SessionContextHolder.clearSessionContext();
        }
    }

    public void cacheUserPermission(Set<Long> userIds) {
        Map<Long, List<UserRoleDTO>> userRoleMap = this.userRoleService.listUserRoles(userIds)
                .stream().collect(Collectors.groupingBy(UserRoleDTO::getUserId));
        for (Map.Entry<Long, List<UserRoleDTO>> entry : userRoleMap.entrySet()) {
            Long userId = entry.getKey();
            List<UserRoleDTO> userRoles = entry.getValue();
            if (CollectionUtils.isEmpty(userRoles)) {
                continue;
            }
            try {
                Set<String> roleIds = userRoles.stream().map(userRole -> String.valueOf(userRole.getId())).collect(Collectors.toSet());
                Set<Permission> permissions = this.subjectPermissionService.getPermissionsBySubjectIds(roleIds, SubjectTypeEnum.ROLE);
                permissions.addAll(extendPermissionRole(userRoles));
                principalPermissionCache.setPrincipalPermission(String.valueOf(userId), Principal.Type.USER, permissions);
            } catch (Exception e) {
                log.error("user Id:{},permission cache error,detail message:", userId, e);
            }
        }
    }

    public Set<Permission> extendPermissionRole(List<UserRoleDTO> userRoles) {
        Set<Permission> permissions = userRoles.stream().map(userRole -> {
            Permission permission = new Permission();
            permission.setCode(userRole.getId().toString());
            permission.setType("ROLE");
            return permission;
        }).collect(Collectors.toSet());
        // activiti7要求用户必须具有ROLE_ACTIVITI_USER角色才能调用其api
        Permission permission = new Permission();
        permission.setCode("ROLE_ACTIVITI_USER");
        permission.setType("ROLE");
        permissions.add(permission);
        return permissions;
    }
}
