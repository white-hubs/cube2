package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectQuery {

    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final PositionClient positionClient;
    private final RoleClient roleClient;

    /**
     * 通过用户IDS查询 用户的名称
     */
    public String extendUserNames(Integer tenant, String userIds) {
        var oldTenant = SessionContextHolder.getRequester().getTenant();
        String userNames = "";
        if (StringUtils.isNotBlank(userIds)) {
            List<Long> uIds = Arrays.stream(userIds.split(",")).map(Long::valueOf).distinct().collect(Collectors.toList());
            try {
                SessionContextHolder.requestTenant(tenant);

                List<UserDTO> userDTOS = userClient.listUserByIds(uIds);
                userNames = Optional.ofNullable(userDTOS).orElse(Collections.emptyList())
                        .stream().map(UserDTO::getName)
                        .collect(Collectors.joining(","));
            } catch (Exception e) {
                log.error("查询用户信息异常", e);
            } finally {
                // 恢复租户
                if (oldTenant == null) {
                    SessionContextHolder.clearSessionContext();
                } else if (!oldTenant.equals(tenant)) {
                    SessionContextHolder.requestTenant(oldTenant);
                }
            }

        }
        return userNames;
    }

    /**
     * 通过部门IDS,查询部门的名称
     */
    public String extendDeptNames(Integer tenant, String deptIds) {
        String oNames = "";
        if (StringUtils.isNotBlank(deptIds)) {
            List<Long> dIds = Arrays.stream(deptIds.split(",")).map(Long::valueOf).distinct().collect(Collectors.toList());
            try {
                SessionContextHolder.requestTenant(tenant);

                List<OrganizationDTO> orgs = organizationClient.getOrganizationsByIds(dIds);
                oNames = Optional.ofNullable(orgs).orElse(Collections.emptyList())
                        .stream().map(OrganizationDTO::getName)
                        .collect(Collectors.joining(","));
            } catch (Exception e) {
                log.error("查询部门信息异常", e);
            }
        }
        return oNames;
    }

    public String extendPostNames(Integer tenant, String postIds) {
        String pNames = "";
        if (StringUtils.isNotBlank(postIds)) {
            List<Long> dIds = Arrays.stream(postIds.split(",")).map(Long::valueOf).distinct().collect(Collectors.toList());
            try {
                SessionContextHolder.requestTenant(tenant);

                List<PositionDTO> orgs = positionClient.getPositions(dIds);
                pNames = Optional.ofNullable(orgs).orElse(Collections.emptyList())
                        .stream().map(PositionDTO::getName)
                        .collect(Collectors.joining(","));
            } catch (Exception e) {
                log.error("查询岗位信息异常", e);
            }

        }
        return pNames;
    }

    public String extendRoleNames(Integer tenant, String roleIds) {
        String rNames = "";
        if (StringUtils.isNotBlank(roleIds)) {
            List<Long> rIds = Arrays.stream(roleIds.split(",")).map(Long::valueOf).distinct().collect(Collectors.toList());
            try {
                SessionContextHolder.requestTenant(tenant);

                List<RoleDTO> orgs = roleClient.listRolesByIds(rIds);
                rNames = Optional.ofNullable(orgs).orElse(Collections.emptyList())
                        .stream().map(RoleDTO::getName)
                        .collect(Collectors.joining(","));
            } catch (Exception e) {
                log.error("查询岗位信息异常", e);
            }

        }
        return rNames;
    }

}
