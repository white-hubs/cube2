package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 换班
 */
public class AttChangeClassDomain {

    public static void changeClassToApproveLog(AttApproveLogPO logPO, String leavePost) {
        ClassesChangeCommand command = JsonUtils.deserialize(leavePost, ClassesChangeCommand.class);
        logPO.setId(IdWorker.getId());
        if (Objects.isNull(command)) {
            return;
        }
        if (command.getFromDate() != null) {
            logPO.setStartTime(command.getFromDate());
            // 23:59:59.999入MySQL会变第二天0点，所以不能用endOfDay
            logPO.setEndTime(DateUtils.offset(command.getFromDate(), ChronoUnit.SECONDS, 3600 * 24 - 1));
        }
        logPO.setUserId(command.getApplicantId());
        if (AttendanceGroupTypeEnum.DUTY.name().equals(command.getWorkType())) {
            logPO.setBusinessType(null);
        }
        String fromDateStr = command.getFromDate() == null ? "" : DateUtils.formatDate(command.getFromDate());
        String toDateStr = command.getToDate() == null ? "" : DateUtils.formatDate(command.getToDate());
        logPO.setProportion(command.getApplicantId() + "," + command.getChangerId() + "," + fromDateStr + "," + toDateStr + "," +
                            command.getFromClassesId() + "," + command.getToClassesId());
    }
}
