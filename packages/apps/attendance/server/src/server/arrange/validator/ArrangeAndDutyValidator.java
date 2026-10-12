package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.validator;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command.SubmitArrangeAndDutyWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeAndDutyValidator {

    private final ClassesValidator classesValidator;
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final DutyWorkClassesMapper dutyWorkClassesMapper;

    /**
     * 验证班次信息
     *
     * @param command 手动排班数据
     */
    public void verifyClasses(SubmitArrangeAndDutyWorkCommand command, String type) {
        List<Classes> classes = command.getClasses();
        Assert.isConflict(!classesValidator.anyClassesNoCross(classes), "班次选择存在冲突(包括打卡范围)，请重新选择");
        Assert.isConflict(classesValidator.ifHaveRestClassesHaveAnyOther(classes), "有休息班次就不能再有其他班次");
        Assert.isNotAcceptable(
                DateUtils.compare(command.getDate(), DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now()))) < 0,
                "今日之前的班次不支持修改");
        // 与前一天的班次是否冲突
        if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
            List<Classes> lastDayClasses = arrangeWorkClassesMapper.getUserClassesByDay(command.getStaffId(), DateUtils.offsetDay(command.getDate(), -1));
            Assert.isConflict(!classesValidator.anyClassesNoCross(lastDayClasses, classes), "与前一天的班次时间冲突(包括打卡范围)");
        } else if (AttendanceGroupTypeEnum.DUTY.name().equals(type)) {
            List<Classes> lastDayClasses = dutyWorkClassesMapper.getUserClassesByDay(command.getStaffId(), DateUtils.offsetDay(command.getDate(), -1));
            Assert.isConflict(!classesValidator.anyClassesNoCross(lastDayClasses, classes), "与前一天的班次时间冲突(包括打卡范围)");
        }
        // 与后一天的班次是否冲突
        if (AttendanceGroupTypeEnum.ARRANGE.name().equals(type)) {
            List<Classes> nextDayClasses = arrangeWorkClassesMapper.getUserClassesByDay(command.getStaffId(), DateUtils.offsetDay(command.getDate(), 1));
            Assert.isConflict(!classesValidator.anyClassesNoCross(classes, nextDayClasses), "与后一天的班次时间冲突(包括打卡范围)");
        } else if (AttendanceGroupTypeEnum.DUTY.name().equals(type)) {
            List<Classes> nextDayClasses = dutyWorkClassesMapper.getUserClassesByDay(command.getStaffId(), DateUtils.offsetDay(command.getDate(), 1));
            Assert.isConflict(!classesValidator.anyClassesNoCross(classes, nextDayClasses), "与后一天的班次时间冲突(包括打卡范围)");
        }
    }

}
