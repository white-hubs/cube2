package com.guhecloud.cube.biz.daily.task.enums;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 负责人类型
 */
@Getter
public enum DailySuperintendentTypeEnum {
    USER("人员") {
        @Override
        public List<Long> getUserList(List<Long> idList) {
            return idList;
        }
    },
    DEPT("部门") {
        @Override
        public List<Long> getUserList(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            return userClient.listEnableUserByOrgAndPost(idList, new ArrayList<>());
        }
    },
    POST("岗位") {
        @Override
        public List<Long> getUserList(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.getUserListByAppointmentIds(StringUtils.join(",", idList));

            return Optional.ofNullable(userDTOS).orElse(new ArrayList<>())
                    .stream().map(UserDTO::getId).collect(Collectors.toList());
        }
    },
    ROLE("角色") {
        @Override
        public List<Long> getUserList(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.listUsersByRoles(new HashSet<>(idList));
            return Optional.ofNullable(userDTOS).orElse(new ArrayList<>())
                    .stream().map(UserDTO::getId).collect(Collectors.toList());
        }
    };
    private final String value;

    DailySuperintendentTypeEnum(String value) {
        this.value = value;
    }

    public abstract List<Long> getUserList(List<Long> idList);
}
