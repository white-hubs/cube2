package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
