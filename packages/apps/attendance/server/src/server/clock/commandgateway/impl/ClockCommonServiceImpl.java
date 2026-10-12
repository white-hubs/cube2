package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommonService;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.enumeration.ClockStatusEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.mapper.LeaveFormMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.infrastructure.po.LeaveFormPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.LeavePostSituationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.po.LeavePostSituationPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.command.WorkingStatus;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bq
 * @date 2022/12/06
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClockCommonServiceImpl implements ClockCommonService {

    private final StaffClient staffClient;
    private final ClassesClockMapper classesClockMapper;
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final LeaveFormMapper leaveFormMapper;
    private final LeavePostSituationMapper leavePostSituationMapper;

    @Override
    public void updateClockToNoCard(CurrentClassesVO vo, LocalDateTime currentDate, StaffDetailDTO staffDetailDTO) {
        ClassesClockPO classesClockPO = classesClockMapper.selectOne(Wrappers.<ClassesClockPO>lambdaQuery()
                                                                             .eq(ClassesClockPO::getArrangeClassesId, vo.getArrangeClassesId())
                                                                             .eq(ClassesClockPO::getClockDate, vo.getClockDate())
                                                                             .eq(ClassesClockPO::getUserId, vo.getStaffId()));
        LocalDateTime dateTime = DateUtils.parse(vo.getClockDate(), "yyyy-MM-dd");
        // 查询整体的请假
        List<LeaveFormPO> leaveFormList = leaveFormMapper.selectList(Wrappers.<LeaveFormPO>lambdaQuery()
                                                                             .eq(LeaveFormPO::getStatus, "PASS")
                                                                             .ge(LeaveFormPO::getLeaveStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                             .and(i -> i.like(LeaveFormPO::getLeaveStartTime, dateTime)
                                                                                     .or().like(LeaveFormPO::getLeaveEndTime, dateTime)
                                                                                     .or(j -> j
                                                                                             .le(LeaveFormPO::getLeaveStartTime, DateUtils.beginOfDay(dateTime))
                                                                                             .ge(LeaveFormPO::getLeaveEndTime, dateTime))
                                                                                 )
                                                                    );
        AtomicBoolean startLeave = new AtomicBoolean(false);
        AtomicBoolean endLeave = new AtomicBoolean(false);
        if (leaveFormList != null && !leaveFormList.isEmpty()) {
            leaveFormList.forEach(leaveFormPO -> {
                LeaveForm leaveForm = JSONObject.parseObject(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                if (vo.getStaffId().equals(leaveForm.getDetail().getUserId())) {
                    if (DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getBegin()) <=
                        DateUtils.toTimestamp(vo.getStartRealTime())
                        && DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getEnd()) >=
                           DateUtils.toTimestamp(vo.getStartRealTime())) {
                        startLeave.set(true);
                    }
                    if (DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getBegin()) <=
                        DateUtils.toTimestamp(vo.getEndRealTime())
                        && DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getEnd()) >=
                           DateUtils.toTimestamp(vo.getEndRealTime())) {
                        endLeave.set(true);
                    }
                }
            });
        }
        boolean startLeavePost = false;
        boolean endLeavePost = false;
        // 查询离岗
        List<LeavePostSituationPO> startLeavePostList = leavePostSituationMapper.selectList(Wrappers.<LeavePostSituationPO>lambdaQuery()
                                                                                                    .eq(LeavePostSituationPO::getApplicantId, vo.getStaffId())
                                                                                                    .ge(LeavePostSituationPO::getStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                                                    .le(LeavePostSituationPO::getStartTime, vo.getStartWorkTime())
                                                                                                    .ge(LeavePostSituationPO::getEndTime, vo.getStartWorkTime())
                                                                                           );
        if (startLeavePostList != null && !startLeavePostList.isEmpty()) {
            startLeavePost = true;
        }
        List<LeavePostSituationPO> endLeavePostList = leavePostSituationMapper.selectList(Wrappers.<LeavePostSituationPO>lambdaQuery()
                                                                                                  .eq(LeavePostSituationPO::getApplicantId, vo.getStaffId())
                                                                                                  .ge(LeavePostSituationPO::getStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                                                  .le(LeavePostSituationPO::getStartTime, vo.getEndWorkTime())
                                                                                                  .ge(LeavePostSituationPO::getEndTime, vo.getEndWorkTime())
                                                                                         );
        if (endLeavePostList != null && !endLeavePostList.isEmpty()) {
            endLeavePost = true;
        }

        boolean startStatusBool = startLeave.get() || startLeavePost;
        boolean endStatusBool = endLeave.get() || endLeavePost;
        if (null == classesClockPO) {
            if (staffDetailDTO == null) {
                try {
                    staffDetailDTO = staffClient.getById(vo.getStaffId());
                } catch (Exception e) {
                    return;
                }
            }
            // 上下班卡都没有打，则记两次缺卡，整体班次为旷工
            if (null != staffDetailDTO && this.checkEndWorkTimeOut(vo.getAfterEndRealTime(), currentDate)) {
                ClassesClockPO saved = ObjectUtils.copying(vo, ClassesClockPO.class);
                saved.setUserId(staffDetailDTO.getId());
                saved.setWorkId(vo.getWorkId());
                saved.setUserDeptId(staffDetailDTO.getOrganizationId());
                saved.setUserName(staffDetailDTO.getName());
                saved.setStartWorkClockStatus(startStatusBool ? ClockStatusEnum.NORMAL.name() : ClockStatusEnum.EXP_NOCARD.name());
                saved.setEndWorkClockStatus(endStatusBool ? ClockStatusEnum.NORMAL.name() : ClockStatusEnum.EXP_NOCARD.name());
                String status = ClockStatusEnum.EXP_NOCARD.name();
                if (startStatusBool && endStatusBool) {
                    status = ClockStatusEnum.NORMAL.name();
                } else if (!startStatusBool && !endStatusBool) {
                    status = ClockStatusEnum.EXP_SKIP_WORK.name();
                }
                saved.setClassesClockStatus(status);
                classesClockMapper.insert(saved);
            }
        } else {
            // 更新某一次的
            LocalDateTime startWorkClockTime = classesClockPO.getStartWorkClockTime();
            LocalDateTime endWorkClockTime = classesClockPO.getEndWorkClockTime();
            if (startWorkClockTime == null) {
                classesClockPO.setStartWorkClockStatus(startStatusBool ? ClockStatusEnum.NORMAL.name() : ClockStatusEnum.EXP_NOCARD.name());
            } else if (endWorkClockTime == null && this.checkEndWorkTimeOut(vo.getAfterEndRealTime(), currentDate)) {
                classesClockPO.setEndWorkClockStatus(endStatusBool ? ClockStatusEnum.NORMAL.name() : ClockStatusEnum.EXP_NOCARD.name());
                if (endStatusBool) {
                    this.changeWorkingStatus(classesClockPO.getUserId());
                }
            }
            classesClockPO.parseClassesClockStatus();
            classesClockMapper.updateById(classesClockPO);
        }
    }

    @Override
    public void updateLastClockToNoCard(StaffDetailDTO staff, CurrentClassesVO vo) {
        // TODO 这里用监听服务，不能用异步执行的方法，因为重新创建请求会丢失tenant
        // 查询上一个班次
        List<CurrentClassesVO> beforeCurrentDate = arrangeWorkClassesMapper.getUserClassesBeforeCurrentDate(staff.getId(), "", vo.getStartRealTime());
        if (CollectionUtils.isEmpty(beforeCurrentDate)) {
            return;
        }
        CurrentClassesVO currentClassesVO = beforeCurrentDate.getFirst();
        this.updateClockToNoCard(currentClassesVO, LocalDateTime.now(), staff);
    }

    /**
     * 检查是否超出下班打卡范围
     *
     * @param afterEndWorkTime 下班之后打卡时间范围
     * @param currentDate      当前时间
     *
     * @return false:当前时间在打卡范围内;true:当前时间在打卡时间范围外
     */
    private boolean checkEndWorkTimeOut(LocalDateTime afterEndWorkTime, LocalDateTime currentDate) {
        if (afterEndWorkTime == null) {
            return true;
        }
        return DateUtils.compare(afterEndWorkTime, currentDate) <= 0;
    }

    private void changeWorkingStatus(Long userId) {
        ChangeWorkingStatusCommand command = new ChangeWorkingStatusCommand();
        command.setEmployeeId(userId);
        command.setWorkingStatus(null);
        // 将在岗状态更新为空，前置条件当前状态为在岗
        command.setCondition(true);
        command.setOriginalInWorkingStatusList(Collections.singletonList(WorkingStatus.WORKING.name()));
        staffClient.changeWorkingStatus(Collections.singletonList(command));
    }

    /**
     * 定时任务更新打卡状态
     *
     * @param vo
     * @param currentDate
     * @param staffDetailDTO
     */
    @Override
    public void updateClockToNoCardJob(CurrentClassesVO vo, LocalDateTime currentDate, StaffDetailDTO staffDetailDTO) {
        ClassesClockPO classesClockPO = classesClockMapper.selectOne(Wrappers.<ClassesClockPO>lambdaQuery()
                                                                             .eq(ClassesClockPO::getArrangeClassesId, vo.getArrangeClassesId())
                                                                             .eq(ClassesClockPO::getClockDate, vo.getClockDate())
                                                                             .eq(ClassesClockPO::getUserId, vo.getStaffId()));
        LocalDateTime dateTime = DateUtils.parse(vo.getClockDate(), "yyyy-MM-dd");
        // 查询整体的请假
        List<LeaveFormPO> leaveFormList = leaveFormMapper.selectList(Wrappers.<LeaveFormPO>lambdaQuery()
                                                                             .eq(LeaveFormPO::getStatus, "PASS")
                                                                             .ge(LeaveFormPO::getLeaveStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                             .and(i -> i.like(LeaveFormPO::getLeaveStartTime, dateTime)
                                                                                     .or().like(LeaveFormPO::getLeaveEndTime, dateTime)
                                                                                     .or(j -> j
                                                                                             .le(LeaveFormPO::getLeaveStartTime, DateUtils.beginOfDay(dateTime))
                                                                                             .ge(LeaveFormPO::getLeaveEndTime, dateTime))
                                                                                 )
                                                                    );
        AtomicBoolean startLeave = new AtomicBoolean(false);
        AtomicBoolean endLeave = new AtomicBoolean(false);
        if (leaveFormList != null && !leaveFormList.isEmpty()) {
            leaveFormList.forEach(leaveFormPO -> {
                LeaveForm leaveForm = JSONObject.parseObject(leaveFormPO.getLeaveFormDetail(), LeaveForm.class);
                if (vo.getStaffId().equals(leaveForm.getDetail().getUserId())) {
                    if (DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getBegin()) <=
                        DateUtils.toTimestamp(vo.getStartRealTime())
                        && DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getEnd()) >=
                           DateUtils.toTimestamp(vo.getStartRealTime())) {
                        startLeave.set(true);
                    }
                    if (DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getBegin()) <=
                        DateUtils.toTimestamp(vo.getEndRealTime())
                        && DateUtils.toTimestamp(leaveForm.getDetail().getLeaveTime().getPeriod().getEnd()) >=
                           DateUtils.toTimestamp(vo.getEndRealTime())) {
                        endLeave.set(true);
                    }
                }
            });
        }
        boolean startLeavePost = false;
        boolean endLeavePost = false;
        // 查询离岗
        List<LeavePostSituationPO> startLeavePostList = leavePostSituationMapper.selectList(Wrappers.<LeavePostSituationPO>lambdaQuery()
                                                                                                    .eq(LeavePostSituationPO::getApplicantId, vo.getStaffId())
                                                                                                    .ge(LeavePostSituationPO::getStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                                                    .le(LeavePostSituationPO::getStartTime, vo.getStartWorkTime())
                                                                                                    .ge(LeavePostSituationPO::getEndTime, vo.getStartWorkTime())
                                                                                           );
        if (startLeavePostList != null && !startLeavePostList.isEmpty()) {
            startLeavePost = true;
        }
        List<LeavePostSituationPO> endLeavePostList = leavePostSituationMapper.selectList(Wrappers.<LeavePostSituationPO>lambdaQuery()
                                                                                                  .eq(LeavePostSituationPO::getApplicantId, vo.getStaffId())
                                                                                                  .ge(LeavePostSituationPO::getStartTime, DateUtils.offsetMonth(dateTime, -1))
                                                                                                  .le(LeavePostSituationPO::getStartTime, vo.getEndWorkTime())
                                                                                                  .ge(LeavePostSituationPO::getEndTime, vo.getEndWorkTime())
                                                                                         );
        if (endLeavePostList != null && !endLeavePostList.isEmpty()) {
            endLeavePost = true;
        }

        boolean startStatusBool = startLeave.get() || startLeavePost;
        boolean endStatusBool = endLeave.get() || endLeavePost;
        if (null == classesClockPO) {
            if (staffDetailDTO == null) {
                try {
                    staffDetailDTO = staffClient.getById(vo.getStaffId());
                } catch (Exception e) {
                    return;
                }
            }
            // 上下班卡都没有打，则记两次缺卡，整体班次为旷工
            if (null != staffDetailDTO && this.checkEndWorkTimeOut(vo.getAfterEndRealTime(), currentDate)) {
                ClassesClockPO saved = ObjectUtils.copying(vo, ClassesClockPO.class);
                saved.setUserId(staffDetailDTO.getId());
                saved.setWorkId(vo.getWorkId());
                saved.setUserDeptId(staffDetailDTO.getOrganizationId());
                saved.setUserName(staffDetailDTO.getName());
                if (startStatusBool) {
                    saved.setStartWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    saved.setStartWorkClockTime(vo.getStartRealTime());
                } else {
                    saved.setStartWorkClockStatus(ClockStatusEnum.EXP_NOCARD.name());
                }
                if (endStatusBool) {
                    saved.setEndWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    saved.setEndWorkClockTime(vo.getEndRealTime());
                } else {
                    saved.setEndWorkClockStatus(ClockStatusEnum.EXP_NOCARD.name());
                }

                String status = ClockStatusEnum.EXP_NOCARD.name();
                if (startStatusBool && endStatusBool) {
                    status = ClockStatusEnum.NORMAL.name();
                } else if (!startStatusBool && !endStatusBool) {
                    status = ClockStatusEnum.EXP_SKIP_WORK.name();
                }
                saved.setClassesClockStatus(status);
                if (startLeavePost) {
                    saved.setStartTrigger(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                }
                if (startLeave.get()) {
                    saved.setStartTrigger(ClassesStatusTriggerEnum.LEAVE.name());
                }
                if (endLeavePost) {
                    saved.setEndTrigger(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                }
                if (endLeave.get()) {
                    saved.setEndTrigger(ClassesStatusTriggerEnum.LEAVE.name());
                }
                classesClockMapper.insert(saved);
            }
        } else {
            // 更新某一次的
            LocalDateTime startWorkClockTime = classesClockPO.getStartWorkClockTime();
            LocalDateTime endWorkClockTime = classesClockPO.getEndWorkClockTime();

            if (startWorkClockTime == null) {
                classesClockPO.setStartWorkClockStatus(ClockStatusEnum.EXP_NOCARD.name());
                if (startLeavePost) {
                    classesClockPO.setStartTrigger(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                    classesClockPO.setStartWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    classesClockPO.setStartWorkClockTime(vo.getStartRealTime());
                }
                if (startLeave.get()) {
                    classesClockPO.setStartTrigger(ClassesStatusTriggerEnum.LEAVE.name());
                    classesClockPO.setStartWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    classesClockPO.setStartWorkClockTime(vo.getStartRealTime());
                }
            }
            if (endWorkClockTime == null && this.checkEndWorkTimeOut(vo.getAfterEndRealTime(), currentDate)) {
                classesClockPO.setEndWorkClockStatus(ClockStatusEnum.EXP_NOCARD.name());
                if (endLeavePost) {
                    classesClockPO.setEndTrigger(ClassesStatusTriggerEnum.LEAVE_THE_POST.name());
                    classesClockPO.setEndWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    classesClockPO.setEndWorkClockTime(vo.getEndRealTime());
                }
                if (endLeave.get()) {
                    classesClockPO.setEndTrigger(ClassesStatusTriggerEnum.LEAVE.name());
                    classesClockPO.setEndWorkClockStatus(ClockStatusEnum.NORMAL.name());
                    classesClockPO.setEndWorkClockTime(vo.getEndRealTime());
                }
                if (!endStatusBool) {
                    this.changeWorkingStatus(classesClockPO.getUserId());
                }
            }
            classesClockPO.parseClassesClockStatus();
            classesClockMapper.updateById(classesClockPO);
        }
    }

}
