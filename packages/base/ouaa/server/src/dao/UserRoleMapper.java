package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.UserRoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户角色关联数据操作接口
 *
 * @author Woody
 * @date 2021/6/30
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 获取用户角色列表
     *
     * @param userIds 用户主键列表
     *
     * @return 角色列表
     */
    List<UserRoleDTO> listUserRoles(Collection<Long> userIds);

    /**
     * 获取角色下用户主键集合
     *
     * @param roleIds
     *
     * @return
     */
    Set<Long> listUserIdsByRoleIds(@Param("roleIds") Collection<Long> roleIds);
}
