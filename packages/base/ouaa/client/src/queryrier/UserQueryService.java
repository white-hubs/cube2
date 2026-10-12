package com.guhecloud.cube.biz.ouaa.client.queryrier;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/11/15 14:39
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserQueryService {
    private final UserClient userClient;

    private final ThreadLocal<QueryVariable> queryVariableThreadLocal = new ThreadLocal<>();

    @Setter
    private class QueryVariable {
        private Set<Long> roleIds;
        private Set<Long> organizationIds;
        private Set<Long> userIds;
        private Boolean enabled;

    }

    /**
     * 查询多个用户
     *
     * @return
     */
    @Deprecated
    public List<UserDTO> list() {
        // 构建查询参数条件
        UserQuery userQuery = UserQuery.builder()
                .organizationIds(queryVariableThreadLocal.get().organizationIds)
                .roleIds(queryVariableThreadLocal.get().roleIds)
                .enabled(queryVariableThreadLocal.get().enabled)
                .userIds(queryVariableThreadLocal.get().userIds)
                .build();

        // 执行查询
        List<UserDTO> result = userClient.list(userQuery);
        clear();
        return result;
    }

    /**
     * post请求方式查询多个用户
     *
     * @return
     */
    public List<UserDTO> postList() {
        // 构建查询参数条件
        UserQuery userQuery = UserQuery.builder()
                .organizationIds(queryVariableThreadLocal.get().organizationIds)
                .roleIds(queryVariableThreadLocal.get().roleIds)
                .enabled(queryVariableThreadLocal.get().enabled)
                .userIds(queryVariableThreadLocal.get().userIds)
                .build();

        // 执行查询
        List<UserDTO> result = userClient.postList(userQuery);
        clear();
        return result;
    }

    /**
     * 查询单个用户
     *
     * @return 返回单个结果，如果有多个结果将会抛出异常；如果用户不存在，返回null
     */
    public UserDTO singleResult() {
        // 构建查询参数条件
        UserQuery userQuery = UserQuery.builder()
                .organizationIds(queryVariableThreadLocal.get().organizationIds)
                .roleIds(queryVariableThreadLocal.get().roleIds)
                .enabled(queryVariableThreadLocal.get().enabled)
                .userIds(queryVariableThreadLocal.get().userIds)
                .build();

        // 执行查询
        List<UserDTO> list = userClient.list(userQuery);
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() > 1) {
                throw new ServiceException("存在多个结果");
            }
            return list.stream().findFirst().get();
        }
        return null;
    }

    public UserQueryService roleIds(Set<Long> roleIds) {
        QueryVariable queryVariable = queryVariableThreadLocal.get();
        if (Objects.isNull(queryVariable)) {
            queryVariable = new QueryVariable();
        }
        queryVariable.setRoleIds(roleIds);
        queryVariableThreadLocal.set(queryVariable);
        return this;
    }

    public UserQueryService userIds(Set<Long> userIds) {
        QueryVariable queryVariable = queryVariableThreadLocal.get();
        if (Objects.isNull(queryVariable)) {
            queryVariable = new QueryVariable();
        }
        queryVariable.setUserIds(userIds);
        queryVariableThreadLocal.set(queryVariable);
        return this;
    }

    /**
     * 查询启用的用户
     *
     * @return
     */
    public UserQueryService enabled() {
        QueryVariable queryVariable = queryVariableThreadLocal.get();
        if (Objects.isNull(queryVariable)) {
            queryVariable = new QueryVariable();
        }
        queryVariable.setEnabled(true);
        queryVariableThreadLocal.set(queryVariable);
        return this;
    }

    /**
     * 查询禁用的
     *
     * @return
     */
    public UserQueryService disable() {
        QueryVariable queryVariable = queryVariableThreadLocal.get();
        if (Objects.isNull(queryVariable)) {
            queryVariable = new QueryVariable();
        }
        queryVariable.setEnabled(false);
        queryVariableThreadLocal.set(queryVariable);
        return this;
    }

    public UserQueryService organizationIds(Set<Long> organizationIds) {
        QueryVariable queryVariable = queryVariableThreadLocal.get();
        if (Objects.isNull(queryVariable)) {
            queryVariable = new QueryVariable();
        }
        queryVariable.setOrganizationIds(organizationIds);
        queryVariableThreadLocal.set(queryVariable);
        return this;
    }

    private void clear() {
        queryVariableThreadLocal.remove();
    }

}
