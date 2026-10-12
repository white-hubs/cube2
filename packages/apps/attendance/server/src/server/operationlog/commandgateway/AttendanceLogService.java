package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;

/**
 * @author wql
 * @describe 考勤操作日志
 * @date 2023/6/12 15:42
 */
public interface AttendanceLogService {
    /**
     * @Author wql
     * @Description 新增操作日志
     * @Date 17:23 2023/6/12
     **/
    void insertRecord(AttendanceOperationLog build);
}
