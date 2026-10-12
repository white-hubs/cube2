package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.NewEmployeeLeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/11/18 19:05
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayConfigValidator implements SubmitLeaveFormValidator {

    private final HolidayQueryService holidayQueryService;
    private final StaffClient staffClient;

    @Override
    public boolean verify(LeaveForm leaveForm) {
        // 适用范围
        Holiday holiday = holidayQueryService.getByType(leaveForm.getDetail().getLeaveType());
        Set<Long> employees = holiday.getEmployees();
        if (CollectionUtils.isNotEmpty(employees) && !employees.contains(leaveForm.getDetail().getUserId())) {
            throw new ServiceException("当前员工无适用范围");
        }

        // 获取当前请假单位
        LeaveFormDetail.LeaveTime leaveTime = leaveForm.getDetail().getLeaveTime();
        LeaveRule.MinimumLeaveTimeUnit minimumLeaveTimeUnit = leaveForm.getLeaveRule().getMinimumLeaveTimeUnitConvert()
                .getMinimumLeaveTimeUnit();
        boolean flag = false;

        if (leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.DAY &&
            minimumLeaveTimeUnit == LeaveRule.MinimumLeaveTimeUnit.DAY) {
            flag = true;
        } else if ((leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.AM ||
                    leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.PM)
                   && minimumLeaveTimeUnit == LeaveRule.MinimumLeaveTimeUnit.HALF_DAY) {
            flag = true;
        } else if (leaveTime.getStartTime().getTimeUnit() == LeaveFormDetail.LeaveTimeUnit.TIME_RANGE &&
                   minimumLeaveTimeUnit == LeaveRule.MinimumLeaveTimeUnit.HOUR) {
            flag = true;
        }
        if (!flag) {
            throw new ServiceException("不满足请假最小时长单位");
        }

        // 新员工请假
        NewEmployeeLeaveRule newEmployeeLeaveRule = holiday.getLeaveRule().getNewEmployeeLeaveRule();

        // 获取员工入职时长

        boolean verify = newEmployeeLeaveRule.verify(getTimeOfInduction(leaveForm.getDetail().getUserId()));
        if (!verify) {
            throw new ServiceException("员工入职时长不满足");
        }
        return true;
    }

    private Integer getTimeOfInduction(Long employeeId) {
        StaffDetailDTO staffDetailDTO = staffClient.getById(employeeId);
        if (Objects.isNull(staffDetailDTO)) {
            return 0;
        }
        LocalDateTime inductionDate = staffDetailDTO.getInductionDate();
        //员工入职时长为空不验证
        if (Objects.isNull(inductionDate)) {
            return 0;
        }
        LocalDateTime now = LocalDateTime.now();
        long months = DateUtils.betweenMonth(inductionDate, now, true);
        return (int) months;
    }
}
