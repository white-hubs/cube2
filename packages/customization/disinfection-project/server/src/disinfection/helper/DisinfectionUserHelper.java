package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisinfectionUserHelper {
    private final UserClient userClient;

    public String getUserName(Long userId) {
        if (Objects.isNull(userId)) {
            return "";
        }
        try {
            UserDTO user = userClient.getUser(userId);
            return Objects.isNull(user) ? "" : user.getName();
        } catch (Exception e) {
            log.error("getUserName error", e);
            return "";
        }

    }

    public Map<Long, UserDTO> getUserMap(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        try {
            List<UserDTO> list = userClient.postList(UserQuery.builder().userIds(new HashSet<>(userIds)).build());
            return MapUtils.listToMap(list, UserDTO::getId, userDTO -> userDTO);
        } catch (Exception e) {
            log.error("getUserMap error:{}", e.getMessage());
            return Collections.emptyMap();
        }

    }

    public Map<Long, String> getUserNameMap(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<UserDTO> list = userClient.listUserByIds(userIds.stream().distinct().collect(Collectors.toList()));
        return MapUtils.listToMap(list, UserDTO::getId, UserDTO::getName);

    }

    public List<Long> getUserIdsByName(String username) {
        if (StringUtils.isBlank(username)) {
            return Collections.emptyList();
        }
        List<UserDTO> userDTOS = userClient.listUsers(username);
        return ListUtils.convertItem(userDTOS, UserDTO::getId);

    }

    public List<UserDTO> listUsers(String keyword) {
        return userClient.postList(UserQuery.builder().keyword(keyword).build());
    }

    public List<UserDTO> getUsers(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        try {
            return userClient.postList(UserQuery.builder().userIds(new HashSet<>(userIds)).build());
        } catch (Exception e) {
            log.error("getUsers error:{}", e.getMessage());
            return Collections.emptyList();
        }

    }

    public List<UserDTO> getUserByDeptId(Long deptId) {

        return userClient.postList(UserQuery.builder().organizationIds(new HashSet<>(Collections.singletonList(deptId))).build());
    }

    public List<UserDTO> getUserByRoleId(Long roleId) {
        HashSet<Long> roleIds = new HashSet<>();
        roleIds.add(roleId);
        return userClient.postList(UserQuery.builder().roleIds(new HashSet<>(Collections.singletonList(roleId))).build());
    }

    public List<UserDTO> getUserByPostId(Long postId) {

        return userClient.postList(UserQuery.builder().positionIds(new HashSet<>(Collections.singletonList(postId))).build());
    }

    public List<UserDTO.Role> getUsersRoles(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<UserDTO> users = getUsers(userIds);
        if (ListUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(UserDTO::getRoles).flatMap(List::stream).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<UserDTO.Organization> getUsersDept(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<UserDTO> users = getUsers(userIds);
        if (ListUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(UserDTO::getOrganization).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<UserDTO.Appointment> getUsersPosts(List<Long> userIds) {
        if (ListUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<UserDTO> users = getUsers(userIds);
        if (ListUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(UserDTO::getAppointments).flatMap(List::stream).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public String getUserNameByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return "";
        }
        if ("system".equals(account)) {
            return "系统";
        }
        try {
            UserDTO user = userClient.getUserByMobile(account);

            return user.getName();
        } catch (Exception e) {
            log.error("getUserNameByAccount error:{}", e.getMessage());
            return "";
        }
    }

    public Map<String, String> getUserNamesMapByAccounts(List<String> accounts) {
        Map<String, String> map = new HashMap<>();
        if (ListUtils.isEmpty(accounts)) {
            return map;
        }
        try {
            accounts.forEach(account -> map.put(account, getUserNameByAccount(account)));
            return map;
        } catch (Exception e) {
            log.error("getUserNamesMap error:{}", e.getMessage());
            return map;
        }
    }

    public String getUserAccountById(Long userId) {
        try {
            return Optional.ofNullable(userId).map(id -> userClient.getUser(id).getUsername()).orElse("");
        } catch (Exception e) {
            log.error("getUserAccountById error:{}", e.getMessage());
            return "";
        }

    }
}
