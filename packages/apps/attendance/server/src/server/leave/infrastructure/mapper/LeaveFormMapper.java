package com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.po.LeaveFormPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface LeaveFormMapper extends BaseMapper<LeaveFormPO> {

    @InterceptorIgnore(tenantLine = "true")
    List<LeaveFormPO> selectListStartLeaveAndIgnoreTenant(@Param("leaveFormIds") Set<Long> leaveFormIds);

    @InterceptorIgnore(tenantLine = "true")
    List<LeaveFormPO> selectListEndLeaveAndIgnoreTenant(@Param("leaveFormIds") Set<Long> leaveFormIds);
}
