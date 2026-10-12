package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.SubmitWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
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
 * 生成每月的排班和值班数据
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenerateWorkDataJob {

    private final AttendanceGroupMapper attendanceGroupMapper;
    private final WorkCommandService workCommandService;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final AttendanceGroupClassesMapper attendanceGroupClassesMapper;

    @JobAnnotation(name = "generateWorkDataJob", cron = "0 0 0 1 * ?", desc = "考勤-每月末排班")
    public void execute(String param) {
        if (log.isDebugEnabled()) {
            log.debug("生成每月排班和值班任务开始执行，额外参数:{}", param);
        }
        SessionContextHolder.clearSessionContext();
        // 查询所有的考勤组
        var attendanceGroupPOS = attendanceGroupMapper.selectList(null);
        // 循环考勤组生成排班或值班数据
        if (CollectionUtils.isEmpty(attendanceGroupPOS)) {
            if (log.isDebugEnabled()) {
                log.debug("无考勤组");
            }
            return;
        }
        // 开始排班时间，如果定时任务不传参数指定默认为次月1号
        LocalDateTime dateTime = null;
        if (StringUtils.isNotEmpty(param)) {
            try {
                dateTime = DateUtils.parse(param, "yyyy-MM-dd");
            } catch (Exception e) {
                throw new ServiceException("参数格式错误，时间格式示例：2023-02-01");
            }
        }
        var finalDateTime = (dateTime != null ? dateTime : DateUtils.beginOfMonth(DateUtils.offsetMonth(LocalDateTime.now(), 1)));
        attendanceGroupPOS.forEach(po -> {
            SessionContextHolder.requestTenant(po.getTenant());
            var workCommand = ObjectUtils.copying(po, SubmitWorkCommand.class);
            workCommand.setGroupId(po.getId());
            // 考勤组需查询关联人员和关联班次
            var groupStaffPOS = attendanceGroupStaffMapper.selectList(Wrappers
                                                                              .<AttendanceGroupStaffPO>lambdaQuery()
                                                                              .eq(AttendanceGroupStaffPO::getGroupId, workCommand.getGroupId()));
            workCommand.setStaffPOS(ObjectUtils.copying(groupStaffPOS, SubmitAttendanceGroupStaffCommand.class));
            var groupClassesPOS = attendanceGroupClassesMapper.selectList(Wrappers
                                                                                  .<AttendanceGroupClassesPO>lambdaQuery()
                                                                                  .eq(AttendanceGroupClassesPO::getGroupId, workCommand.getGroupId()));
            List<SubmitAttendanceGroupClassesCommand> classesPOS = new ArrayList<>();
            groupClassesPOS.stream().collect(Collectors.groupingBy(AttendanceGroupClassesPO::getWeek)).forEach((week, list) -> {
                SubmitAttendanceGroupClassesCommand classesCommand = new SubmitAttendanceGroupClassesCommand();
                classesCommand.setWeek(week);
                classesCommand.setClasses(ObjectUtils.copying(list, Classes.class));
                classesPOS.add(classesCommand);
            });
            workCommand.setClassesPOS(classesPOS);
            workCommand.setBeginDate(finalDateTime);
            workCommandService.generateWorkData(workCommand);
        });
    }

}
