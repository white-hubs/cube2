package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户角色关联业务操作接口
 *
 * @author Woody
 * @date 2021/6/30
 */
public interface UserRoleService extends IService<UserRole> {
    /**
     * 获取用户角色列表
     *
     * @param userIds 用户主键列表
     *
     * @return 角色列表
     */
    List<UserRoleDTO> listUserRoles(Collection<Long> userIds);

    /**
     * 获取角色下的用户列表
     *
     * @param roleIds
     *
     * @return
     */
    Set<Long> listUserIdsByRoleIds(Collection<Long> roleIds);

    /**
     * 批量人员分配角色
     * 注意：如果人员id集合为空会清空角色下分配的人员
     *
     * @param roleId     角色id
     * @param userIdList 人员id集合
     */
    @Transactional(rollbackFor = Exception.class)
    void roleAssignUsers(@NonNull Long roleId, List<Long> userIdList);

    List<UserRole> getBindUser(@NonNull List<Long> roleIds);
}
