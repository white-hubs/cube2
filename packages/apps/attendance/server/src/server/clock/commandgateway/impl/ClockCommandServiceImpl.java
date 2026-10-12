package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.ArrangeWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommonService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.SubmitClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.SubmitThirdClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.UpdateClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockLogsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration.ClockStatusEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo.ThirdClockResultVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockLogsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.validator.ClockValidator;
import com.guhecloud.cube.srv.gen.attendance.management.server.overtime.rocketmq.message.WorkOvertimeClockMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupPunchCardWayMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.dto.PunchCardWayDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupPunchCardWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.RepairClockTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.ThirdUserStaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.WorkingStatus;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author bq
 * @date 2022/11/23
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClockCommandServiceImpl extends ServiceImpl<ClassesClockMapper, ClassesClockPO> implements ClockCommandService {

    private final StaffClient staffClient;
    private final ClockValidator clockValidator;
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final ClockCommonService clockCommonService;
    private final ClassesClockLogsMapper classesClockLogsMapper;
    private final ThirdUserStaffClient thirdUserStaffClient;
    private final ArrangeWorkQueryService arrangeWorkQueryService;
    private final AttendanceGroupPunchCardWayMapper attendanceGroupPunchCardWayMapper;

    @Override
    public StaffDetailDTO saveByPhone(SubmitClockCommand command) {
        Long userId = command.getStaffId() == null ? SecurityContextHolder.getPrincipal().getId() : command.getStaffId();
        // 查询用户信息
        StaffDetailDTO staffDetailDTO = staffClient.getStaffVOById(userId);//.getByUserId(userId);
        Assert.isNotFound(staffDetailDTO, "未查询到相关员工信息");
        command.setStaffId(staffDetailDTO.getId());
        // 有考勤组和班次的按照正常时间打卡
        if (command.getAttendanceGroupId() != null && command.getArrangeClassesId() != null) {
            this.saveClassesClock(command, staffDetailDTO);
            this.sendEndClockMessage(command);
        } else {
            throw new ServiceException("打卡失败，当前无班次信息");
        }
        return staffDetailDTO;
    }

    @Override
    public void updateClockToNormal(UpdateClockCommand command) {
        ClassesClockPO clockPO = this.getDateCurrentClassesClock(command.getClockDate(), command.getArrangeClassesId(), command.getStaffId());
        if (clockPO == null) {
            // 查询用户信息
            StaffDetailDTO staffDetailDTO = staffClient.getById(command.getStaffId());
            Assert.isNotFound(staffDetailDTO, "未查询到相关用户");
            // 查询班次信息
            ArrangeWorkClassesVO workClassesVO = arrangeWorkClassesMapper.getUserClassesByClassesId(command.getStaffId(), command.getClockDate(), command.getArrangeClassesId());
            ClassesClockPO classesClockPO = ObjectUtils.copying(command, ClassesClockPO.class);
            ObjectUtils.copying(workClassesVO, classesClockPO);
            classesClockPO.setUserId(staffDetailDTO.getId());
            classesClockPO.setUserDeptId(staffDetailDTO.getOrganizationId());
            classesClockPO.setUserName(staffDetailDTO.getName());
            if (command.getClockType().name().startsWith(RepairClockTypeEnum.START.name())) {
                command.setStartWorkClockTime(workClassesVO.getStartRealTime());
            } else {
                command.setEndWorkClockTime(workClassesVO.getEndRealTime());
            }
            this.updateStartOrEndToNormal(classesClockPO, command);
            classesClockPO.setClassesClockStatus(ClockStatusEnum.NORMAL.name());
            this.save(classesClockPO);
        } else {
            if (command.getClockType().name().startsWith(RepairClockTypeEnum.START.name())) {
                command.setStartWorkClockTime(clockPO.getStartRealTime());
            } else {
                command.setEndWorkClockTime(clockPO.getEndRealTime());
            }
            this.updateStartOrEndToNormal(clockPO, command);
            clockPO.parseClassesClockStatus();
            this.updateById(clockPO);
        }
    }

    @Override
    public void updateClockToNormalByPeriod(UpdateClockCommand command) {
        Long staffId = command.getStaffId();
        Period period = command.getPeriod();
        // 分开查询时间段内的上下班班次信息
        List<CurrentClassesVO> startWorkClasses = arrangeWorkClassesMapper.getUserClassesByPeriod(staffId, true, false, period);
        List<CurrentClassesVO> endWorkClasses = arrangeWorkClassesMapper.getUserClassesByPeriod(staffId, false, true, period);
        startWorkClasses.forEach(vo -> {
            UpdateClockCommand startCommand = UpdateClockCommand.builder().staffId(staffId)
                    .arrangeClassesId(vo.getArrangeClassesId())
                    .clockDate(vo.getClockDate())
                    .startWorkClockTime(vo.getStartRealTime())
                    .trigger(command.getTrigger())
                    .clockType(RepairClockTypeEnum.START)
                    .build();
            this.updateClockToNormal(startCommand);
        });
        endWorkClasses.forEach(vo -> {
            UpdateClockCommand endCommand = UpdateClockCommand.builder().staffId(staffId)
                    .arrangeClassesId(vo.getArrangeClassesId())
                    .clockDate(vo.getClockDate())
                    .endWorkClockTime(vo.getEndRealTime())
                    .trigger(command.getTrigger())
                    .clockType(RepairClockTypeEnum.END)
                    .build();
            this.updateClockToNormal(endCommand);
        });
    }

    @Override
    public ThirdClockResultVO saveByThird(SubmitThirdClockCommand command) {
        log.info("收到打卡回调，参数：" + JsonUtils.serialize(command));
        AtomicReference<ThirdClockResultVO> clockVO = new AtomicReference<>(new ThirdClockResultVO());
        LocalDateTime clockTime = command.getClockTime();
        this.saveClockLog(Long.valueOf(command.getUserId()), clockTime, null, null);
        // 根据设备code，在考勤组中查询是否配置的有该考勤机，按租户分组，主要查询租户信息
        List<PunchCardWayDTO> machineWayByCode = attendanceGroupPunchCardWayMapper.getMachineWayByCode(command.getDeviceCode());
        machineWayByCode.forEach(machine -> {
            SessionContextHolder.requestTenant(machine.getTenant());
            clockVO.set(this.thirdClock(command.getUserId(), command.getClockTime(), command.getDeviceCode()));
        });
        return CollectionUtils.isEmpty(machineWayByCode) ? null : clockVO.get();
    }

    // 第三方软件打卡
    private ThirdClockResultVO thirdClock(String thirdUserId, LocalDateTime clockTime, String deviceCode) {
        ThirdClockResultVO clockResultVO = new ThirdClockResultVO();
        // 查询中间表
        try {
            Long staffId = thirdUserStaffClient.getStaffByThirdUserId(thirdUserId);
            if (Objects.isNull(staffId)) {
                return null;
            }
            CurrentClassesVO currentClassesVO = arrangeWorkQueryService.getUserClassesCurrentDate(staffId);
            if (Objects.nonNull(currentClassesVO)) {
                // 考勤打卡方式中是否配置有考勤机打卡
                boolean hasMachine = currentClassesVO.getWayList().stream()
                        .anyMatch(w -> AttendanceGroupPunchCardWayEnum.MACHINE.name().equals(w.getWay()) &&
                                       deviceCode.equals(w.getDeviceCode()));
                if (!hasMachine) {
                    return null;
                }
                // 有当前班次信息，如果上班卡没有打且还没有到下班时间，则优先打上班卡，只要上班卡打了就是打下班卡（一直更新）
                SubmitClockCommand clockCommand = ObjectUtils.copying(currentClassesVO, SubmitClockCommand.class);
                clockResultVO.setScheduName(currentClassesVO.getArrangeClassesName());
                if (currentClassesVO.getStartWorkClockTime() == null &&
                    DateUtils.compare(currentClassesVO.getEndRealTime(), clockTime) > 0 &&
                    (Objects.isNull(currentClassesVO.getAfterStartRealTime()) ||
                     DateUtils.compare(currentClassesVO.getAfterStartRealTime(), clockTime) >= 0)) {
                    clockCommand.setStartWorkClockTime(clockTime);
                    clockResultVO.setStartEmployType(
                            "上班打卡" + (DateUtils.compare(clockTime, currentClassesVO.getStartRealTime()) <= 0 ? "正常" : "迟到"));
                } else {
                    clockCommand.setStartWorkClockTime(null);
                    clockCommand.setEndWorkClockTime(clockTime);
                    clockResultVO.setStartEmployType(
                            "下班打卡" + (DateUtils.compare(clockTime, currentClassesVO.getEndRealTime()) >= 0 ? "正常" : "早退"));
                }
                StaffDetailDTO staffDetailDTO = this.saveByPhone(clockCommand);
                clockResultVO.setStarTime(DateUtils.format(clockTime, "yyyy-MM-dd HH:mm:ss"));
                clockResultVO.setUserId(Long.parseLong(thirdUserId));
                clockResultVO.setUserName(staffDetailDTO.getName());
                clockResultVO.setDeptName(staffDetailDTO.getOrganizationName());
                return clockResultVO;
            }
            return null;
        } catch (Exception e) {
            log.error("第三方软件打卡失败,", e);
            return null;
        }
    }

    // 更新上班卡或者下班卡正常
    private void updateStartOrEndToNormal(ClassesClockPO clockPO, UpdateClockCommand command) {
        LocalDateTime startWorkClockTime = command.getStartWorkClockTime();
        LocalDateTime endWorkClockTime = command.getEndWorkClockTime();
        if (startWorkClockTime != null && !ClockStatusEnum.NORMAL.name().equals(clockPO.getStartWorkClockStatus())) {
            clockPO.setStartWorkClockTime(startWorkClockTime);
            clockPO.setStartWorkClockStatus(ClockStatusEnum.NORMAL.name());
            clockPO.setStartTrigger(command.getTrigger());
        } else if (endWorkClockTime != null) {
            if (!ClockStatusEnum.NORMAL.name().equals(clockPO.getEndWorkClockStatus())) {
                clockPO.setEndWorkClockTime(endWorkClockTime);
                clockPO.setEndWorkClockStatus(ClockStatusEnum.NORMAL.name());
            }
            clockPO.setEndTrigger(command.getTrigger());
        }
    }

    // 保存没有班次的打卡记录
    private void saveNoClassesClock(SubmitClockCommand command, UserDTO user) {
        // 查询当日打卡记录
        ClassesClockPO clockPO = this.getDateCurrentClassesClock(command.getClockDate(), null, user.getId());
        if (clockPO == null) {
            ClassesClockPO classesClockPO = ObjectUtils.copying(command, ClassesClockPO.class);
            classesClockPO.setUserId(user.getId());
            classesClockPO.setUserName(user.getName());
            classesClockPO.setUserDeptId(user.getOrganization().getId());
            classesClockPO.setStartWorkClockStatus(ClockStatusEnum.NORMAL.name());
            classesClockPO.setClassesClockStatus(ClockStatusEnum.NORMAL.name());
            this.save(classesClockPO);
        } else {
            clockPO.setEndWorkClockTime(command.getEndWorkClockTime());
            clockPO.setEndWorkClockStatus(ClockStatusEnum.NORMAL.name());
            this.updateById(clockPO);
        }
    }

    // 保存有考勤班次的打卡记录
    private void saveClassesClock(SubmitClockCommand command, StaffDetailDTO staffDetailDTO) {
        clockValidator.verifyIsRest(command);
        clockValidator.verifyDateRanges(command);
        // 查询当日当前班次打卡记录
        ClassesClockPO clockPO = this.getDateCurrentClassesClock(command.getClockDate(), command.getArrangeClassesId(), staffDetailDTO.getId());
        if (null == clockPO) {
            // 如果没有记录，则插入一条
            ClassesClockPO classesClockPO = ObjectUtils.copying(command, ClassesClockPO.class);
            classesClockPO.setUserId(staffDetailDTO.getId());
            classesClockPO.setUserName(staffDetailDTO.getName());
            classesClockPO.setUserDeptId(staffDetailDTO.getOrganizationId());
            classesClockPO.setStartWorkClockStatus(this.parseStartClockStatus(command, classesClockPO));
            classesClockPO.setEndWorkClockStatus(this.parseEndClockStatus(command));
            classesClockPO.parseClassesClockStatus();
            this.save(classesClockPO);
            this.changeWorkingStatus(classesClockPO.getUserId(), classesClockPO.getStartWorkClockTime());
            this.saveClockLog(staffDetailDTO.getId(), classesClockPO.getStartWorkClockTime(), classesClockPO.getEndWorkClockTime(), command.getWorkId());
        } else {
            // 已经有记录，直接更新，PS:会覆盖之前的打卡记录，产品确认
            LocalDateTime startWorkClockTime = command.getStartWorkClockTime();
            LocalDateTime endWorkClockTime = command.getEndWorkClockTime();
            if (startWorkClockTime != null) {
                // 如果已经有审批动作，无需再打卡
                if (StringUtils.isNotEmpty(clockPO.getStartTrigger())) {
                    return;
                }
                clockPO.setStartWorkClockTime(startWorkClockTime);
                clockPO.setStartWorkClockStatus(this.parseStartClockStatus(command, clockPO));
            }
            if (endWorkClockTime != null) {
                if (StringUtils.isNotEmpty(clockPO.getEndTrigger())) {
                    return;
                }
                clockPO.setEndWorkClockTime(endWorkClockTime);
                clockPO.setEndWorkClockStatus(this.parseEndClockStatus(command));
                clockPO.parseClassesClockStatus();
                this.changeWorkingStatus(clockPO.getUserId(), null);
            }
            this.updateById(clockPO);
            this.saveClockLog(staffDetailDTO.getId(), startWorkClockTime, endWorkClockTime, command.getWorkId());
        }
        if (command.getStartWorkClockTime() != null) {
            // TODO 这里发消息
            // 打上班卡时检查上个班次是否有缺卡
            clockCommonService.updateLastClockToNoCard(staffDetailDTO, ObjectUtils.copying(command, CurrentClassesVO.class));
        }
    }

    private void changeWorkingStatus(Long userId, LocalDateTime startWorkClockTime) {
        ChangeWorkingStatusCommand command = new ChangeWorkingStatusCommand();
        command.setEmployeeId(userId);
        if (startWorkClockTime != null) {
            // 更新为在岗时，无前置条件，优先级最高直接更新
            command.setWorkingStatus(WorkingStatus.WORKING);
        } else {
            // 将在岗状态更新为空，前置条件：当前状态为在岗
            command.setCondition(true);
            command.setWorkingStatus(null);
            command.setOriginalInWorkingStatusList(Collections.singletonList(WorkingStatus.WORKING.name()));
        }
        staffClient.changeWorkingStatus(Collections.singletonList(command));
    }

    // 查询用户当日当前班次打卡记录
    private ClassesClockPO getDateCurrentClassesClock(String date, Long classesId, Long userId) {
        return this.lambdaQuery().eq(StringUtils.isNotEmpty(date), ClassesClockPO::getClockDate, date)
                .eq(null != classesId, ClassesClockPO::getArrangeClassesId, classesId)
                .eq(ClassesClockPO::getUserId, userId)
                .one();
    }

    // 解析上班打卡状态,更新上班状态的同时把上班状态更新为全班次状态
    private String parseStartClockStatus(SubmitClockCommand command, ClassesClockPO classesClockPO) {
        LocalDateTime startWorkClockTime = command.getStartWorkClockTime();
        LocalDateTime startWorkTime = command.getStartRealTime();
        if (startWorkClockTime != null) {
            if (startWorkTime == null || DateUtils.compare(startWorkTime, startWorkClockTime) >= 0) {
                classesClockPO.setClassesClockStatus(ClockStatusEnum.NORMAL.name());
                return ClockStatusEnum.NORMAL.name();
            } else {
                classesClockPO.setClassesClockStatus(ClockStatusEnum.EXP_LATE.name());
                return ClockStatusEnum.EXP_LATE.name();
            }
        }
        // 只能设为null，不能改成空串
        return null;
    }

    // 解析下班打卡状态
    private String parseEndClockStatus(SubmitClockCommand command) {
        LocalDateTime endWorkClockTime = command.getEndWorkClockTime();
        LocalDateTime endWorkTime = command.getEndRealTime();
        if (endWorkClockTime != null) {
            if (endWorkTime == null || DateUtils.compare(endWorkTime, endWorkClockTime) <= 0) {
                return ClockStatusEnum.NORMAL.name();
            } else {
                return ClockStatusEnum.EXP_LEAVE_EARLY.name();
            }
        }
        // 只能设为null,不能改成空串
        return null;
    }

    // 发送打下班卡的消息
    private void sendEndClockMessage(SubmitClockCommand command) {
        try {
            if (Objects.isNull(command.getEndWorkClockTime())) {
                return;
            }
            WorkOvertimeClockMessage message = WorkOvertimeClockMessage.builder().classId(command.getArrangeClassesId())
                    .workEndTime(command.getEndRealTime())
                    .workEndClockTime(command.getEndWorkClockTime())
                    .userId(command.getStaffId())
                    .workOvertimeType(command.getIsHoliday() != null && command.getIsHoliday() ? WorkOvertimeTypeEnum.FESTIVAL.name()
                                                                                               : WorkOvertimeTypeEnum.WORK.name())
                    .build();
            RocketMQContextHolder.send(WorkOvertimeClockMessage.TOPIC, message);
        } catch (Exception e) {
            log.error("发送打下班卡消息失败," + JsonUtils.serialize(command), e);
        }
    }

    private void saveClockLog(Long staffId, LocalDateTime startClockTime, LocalDateTime endClockTime, Long workId) {
        ClassesClockLogsPO po = new ClassesClockLogsPO();
        po.setStaffId(staffId);
        po.setIsStartClock(startClockTime != null);
        po.setWorkId(workId);
        po.setClockTime(startClockTime != null ? startClockTime : endClockTime);
        po.setTenant(SessionContextHolder.getRequester().getTenant());
        classesClockLogsMapper.insert(po);
    }

}
