package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionRoleHelper {
    private final RoleClient roleClient;
    private final UserClient userClient;

    public Map<Long, String> getRoleNameMap(List<Long> roleIds) {
        if (ListUtils.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }
        try {
            List<RoleDTO> roleList = roleClient.listRolesByIds(roleIds);
            return MapUtils.listToMap(roleList, RoleDTO::getId, RoleDTO::getName);
        } catch (Exception e) {
            log.error("查询角色信息异常", e);
            return Collections.emptyMap();
        }

    }

    public String getRoleName(Long roleId) {
        return getRoleNameMap(Collections.singletonList(roleId)).get(roleId);
    }

    public Map<Long, Boolean> isExistUsersByRoleIds(List<Long> roleIds) {
        if (ListUtils.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }
        UserQuery userQuery = UserQuery.builder().roleIds(new HashSet<>(roleIds)).build();
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<Long, Boolean> result = roleIds.stream().collect(Collectors.toMap(roleId -> roleId, roleId -> false));
        if (ListUtils.nonEmpty(userDTOS)) {
            userDTOS.forEach(userDTO -> {
                List<UserDTO.Role> roles = userDTO.getRoles();
                if (ListUtils.nonEmpty(roles)) {
                    roles.forEach(role -> {
                        Long id = role.getId();
                        result.put(id, true);
                    });
                }
            });
        }
        return result;
    }
}
