package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.UserRoleMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户角色关联业务操作实现
 *
 * @author Woody
 * @date 2021/6/30
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Override
    public List<UserRoleDTO> listUserRoles(@NonNull Collection<Long> userIds) {
        return userIds.isEmpty() ? Collections.emptyList() : this.baseMapper.listUserRoles(userIds);
    }

    @Override
    public Set<Long> listUserIdsByRoleIds(@NonNull Collection<Long> roleIds) {
        return roleIds.isEmpty() ? Collections.emptySet() : this.baseMapper.listUserIdsByRoleIds(roleIds);
    }

    /**
     * 批量人员分配角色
     * 注意：如果人员id集合为空会清空角色下分配的人员
     *
     * @param roleId     角色id
     * @param userIdList 人员id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleAssignUsers(@NonNull Long roleId, List<Long> userIdList) {
        this.remove(Wrappers.<UserRole>lambdaQuery()
                            .eq(UserRole::getRoleId, roleId)
                   );
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<UserRole> userRoleList = userIdList.stream().distinct()
                .map(userId -> UserRole.builder().id(IdWorker.getId()).roleId(roleId).userId(userId).build()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userRoleList)) {
            this.saveBatch(userRoleList);
        }
    }

    @Override
    public List<UserRole> getBindUser(@NonNull List<Long> roleIds) {
        return lambdaQuery().in(UserRole::getRoleId, roleIds).list();
    }
}
