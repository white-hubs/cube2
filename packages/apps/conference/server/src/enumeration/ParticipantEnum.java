package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceBaseEntityVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 17:56
 * @Description: 参与人类型
 */
@Getter
public enum ParticipantEnum {
    DEPARTMENT("部门") {
        @Override
        public List<ConferenceBaseEntityVO> getPersonInfo(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            OrganizationClient organizationClient = ApplicationContextHolder.getBean(OrganizationClient.class);
            List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(idList);
            return organizationsByIds.stream().map(organizationDTO -> ConferenceBaseEntityVO.builder()
                    .id(organizationDTO.getId())
                    .name(organizationDTO.getName())
                    .build()).collect(Collectors.toList());
        }
    },
    ROLE("角色") {
        @Override
        public List<ConferenceBaseEntityVO> getPersonInfo(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            RoleClient roleClient = ApplicationContextHolder.getBean(RoleClient.class);
            List<RoleDTO> roleDTOS = roleClient.listRolesByIds(idList);
            return roleDTOS.stream().map(roleDTO -> ConferenceBaseEntityVO.builder()
                    .id(roleDTO.getId())
                    .name(roleDTO.getName())
                    .build()).collect(Collectors.toList());
        }
    },
    USER("用户") {
        @Override
        public List<ConferenceBaseEntityVO> getPersonInfo(List<Long> idList) {
            if (CollectionUtils.isEmpty(idList)) {
                return Collections.emptyList();
            }
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<UserDTO> userDTOS = userClient.listUserByIds(idList);
            return userDTOS.stream().map(UserDTO -> ConferenceBaseEntityVO.builder()
                    .id(UserDTO.getId())
                    .name(UserDTO.getName())
                    .build()).collect(Collectors.toList());
        }
    };
    /**
     * 描述信息
     */
    private final String description;

    public abstract List<ConferenceBaseEntityVO> getPersonInfo(List<Long> idList);

    ParticipantEnum(String description) {
        this.description = description;
    }
}
