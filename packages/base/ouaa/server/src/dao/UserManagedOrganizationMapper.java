package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.UserManagedOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface UserManagedOrganizationMapper extends BaseMapper<UserManagedOrganization> {

    Set<Long> selectManagedDeptIds(@Param("departmentId") Long departmentId, @Param("postIds") Set<Long> postIds);

    Set<Long> getManagedDeptListByAppointment(@Param("departmentId") Long departmentId, @Param("appointmentIds") Set<Long> appointmentIds);

    Set<Long> getDeptListByPost(@Param("departmentId") Long departmentId, @Param("postIds") Set<Long> postIds);
}
