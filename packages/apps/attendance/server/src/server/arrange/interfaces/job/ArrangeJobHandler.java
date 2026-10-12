package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkTodayStatVO;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.WorkingStatus;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排班和值班的定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeJobHandler {

    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final DutyWorkClassesMapper dutyWorkClassesMapper;
    private final StaffClient staffClient;

    @JobAnnotation(name = "clearArrangeAndDutyClassesId", cron = "0 0 0 L * ?", desc = "考勤-定时清理未被引用的排班班次")
    public void clearArrangeAndDutyClassesId(String param) {
        var dateTime = DateUtils.beginOfMonth(DateUtils.offsetMonth(LocalDateTime.now(), -1));
        // 每月初初执行，删除上上个月的数据
        // 查询一段时间未被引用的班次
        var noUseWorkClassesIds = arrangeWorkClassesMapper.getNoUseWorkClasses(dateTime);
        if (!CollectionUtils.isEmpty(noUseWorkClassesIds)) {
            arrangeWorkClassesMapper.deleteByIds(noUseWorkClassesIds);
        }
        // 值班(排班表中没有存储真正的班次时间，所以不确定是哪一天的班次，不用时间去限制)
        var noUseDutyWorkClassesIds = dutyWorkClassesMapper.getNoUseWorkClasses();
        if (!CollectionUtils.isEmpty(noUseDutyWorkClassesIds)) {
            dutyWorkClassesMapper.deleteByIds(noUseDutyWorkClassesIds);
        }
    }

    /**
     * 检查每天休息的人，更新花名册工作状态为休息
     */
    @JobAnnotation(name = "checkEveyDayRestStaff", cron = "0 0 0 * * ?", desc = "考勤-检查每日休息的员工")
    public void execute(String param) {
        var date = LocalDateTime.now();
        SessionContextHolder.clearSessionContext();
        var staffList = arrangeWorkClassesMapper.getToadyStaffNoClasses(DateUtils.formatDate(date));
        staffList.stream().collect(Collectors.groupingBy(ArrangeWorkTodayStatVO.Staff::getTenant)).forEach((tenant, list) -> {
            List<ChangeWorkingStatusCommand> changeWorkingStatusCommands = new ArrayList<>();
            list.forEach(staff ->
                                 changeWorkingStatusCommands.add(ChangeWorkingStatusCommand.builder().employeeId(staff.getId())
                                                                         .workingStatus(WorkingStatus.REST).condition(true)
                                                                         .originalStatusIsNull(true)
                                                                         .build())
                        );
            SessionContextHolder.requestTenant(tenant);
            staffClient.changeWorkingStatus(changeWorkingStatusCommands);
        });
    }

}
