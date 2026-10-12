package com.guhecloud.cube.srv.gen.maintain.server.common.service.impl;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainUserClientService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lijie
 * @describe 人员信息
 * @date 2023/6/1 10:27
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintainUserClientServiceImpl implements MaintainUserClientService {
    private final UserClient userClient;

    @Override
    public String getUserName(Long id) {
        if (ObjectUtils.isNotEmpty(id)) {
            UserDTO user = userClient.getUser(id);
            if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getName())) {
                return user.getName();
            }
        }
        return "";
    }

    @Override
    public String getUserName(String userName) {
        if (ObjectUtils.isNotEmpty(userName)) {
            UserDTO user = userClient.getUserByUserName(userName);
            if (ObjectUtils.isNotEmpty(user.getName())) {
                return user.getName();
            }
        }
        return "";
    }

    @Override
    public UserDTO getUser(Long id) {
        if (ObjectUtils.isNotEmpty(id)) {
            UserDTO user = userClient.getUser(id);
            if (ObjectUtils.isNotEmpty(user.getName())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserDTO getUser(String userName) {
        if (ObjectUtils.isNotEmpty(userName)) {
            UserDTO user = userClient.getUserByUserName(userName);
            if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getName())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserDTO getUser(String userName, Boolean bool) {
        UserDTO user = this.getUser(userName);
        if (bool && ObjectUtils.isNotEmpty(user)) {
            return this.getUser(user.getId());
        }
        return user;
    }

    @Override
    public String getUserNames(String receiveIds) {
        if (StringUtils.isNotBlank(receiveIds)) {
            StringBuilder sb = new StringBuilder();
            String[] split = receiveIds.split(",");
            for (String id : split) {
                String userName = getUserName(Long.parseLong(id));
                if (StringUtils.isNotBlank(userName)) {
                    sb.append(userName).append(",");
                }
            }
            return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
        }
        return "";
    }

    @Override
    public Map<Long, UserDTO> getUserBatch(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }
        return Optional.ofNullable(userClient.listUserByIds(userIds))
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

    }
}
