package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("securityManagementRiskOuaaFacadeService")
@Slf4j
public class OuaaFacadeService {
    private final UserClient userClient;
    private final OrganizationClient organizationClient;

    @Autowired
    public OuaaFacadeService(UserClient userClient, OrganizationClient organizationClient) {
        this.userClient = userClient;
        this.organizationClient = organizationClient;
    }

    public Map<String, String> batchUserNames(Collection<String> userIds) {
        List<Long> ids = toLongIds(userIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            List<UserDTO> users = userClient.listUserByIds(ids);
            if (users == null || users.isEmpty()) {
                return Collections.emptyMap();
            }
            return users.stream()
                    .filter(Objects::nonNull)
                    .filter(user -> user.getId() != null && user.getName() != null)
                    .collect(Collectors.toMap(user -> String.valueOf(user.getId()), UserDTO::getName, (left, right) -> left));
        } catch (RuntimeException ex) {
            log.warn("Failed to resolve risk control user names, userIds={}", ids, ex);
            return Collections.emptyMap();
        }
    }

    public Map<String, String> batchDepartmentNames(Collection<String> departmentIds) {
        List<Long> ids = toLongIds(departmentIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            List<OrganizationDTO> organizations = organizationClient.getOrganizationsByIds(ids);
            if (organizations == null || organizations.isEmpty()) {
                return Collections.emptyMap();
            }
            return organizations.stream()
                    .filter(Objects::nonNull)
                    .filter(organization -> organization.getId() != null && organization.getName() != null)
                    .collect(Collectors.toMap(organization -> String.valueOf(organization.getId()),
                            OrganizationDTO::getName, (left, right) -> left));
        } catch (RuntimeException ex) {
            log.warn("Failed to resolve risk control department names, departmentIds={}", ids, ex);
            return Collections.emptyMap();
        }
    }

    private List<Long> toLongIds(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return ids.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .map(this::parseLong)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private Long parseLong(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
