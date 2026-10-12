package com.guhecloud.cube.srv.gen.maintain.server.common.util;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/29 15:56
 * @Description: 权限过滤参数公共方法
 */
@Component
public class PermissionFilterParamUtils {

    /**
     * 根据当前用户部门获取权限过滤参数
     *
     * @return
     */
    public List<Long> getPermissionFilterParamByUserDept() {
        //当前用户主键
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO user = userClient.getUser(userId);
        if (!CollectionUtils.isEmpty(user.getOrganizations())) {
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(Collections
                                                                                  .singletonList(user.getOrganizations()
                                                                                                         .get(user.getOrganizations()
                                                                                                                      .size() - 1)
                                                                                                         .getId()), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            return userIdList;
        }
        return null;
    }

    /**
     * 根据自定义部门获取权限过滤参数
     *
     * @param customParam
     *
     * @return
     */
    public List<Long> getPermissionFilterParamByCustomDept(Object customParam) {

        if (Objects.isNull(customParam)) {
            return null;
        }
        //自定义部门角色过滤
        Set<Long> customDeptIds = new HashSet<>();
        if (customParam instanceof List) {
            ((List) customParam).stream().forEach(t -> {
                customDeptIds.add(Long.valueOf(t.toString()));
            });
        } else {
            customDeptIds.add(Long.valueOf(customParam.toString()));
        }
        if (!CollectionUtils.isEmpty(customDeptIds)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(new ArrayList<>(customDeptIds), new ArrayList<>());
            if (CollectionUtils.isEmpty(userIdList)) {
                return null;
            }
            return userIdList;
        }
        return null;
    }

}
