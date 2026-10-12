package com.guhecloud.cube.srv.gen.maintain.server.common.service;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.Map;
import java.util.Set;

public interface MaintainUserClientService {
    String getUserName(Long id);

    String getUserName(String userName);

    UserDTO getUser(Long id);

    /**
     * @Author wql
     * @Description 根据用户名查询用户信息没有部门
     * @Date 18:54 2023/6/14
     **/
    UserDTO getUser(String userName);

    /**
     * @param bool 是否查询部门
     *
     * @Author wql
     * @Description 根据用户名查询用户信息可查部门
     * @Date 18:54 2023/6/14
     **/
    UserDTO getUser(String userName, Boolean bool);

    /**
     * @Author wql
     * @Description 获取用户名称
     * @Date 15:05 2023/11/28
     **/
    String getUserNames(String receiveIds);

    /**
     * 批量获取用户信息
     */
    Map<Long, UserDTO> getUserBatch(Set<Long> userIds);
}
