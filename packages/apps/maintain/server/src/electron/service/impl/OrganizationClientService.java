package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.google.common.collect.Lists;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantUserWorkStatusEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationClientService {
    private final OrganizationClient organizationClient;

    private final UserClient userClient;

    private final TerminalClient terminalClient;

    /**
     * @Author wql
     * @Description 查询部门名称
     * @Date 15:44 2023/5/31
     **/
    public String getOrganizationName(Long depId) {
        OrganizationDTO id = null;
        if (depId != null) {
            id = organizationClient.getOrganizationsById(depId);
        }
        return id == null ? "" : id.getName();
    }

    /**
     * 查询部门名称
     *
     * @param depIds 部门id集合
     *
     * @return 部门id对应名称集合
     */
    public Map<Long, String> getOrganizationNames(Set<Long> depIds) {
        if (CollectionUtils.isEmpty(depIds)) {
            return new HashMap<>();
        }
        List<OrganizationDTO> byIds = organizationClient.getOrganizationsByIds(Lists.newArrayList(depIds));
        if (!CollectionUtils.isEmpty(byIds)) {
            return byIds.stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
        }
        return new HashMap<>();
    }

    /**
     * @Author wql
     * @Description 根据用户id查询用户信息
     * @Date 10:41 2023/10/25
     **/
    public UserDTO getUserInfo(Long userId) {
        UserDTO user = userClient.getUser(userId);
        if (ObjectUtils.isNull(user)) {
            user = new UserDTO();
            user.setOrganization(new UserDTO.Organization());
        }
        if (ObjectUtils.isEmpty(user.getOrganization())) {
            user.setOrganization(new UserDTO.Organization());
        }
        return user;
    }

    /**
     * @Author wql
     * @Description 获取用户名称
     * @Date 17:50 2023/5/31
     **/
    public String getUserName(Long userId) {
        UserDTO id = null;
        if (userId != null) {
            id = userClient.getUser(userId);
        }
        return id == null ? "" : id.getName();
    }

    /**
     * @Author lijie
     * @Description 获取用户名称
     * @Date 10:50 2023/6/19
     **/
    public String getUserName(String userName) {
        if (ObjectUtils.isEmpty(userName)) {
            return "";
        }
        UserDTO user = userClient.getUserByUserName(userName);
        return ObjectUtils.isNotEmpty(user) ? user.getName() : "";
    }

    /**
     * @Author wql
     * @Description 判断用户是否在岗
     * @Date 9:38 2023/10/30
     **/
    public String getOnGuardUser(String belonger) {
        if (StringUtils.isNotBlank(belonger)) {
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            Set<Long> set = new HashSet<>();
            Arrays.stream(belonger.split(",")).mapToLong(Long::parseLong).forEach(set::add);
            List<UserTenantDTO> userWorkStatus = terminalClient.getTenantUserWorkStatus(tenant, set);
            Set<String> onDutyEmployee = new HashSet<>();
            userWorkStatus.stream().filter(dto -> TenantUserWorkStatusEnum.WORKING.name().equals(dto.getWorkStatus())).forEach(dto -> {
                onDutyEmployee.add(dto.getUserId().toString());
            });
            if (ObjectUtils.isNotEmpty(onDutyEmployee)) {
                belonger = String.join(",", onDutyEmployee);
            }
        }
        return belonger;
    }

    /**
     * 根据id批量获取部门数据
     *
     * @return
     */
    public List<OrganizationDTO> getOrgInfoByIdList(List<Long> orgIdList) {
        if (CollectionUtils.isEmpty(orgIdList)) {
            return new ArrayList<>();
        }
        return organizationClient.getOrganizationsByIds(orgIdList);
    }

}
