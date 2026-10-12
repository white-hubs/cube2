package com.guhecloud.cube.biz.ouaa.server.permission.role.controller;

import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionConfigModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.SubjectPermissionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePermissionController {

    private final SubjectPermissionService subjectPermissionService;

    /**
     * 更新角色权限配置
     *
     * @param roleId
     * @param subjectPermissionConfigModifies
     */
    @PutMapping("/role-permission/{roleId}")
    public void updateRolePermission(@PathVariable("roleId") Long roleId,
                                     @Valid @RequestBody Set<SubjectPermissionConfigModify> subjectPermissionConfigModifies
                                    ) {
        SubjectPermissionModify subjectPermissionModify = new SubjectPermissionModify();
        subjectPermissionModify.setSubjectId(String.valueOf(roleId));
        subjectPermissionModify.setSubjectType(SubjectTypeEnum.ROLE);
        subjectPermissionModify.setPermissionConfigs(subjectPermissionConfigModifies);
        this.subjectPermissionService.save(subjectPermissionModify);
    }

    /**
     * 更新角色权限扩展配置
     *
     * @param roleId
     * @param subjectPermissionConfigModifies
     */
    @PutMapping("/role-permission/{roleId}/extend")
    public void updateRolePermissionExtendConfig(@PathVariable("roleId") Long roleId,
                                                 @Valid @RequestBody Set<SubjectPermissionConfigModify> subjectPermissionConfigModifies
                                                ) {
        this.subjectPermissionService.saveExtendConfig(String.valueOf(roleId), SubjectTypeEnum.ROLE, subjectPermissionConfigModifies);
    }

    /**
     * 角色权限集合
     *
     * @param roleId
     *
     * @return
     */
    @GetMapping("/role-permissions/{roleId}")
    public List<SubjectPermissionVO> getRolePermissions(@PathVariable("roleId") Long roleId) {
        return this.subjectPermissionService.getSubjectPermissions(Collections.singleton(String.valueOf(roleId)), SubjectTypeEnum.ROLE);
    }
}
