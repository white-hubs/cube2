package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class UserRoleDTOConverterForUserVORole {
    private UserRoleDTOConverterForUserVORole() {}

    public static UserVO.Role toUserVORole(UserRoleDTO role) {
        var userVORole = new UserVO.Role();
        userVORole.setId(role.getId());
        userVORole.setName(role.getName());
        return userVORole;
    }

    public static List<UserVO.Role> toUserVORoles(List<UserRoleDTO> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return CollectionUtils.emptyList();
        }
        return roles.stream().map(UserRoleDTOConverterForUserVORole::toUserVORole).toList();
    }
}
