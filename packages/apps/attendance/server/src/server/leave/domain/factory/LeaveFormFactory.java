package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.factory;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice.LeaveFormDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/11/18 20:00
 * @description 请假单聚合工厂
 */
public class LeaveFormFactory {

    public static LeaveForm create(LocalDateTime submitTime,
                                   Long userId,
                                   LeaveFormDetail.LeaveTime leaveTime,
                                   Holiday.HolidayType leaveType,
                                   Holiday holiday, String leaveLocation
                                  ) {

        // 请假时长计算
        LeaveRule leaveRule = holiday.getLeaveRule();
        Float hour = LeaveFormDomainService.computeLeaveTime(userId, leaveTime, leaveRule);

        if (hour <= 0.0) {
            throw new ServiceException("请假时间有误");
        }

        LeaveFormDetail leaveFormDetail = new LeaveFormDetail(
                Objects.isNull(userId) ? SecurityContextHolder.getPrincipal().getId() : userId,
                leaveTime,
                leaveType,
                hour
        );

        leaveFormDetail.setLeaveLocation(leaveLocation);
        LeaveForm leaveForm = new LeaveForm(
                leaveFormDetail,
                holiday.getLeaveRule()
        );
        leaveForm.setSubmitTime(Objects.nonNull(submitTime) ? submitTime : LocalDateTime.now());
        return leaveForm;
    }
}
