package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.InfoTypesEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wql
 * @describe 用户信息和部门信息查询
 * @date 2023/9/15 11:12
 */
@Service
public class UserAndOrgService {
    @Resource
    private UserClient userClient;
    @Resource
    private OrganizationClient organizationClient;
    @Resource
    private FileClient fileClient;

    /**
     * @Author wql
     * @Description 根据用户id查询用户信息
     * @Date 11:13 2023/9/15
     **/
    public UserDTO getUserById(Long userId) {
        if (ObjectUtils.isEmpty(userId)) {
            return new UserDTO();
        }
        UserDTO user = userClient.getUser(userId);
        return ObjectUtils.isNotNull(user) ? user : new UserDTO();
    }

    public List<UserDTO> getUserByIds(String userId) {
        if (ObjectUtils.isEmpty(userId)) {
            return new ArrayList<>();
        }
        String[] ids = userId.split(",");
        List<Long> userIds = new ArrayList<>();
        for (String id : ids) {
            userIds.add(Long.parseLong(id));
        }
        List<UserDTO> user = userClient.listUserByIds(userIds);
        return ObjectUtils.isNotNull(user) ? user : new ArrayList<>();
    }

    public UserDTO getUserByUsername(String username) {
        if (ObjectUtils.isEmpty(username)) {
            return new UserDTO();
        }
        UserDTO user = userClient.getUserByUserName(username);
        return ObjectUtils.isNotNull(user) ? user : new UserDTO();
    }

    public Map<String, UserDTO> getUserByUserNames(Set<String> userNames) {
        if (CollectionUtils.isEmpty(userNames)) {
            return new HashMap<>();
        }
        UserMapQuery userMapQuery = new UserMapQuery();
        userMapQuery.setInfoTypes(Collections.singleton(InfoTypesEnum.ORGANIZATION.name()));
        userMapQuery.setUserNames(userNames);
        Map<String, UserDTO> userDTOMap = userClient.getTenantUserMap(userMapQuery);
        return MapUtils.isNotEmpty(userDTOMap) ? userDTOMap : new HashMap<>();
    }

    /**
     * @Author wql
     * @Description 根据用户手机号查询用户信息
     * @Date 11:13 2023/9/15
     **/
    public UserDTO getUserByPhone(Long phone) {
        UserDTO user = userClient.getUserByMobile(String.valueOf(phone));
        if (ObjectUtils.isNotNull(user)) {
            user = userClient.getUser(user.getId());
        }
        return ObjectUtils.isNotNull(user) ? user : new UserDTO();
    }

    /**
     * @Author wql
     * @Description 根据部门id查部门信息
     * @Date 11:15 2023/9/15
     **/
    public OrganizationDTO getOrgInfoById(Long orgId) {
        if (orgId == null) {
            return new OrganizationDTO();
        }
        OrganizationDTO organizations = organizationClient.getOrganizationsById(orgId);
        return ObjectUtils.isNotNull(organizations) ? organizations : new OrganizationDTO();
    }

    /**
     * 根据id批量获取部门数据
     *
     * @return
     */
    public List<OrganizationDTO> getOrgInfoByIdList(Collection<Long> orgIdList) {
        if (CollectionUtils.isEmpty(orgIdList)) {
            return new ArrayList<>();
        }
        return organizationClient.getOrganizationsByIds(new ArrayList<>(orgIdList));
    }

    /**
     * @Author wql
     * @Description 获取文件列表
     * @Date 13:45 2023/9/15
     **/
    public List<FileVO> getFileListByIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            List<Long> fileIds = new ArrayList<>();
            for (String s : ids.split(",")) {
                fileIds.add(Long.parseLong(s));
            }
            return fileClient.getByIds(fileIds);
        }
        return null;
    }

    public List<UserDTO> getUserListByName(String name) {
        return userClient.listUsers(name);
    }
}
