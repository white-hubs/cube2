package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.commandgateway.AttendanceLogService;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.dao.AttendanceOperationLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.po.AttendanceOperationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 考勤管理操作日志
 * @date 2023/6/12 15:43
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceLogServiceImpl extends ServiceImpl<AttendanceOperationLogMapper, AttendanceOperationLog> implements AttendanceLogService {

    /**
     * 新增操作日志
     *
     * @param build
     */
    @Override
    public void insertRecord(AttendanceOperationLog build) {
        this.save(build);
    }
}
