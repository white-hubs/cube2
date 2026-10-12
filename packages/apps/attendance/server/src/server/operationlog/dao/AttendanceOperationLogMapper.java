package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wql
 * @describe 考勤管理操作日志
 * @date 2023/6/12 15:00
 */
@Mapper
public interface AttendanceOperationLogMapper extends BaseMapper<AttendanceOperationLog> {
}
