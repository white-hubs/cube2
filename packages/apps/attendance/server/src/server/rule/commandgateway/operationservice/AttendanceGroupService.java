package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 考勤组新增修改删除操作
 * @date 2023/6/12 18:25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceGroupService extends ServiceImpl<AttendanceGroupMapper, AttendanceGroupPO> {

    @LogRecord(success = "新增:{{#attendanceGroupPO.name}}", subType = "ATTENDANCE_SECTION", operationType = "ADD",
               subTypeName = "考勤组", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#attendanceGroupPO.id}}"
    )
    public void addAttendanceGroup(AttendanceGroupPO attendanceGroupPO) {
        log.info("新增考勤组...");
        this.save(attendanceGroupPO);
    }

    @LogRecord(success = "删除:{{#groupPO.name}}", subType = "ATTENDANCE_SECTION", subTypeName = "考勤组", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#groupPO.id}}"
    )
    public void deleteAttendanceGroup(AttendanceGroupPO groupPO) {
        log.info("删除考勤组...");
        DatabaseContextHolder.softDelete(this, groupPO.getId());
    }
}
