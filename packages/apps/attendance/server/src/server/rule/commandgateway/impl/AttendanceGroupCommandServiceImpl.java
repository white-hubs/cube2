package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.SubmitWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.AttendanceGroupCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupPunchCardWayCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupSupplementCardRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupWorkOvertimeRuleCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.AttendanceGroupService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupPunchCardWayMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupSupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupWorkOvertimeRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupPunchCardWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPunchCardWayPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupSupplementCardRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupWorkOvertimeRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.rocketmq.message.AttendanceGroupStaffMessage;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.AttendanceGroupValidator;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceGroupCommandServiceImpl extends ServiceImpl<AttendanceGroupMapper, AttendanceGroupPO> implements AttendanceGroupCommandService {

    private final AttendanceGroupValidator attendanceGroupValidator;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final AttendanceGroupClassesMapper attendanceGroupClassesMapper;
    private final AttendanceGroupPunchCardWayMapper attendanceGroupPunchCardWayMapper;
    private final AttendanceGroupWorkOvertimeRuleMapper attendanceGroupWorkOvertimeRuleMapper;
    private final AttendanceGroupSupplementCardRuleMapper attendanceGroupSupplementCardRuleMapper;
    private final WorkCommandService workCommandService;
    private final ClassesValidator classesValidator;
    private final StaffClient staffClient;
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final AttendanceGroupService attendanceGroupService;
    private final DutyWorkMapper dutyWorkMapper;
    private final DutyWorkClassesMapper dutyWorkClassesMapper;

    @Override
    public Long saveOrUpdate(SubmitAttendanceGroupCommand command) {
        Long id = command.getId();
        attendanceGroupValidator.verifyIsExistByName(id, command.getName());
        attendanceGroupValidator.verifyIsExistByStaff(command);
        // 本次操作动作是否是新增
        boolean actionIsSaved = false;
        // 修改之前初始的员工IDS
        List<Long> originalStaffIds = new ArrayList<>();
        if (null == id) {
            AttendanceGroupPO attendanceGroupPO = ObjectUtils.copying(command, AttendanceGroupPO.class);
            attendanceGroupService.addAttendanceGroup(attendanceGroupPO);
            id = attendanceGroupPO.getId();
            actionIsSaved = true;
        } else {
            AttendanceGroupPO saved = this.getById(id);
            Assert.isNotFound(saved == null, "考勤组不存在");
            Assert.isNotAcceptable(!command.getType().equals(saved.getType()), "考勤组类型不能更改");
            this.getOriginalStaffIds(id, saved.getType(), saved.getRanges(), originalStaffIds);
            // 筛选考勤组删减的人员IDS
            List<Long> removeStaffIds = this.filterStaffIds(command, saved);
            workCommandService.removeWorkData(removeStaffIds, command.getType());
            ObjectUtils.copying(command, saved);
            this.updateById(saved);
        }
        this.saveOrUpdateOtherConfig(id, command);
        // 生成排班或者值班数据，不影响当前班次
        this.generateWork(id, command, originalStaffIds);
        this.sendMessage(actionIsSaved, id, command.getType(), command.getRanges(), originalStaffIds, command.getStaffPOS());

        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AttendanceGroupPO groupPO = this.getById(id);
        if (groupPO == null) {
            return;
        }
        // 将该考勤组下的人当天班次之后的当月排班数据置为休息
        // 这里不需要清除未被引用的排班班次，因为有定时任务每月清除(clearArrangeAndDutyClassesId)
        if (AttendanceGroupTypeEnum.ARRANGE.name().equals(groupPO.getType())) {
            this.cleanAfterArrangeWorkData(id);
        } else if (AttendanceGroupTypeEnum.DUTY.name().equals(groupPO.getType())) {
            this.cleanAfterDutyWorkData(id);
        }
        // 删除考勤组下的人员
        attendanceGroupStaffMapper.delete(Wrappers.<AttendanceGroupStaffPO>lambdaQuery().eq(AttendanceGroupStaffPO::getGroupId, id));
        attendanceGroupService.deleteAttendanceGroup(groupPO);

    }

    /**
     * 清除当前班次之后的排班数据
     *
     * @param id
     */
    public void cleanAfterArrangeWorkData(Long id) {
        arrangeWorkMapper.cleanArrangeWorkClassesIds(Wrappers.<ArrangeWorkPO>lambdaQuery()
                                                             .eq(ArrangeWorkPO::getGroupId, id)
                                                             .eq(ArrangeWorkPO::getMonth, DateUtils.format(LocalDateTime.now(), "yyyy-MM"))
                                                             .gt(ArrangeWorkPO::getDate, DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd"))
                                                    );
        // 处理当天其他班次
        List<ArrangeWorkPO> arrangeWorkPOS = arrangeWorkMapper.getTodayNoStartClasses(Wrappers.query()
                                                                                              .eq("aw.group_id", id)
                                                                                              .eq("aw.`month`", DateUtils.format(LocalDateTime.now(), "yyyy-MM"))
                                                                                              .eq("aw.`date`", DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd"))
                                                                                              .gt("awc.start_real_time", DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"))
                                                                                     );
        if (arrangeWorkPOS != null && !arrangeWorkPOS.isEmpty()) {
            arrangeWorkPOS.forEach(arrangeWorkPO -> {
                StringBuilder updClassId = new StringBuilder();
                String classId = arrangeWorkPO.getArrangeWorkClassesIds();
                if (StringUtils.isNotEmpty(classId)) {
                    String[] classIds = classId.split(",");
                    for (String s : classIds) {
                        ArrangeWorkClassesPO classesPO = arrangeWorkClassesMapper.selectById(s);
                        if (DateUtils.toTimestamp(classesPO.getStartRealTime()) <= DateUtils.toTimestamp(LocalDateTime.now())) {
                            updClassId.append(",").append(classesPO.getId());
                        }
                    }
                    String cds = "";
                    if (StringUtils.isNotEmpty(updClassId)) {
                        cds = updClassId.substring(1);
                    }
                    arrangeWorkMapper.updateArrangeWorkClassesIds(cds, arrangeWorkPO.getId());
                }
            });
        }
    }

    /**
     * 清除当前班次之后的值班数据
     *
     * @param id
     */
    public void cleanAfterDutyWorkData(Long id) {
        dutyWorkMapper.cleanDutyWorkClassesIds(Wrappers.<DutyWorkPO>lambdaQuery()
                                                       .eq(DutyWorkPO::getGroupId, id)
                                                       .eq(DutyWorkPO::getMonth, DateUtils.format(LocalDateTime.now(), "yyyy-MM"))
                                                       .gt(DutyWorkPO::getDate, DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd"))
                                              );
        // 处理当天其他班次
        List<DutyWorkPO> dutyWorkPOS = dutyWorkMapper.selectList(Wrappers.<DutyWorkPO>lambdaQuery()
                                                                         .eq(DutyWorkPO::getGroupId, id)
                                                                         .eq(DutyWorkPO::getMonth, DateUtils.format(LocalDateTime.now(), "yyyy-MM"))
                                                                         .eq(DutyWorkPO::getDate, DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd"))

                                                                );

        if (CollectionUtils.isNotEmpty(dutyWorkPOS)) {
            dutyWorkPOS.forEach(dutyWorkPO -> {
                StringBuilder updClassId = new StringBuilder();
                String classId = dutyWorkPO.getDutyWorkClassesIds();
                if (StringUtils.isNotEmpty(classId)) {
                    String[] classIds = classId.split(",");
                    for (String s : classIds) {
                        DutyWorkClassesPO classesPO = dutyWorkClassesMapper.selectById(s);
                        if (ClassesMarkEnum.All_DAY_REST.name().equals(classesPO.getMark())) {
                            continue;
                        }
                        LocalTime localTime = LocalTime.now();
                        LocalTime startLocalTime = LocalTime.parse(classesPO.getStartWorkTime() + ":00");
                        if (startLocalTime.isBefore(localTime) || startLocalTime.equals(localTime)) {
                            updClassId.append(",").append(classesPO.getId());
                        }
                    }
                    if (StringUtils.isNotEmpty(updClassId)) {
                        String cds = updClassId.substring(1);
                        dutyWorkMapper.updateDutyWorkClassesIds(cds, dutyWorkPO.getId());
                    }

                }
            });
        }
    }

    // 查询考勤组修改之前的员工IDS
    private void getOriginalStaffIds(Long id, String type, String ranges, List<Long> originalStaffIds) {
        if (AttendanceGroupTypeEnum.DUTY.name().equals(type)) {
            return;
        }
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(ranges)) {
            originalStaffIds.addAll(staffClient.getList(new StaffQuery()).stream().map(StaffDetailDTO::getId).toList());
        } else {
            originalStaffIds.addAll(attendanceGroupStaffMapper.selectList(Wrappers.<AttendanceGroupStaffPO>lambdaQuery()
                                                                                  .eq(AttendanceGroupStaffPO::getGroupId, id)).stream()
                                            .map(AttendanceGroupStaffPO::getStaffId).toList());
        }
    }

    // 新增修改考勤组时的人员变动消息
    private void sendMessage(boolean actionIsSaved, Long id, String type, String ranges, List<Long> originalStaffIds,
                             List<SubmitAttendanceGroupStaffCommand> staffPOS
                            ) {
        if (AttendanceGroupTypeEnum.DUTY.name().equals(type)) {
            return;
        }
        List<Long> addStaffIds = new ArrayList<>();
        List<Long> delStaffIds = new ArrayList<>();
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(ranges)) {
            List<Long> allStaffIds = staffClient.getList(new StaffQuery()).stream().map(StaffDetailDTO::getId).collect(Collectors.toList());
            if (actionIsSaved) {
                addStaffIds.addAll(allStaffIds);
            } else {
                this.filterStaffIds(originalStaffIds, allStaffIds, addStaffIds, delStaffIds);
            }
        } else {
            List<Long> newStaffIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(staffPOS)) {
                newStaffIds.addAll(staffPOS.stream().map(SubmitAttendanceGroupStaffCommand::getStaffId).toList());
            }
            if (actionIsSaved) {
                addStaffIds.addAll(newStaffIds);
            } else {
                this.filterStaffIds(originalStaffIds, newStaffIds, addStaffIds, delStaffIds);
            }
        }
        // 发出修改考勤组时的人员变动消息
        RocketMQContextHolder.send(AttendanceGroupStaffMessage.TOPIC, AttendanceGroupStaffMessage.builder()
                                           .groupId(id)
                                           .addStaffIds(addStaffIds)
                                           .delStaffIds(delStaffIds)
                                           .build()
                                  );
    }

    // 对比修改考勤组时的员工，筛选出新增的和删除的
    private void filterStaffIds(List<Long> originalStaffIds, List<Long> newStaffIds, List<Long> addStaffIds, List<Long> delStaffIds) {
        originalStaffIds.forEach(oriId -> {
            if (!newStaffIds.contains(oriId)) {
                delStaffIds.add(oriId);
            }
        });
        newStaffIds.forEach(newId -> {
            if (!originalStaffIds.contains(newId)) {
                addStaffIds.add(newId);
            }
        });
    }

    // 筛选关联人员，删减的人员要删除其排班数据
    private List<Long> filterStaffIds(SubmitAttendanceGroupCommand command, AttendanceGroupPO saved) {
        List<Long> removeStaffIds = new ArrayList<>();
        // 1、全员组->全员组，部分->全员 无删减
        // 2、全员组->部分，部分->部分 有删减
        if (AttendanceGroupRangesEnum.All_STAFF.name().equals(saved.getRanges())) {
            if (AttendanceGroupRangesEnum.All_STAFF.name().equals(command.getRanges())) {
                return removeStaffIds;
            }
            List<StaffDetailDTO> allStaffs = staffClient.getList(new StaffQuery());
            List<SubmitAttendanceGroupStaffCommand> staffPOS = command.getStaffPOS();
            removeStaffIds.addAll(allStaffs.stream().map(StaffDetailDTO::getId)
                                          .filter(id -> staffPOS.stream().noneMatch(s -> id.equals(s.getStaffId()))).toList());
        } else {
            if (AttendanceGroupRangesEnum.All_STAFF.name().equals(command.getRanges())) {
                return removeStaffIds;
            }
            List<AttendanceGroupStaffPO> savedStaffPOS = attendanceGroupStaffMapper.selectList(Wrappers
                                                                                                       .<AttendanceGroupStaffPO>lambdaQuery()
                                                                                                       .eq(AttendanceGroupStaffPO::getGroupId, saved.getId()));
            List<SubmitAttendanceGroupStaffCommand> staffPOS = command.getStaffPOS();
            removeStaffIds.addAll(savedStaffPOS.stream()
                                          .map(AttendanceGroupStaffPO::getStaffId)
                                          .filter(staffId -> staffPOS.stream().noneMatch(s -> staffId.equals(s.getStaffId()))).toList());
        }
        return removeStaffIds;
    }

    private void generateWork(Long id, SubmitAttendanceGroupCommand command, List<Long> originalStaffIds) {
        //TODO 如果班次类型是排班制那么不能去更新已有人员的排班数据，会删除掉所有排班数据(ps:考勤组的人员没有生成排班数据，第二次修改时就再也生成不了了)
        SubmitWorkCommand workCommand = ObjectUtils.copying(command, SubmitWorkCommand.class);
        workCommand.setGroupId(id);
        workCommandService.generateWorkData(workCommand);
    }

    private void saveOrUpdateOtherConfig(Long id, SubmitAttendanceGroupCommand command) {
        attendanceGroupStaffMapper.delete(Wrappers.<AttendanceGroupStaffPO>lambdaQuery().eq(AttendanceGroupStaffPO::getGroupId, id));
        if (AttendanceGroupRangesEnum.PART_STAFF.name().equals(command.getRanges())) {
            List<SubmitAttendanceGroupStaffCommand> staffPOS = command.getStaffPOS();
            Assert.isParameterError(CollectionUtils.isEmpty(staffPOS), "非适用所有人的情况，必须选择人员");
            staffPOS.forEach(staff -> {
                staff.setId(null);
                staff.setGroupId(id);
                AttendanceGroupStaffPO groupStaffPO = ObjectUtils.copying(staff, AttendanceGroupStaffPO.class);
                groupStaffPO.setGroupType(command.getType());
                try {
                    attendanceGroupStaffMapper.insert(groupStaffPO);
                } catch (DuplicateKeyException e) {
                    throw new ServiceException(staff.getStaffName() + "已经存在于其他考勤组中");
                }
            });
        }
        if (AttendanceTypeEnum.FIXED.name().equals(command.getClassesType())) {
            List<SubmitAttendanceGroupClassesCommand> classesPOS = command.getClassesPOS();
            // 校验班次时间不能交叉
            classesValidator.verifyIsCrossByWeek(classesPOS);
            attendanceGroupClassesMapper.delete(Wrappers.<AttendanceGroupClassesPO>lambdaQuery()
                                                        .eq(AttendanceGroupClassesPO::getGroupId, id));
            classesPOS.forEach(classesPO -> {
                List<Classes> classes = classesPO.getClasses();
                // 如果当天排班的班次为空，则记录一条空数据
                if (CollectionUtils.isEmpty(classes)) {
                    AttendanceGroupClassesPO saved = new AttendanceGroupClassesPO();
                    saved.setGroupId(id);
                    saved.setWeek(classesPO.getWeek());
                    attendanceGroupClassesMapper.insert(saved);
                } else {
                    // 如果第一次走上面逻辑保存了一条空数据后，在编辑的时候要把这条班次找出来特殊处理
                    if (StringUtils.isEmpty(classes.getFirst().getName())) {
                        // 如果没有班次名称则说明是空数据，重新保存一份，并把提交的班次数组置为空数组，这样排班的时候就才不会排班
                        AttendanceGroupClassesPO saved = new AttendanceGroupClassesPO();
                        saved.setId(null);
                        saved.setClassesId(null);
                        saved.setGroupId(id);
                        saved.setWeek(classesPO.getWeek());
                        attendanceGroupClassesMapper.insert(saved);
                        classesPO.setClasses(new ArrayList<>());
                    } else {
                        classes.forEach(c -> {
                            AttendanceGroupClassesPO saved = ObjectUtils.copying(c, AttendanceGroupClassesPO.class);
                            saved.setId(null);
                            saved.setGroupId(id);
                            saved.setWeek(classesPO.getWeek());
                            attendanceGroupClassesMapper.insert(saved);
                        });
                    }
                }
            });
        }
        attendanceGroupPunchCardWayMapper.delete(Wrappers.<AttendanceGroupPunchCardWayPO>lambdaQuery()
                                                         .eq(AttendanceGroupPunchCardWayPO::getGroupId, id));
        if (command.getIsPunchCard()) {
            List<String> punchCardWays = command.getPunchCardWay();
            Assert.isParameterError(CollectionUtils.isEmpty(punchCardWays), "必须选择打卡方式");
            this.insertPunchWay(command.getPunchCardWayWifi(), id, AttendanceGroupPunchCardWayEnum.WIFI.name());
            this.insertPunchWay(command.getPunchCardWayGps(), id, AttendanceGroupPunchCardWayEnum.GPS.name());
            this.insertPunchWay(command.getPunchCardWayMachine(), id, AttendanceGroupPunchCardWayEnum.MACHINE.name());
        }
        attendanceGroupSupplementCardRuleMapper.delete(Wrappers.<AttendanceGroupSupplementCardRulePO>lambdaQuery()
                                                               .eq(AttendanceGroupSupplementCardRulePO::getGroupId, id));
        if (command.getIsSupplementCard()) {
            List<SubmitAttendanceGroupSupplementCardRuleCommand> supplementCardRulePOS = command.getSupplementCardRulePOS();
            Assert.isParameterError(CollectionUtils.isEmpty(supplementCardRulePOS), "必须选择补卡规则");
            Set<String> ruleTypes = supplementCardRulePOS.stream().map(SubmitAttendanceGroupSupplementCardRuleCommand::getType)
                    .collect(Collectors.toSet());
            Assert.isParameterError(ruleTypes.size() != supplementCardRulePOS.size(), "补卡类型不能重复");
            supplementCardRulePOS.forEach(rulePO -> {
                AttendanceGroupSupplementCardRulePO cardRulePO = ObjectUtils.copying(rulePO, AttendanceGroupSupplementCardRulePO.class);
                cardRulePO.setId(null);
                cardRulePO.setGroupId(id);
                attendanceGroupSupplementCardRuleMapper.insert(cardRulePO);
            });
        }
        attendanceGroupWorkOvertimeRuleMapper.delete(Wrappers.<AttendanceGroupWorkOvertimeRulePO>lambdaQuery()
                                                             .eq(AttendanceGroupWorkOvertimeRulePO::getGroupId, id));
        if (command.getIsWorkOvertime()) {
            List<SubmitAttendanceGroupWorkOvertimeRuleCommand> workOvertimeRulePOS = command.getWorkOvertimeRulePOS();
            Assert.isParameterError(CollectionUtils.isEmpty(workOvertimeRulePOS), "必须选择加班规则");
            workOvertimeRulePOS.forEach(rulePO -> {
                AttendanceGroupWorkOvertimeRulePO overtimeRulePO = ObjectUtils.copying(rulePO, AttendanceGroupWorkOvertimeRulePO.class);
                overtimeRulePO.setId(null);
                overtimeRulePO.setGroupId(id);
                attendanceGroupWorkOvertimeRuleMapper.insert(overtimeRulePO);
            });
        }
    }

    private void insertPunchWay(List<SubmitAttendanceGroupPunchCardWayCommand> punchCardWayPOS, Long groupId, String way) {
        if (CollectionUtils.isNotEmpty(punchCardWayPOS)) {
            punchCardWayPOS.forEach(wayPO -> {
                wayPO.setGroupId(groupId);
                wayPO.setWay(way);
                attendanceGroupPunchCardWayMapper.insert(ObjectUtils.copying(wayPO, AttendanceGroupPunchCardWayPO.class));
            });
        }
    }

}
