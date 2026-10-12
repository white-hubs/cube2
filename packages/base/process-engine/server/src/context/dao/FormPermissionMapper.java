package com.guhecloud.cube.base.flow.context.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.context.po.FormPermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 15:00
 * @Description:
 */
@Mapper
public interface FormPermissionMapper extends BaseMapper<FormPermissionPO> {

    List<Long> selectCurrentUserForm(@Param("id") Long id, @Param("organizationId") Long organizationId, @Param("roles") List<Long> roles);

}
