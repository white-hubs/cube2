package com.guhecloud.cube.biz.ouaa.server.permission.user.controller;

import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.PermissionVO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.SubjectPermissionVO;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserPermissionController {

    private final UserRoleService userRoleService;

    private final SubjectPermissionService subjectPermissionService;

    private final PermissionService permissionService;

    /**
     * 获取当前用户权限集
     *
     * @return
     */
    @GetMapping("/user-permissions")
    public List<SubjectPermissionVO> getCurrentUserPermissions() {
        // 判断当前用户是否为租户管理员（tenantAdmin）,如果是租户管理员直接返回
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        if (isTenantAdmin) {
            List<PermissionVO> permissions = this.permissionService.getPermissions(null);
            return ObjectUtils.copying(permissions, SubjectPermissionVO.class);
        }
        List<UserRoleDTO> userRoleDTOS = this.userRoleService.listUserRoles(Collections.singleton(SecurityContextHolder.getPrincipal()
                                                                                                          .getId()));
        Set<String> roleIds = userRoleDTOS.stream().map(userRoleDTO -> String.valueOf(userRoleDTO.getId())).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return this.subjectPermissionService.getSubjectPermissions(roleIds, SubjectTypeEnum.ROLE);
    }
}
