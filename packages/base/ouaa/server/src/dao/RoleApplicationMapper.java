package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色应用关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Mapper
public interface RoleApplicationMapper extends BaseMapper<RoleApplication> {

    List<RoleApplication> listByTenant(@Param("tenantId") Long tenantId);
}
