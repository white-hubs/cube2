package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionDeptHelper {
    private final OrganizationClient organizationClient;
    private final UserClient userClient;

    public Map<Long, String> getDeptNameMap(List<Long> deptIds) {
        if (ListUtils.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }
        try {
            List<OrganizationDTO> departmentList = organizationClient.getOrganizationsByIds(deptIds);
            return MapUtils.listToMap(departmentList, OrganizationDTO::getId, OrganizationDTO::getName);
        } catch (Exception e) {
            log.error("获取部门名称失败", e);
            return Collections.emptyMap();
        }

    }

    public String getDeptName(Long deptId) {
        return getDeptNameMap(Collections.singletonList(deptId)).getOrDefault(deptId, "");
    }

    public Map<Long, Boolean> isExistUsersByDeptIds(List<Long> deptIds) {
        if (ListUtils.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }
        UserQuery userQuery = UserQuery.builder().organizationIds(new HashSet<>(deptIds)).build();
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<Long, Boolean> result = deptIds.stream().collect(Collectors.toMap(deptId -> deptId, deptId -> false));
        if (ListUtils.nonEmpty(userDTOS)) {
            userDTOS.forEach(userDTO -> {
                UserDTO.Organization organization = userDTO.getOrganization();
                if (Objects.nonNull(organization)) {
                    Long id = organization.getId();
                    result.put(id, true);
                }
            });
        }
        return result;
    }
}
