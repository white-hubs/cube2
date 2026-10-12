package com.guhecloud.cube.base.flow.context.enumeration;

import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 14:44
 * @Description:
 */
@Getter
public enum PermissionType {

    ALL("全部") {
        @Override
        public List<FormFieldVO> getPerMissions(List<Long> permissionIds) {
            return Collections.emptyList();
        }
    },
    USER("用户") {
        @Override
        public List<FormFieldVO> getPerMissions(List<Long> permissionIds) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.listUserByIds(permissionIds);
            return Optional.ofNullable(userDTOS).orElse(new ArrayList<>())
                    .stream().map(userDTO ->
                                          FormFieldVO.builder()
                                                  .id(userDTO.getId().toString())
                                                  .name(userDTO.getName())
                                                  .build()
                                 ).collect(Collectors.toList());
        }
    },
    ROLE("角色") {
        @Override
        public List<FormFieldVO> getPerMissions(List<Long> permissionIds) {
            RoleClient roleClient = ApplicationContextHolder.getBean(RoleClient.class);
            List<RoleDTO> roleDTOS = roleClient.listRolesByIds(permissionIds);
            return Optional.ofNullable(roleDTOS).orElse(new ArrayList<>())
                    .stream().map(roleDTO ->
                                          FormFieldVO.builder()
                                                  .id(roleDTO.getId().toString())
                                                  .name(roleDTO.getName())
                                                  .build()
                                 ).collect(Collectors.toList());
        }
    },
    DEPARTMENT("部门") {
        @Override
        public List<FormFieldVO> getPerMissions(List<Long> permissionIds) {
            OrganizationClient organizationClient = ApplicationContextHolder.getBean(OrganizationClient.class);
            List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(permissionIds);
            return Optional.ofNullable(organizationsByIds).orElse(new ArrayList<>())
                    .stream().map(organizationDTO ->
                                          FormFieldVO.builder()
                                                  .id(organizationDTO.getId().toString())
                                                  .name(organizationDTO.getName())
                                                  .build()
                                 ).collect(Collectors.toList());
        }
    };

    private final String desc;

    PermissionType(String desc) {
        this.desc = desc;
    }

    public abstract List<FormFieldVO> getPerMissions(List<Long> permissionIds);
}
