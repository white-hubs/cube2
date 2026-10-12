package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkClassesCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.DutyWorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command.SubmitArrangeAndDutyWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.WorkDateCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.validator.ArrangeAndDutyValidator;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.DutyWorkOperationService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupStaffVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.ClassesValidator;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.enumeration.StaffStatusEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author bq
 * @date 2022/11/23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkCommandServiceImpl extends ServiceImpl<DutyWorkMapper, DutyWorkPO> implements DutyWorkCommandService {

    private final DutyWorkClassesMapper dutyWorkClassesMapper;
    private final DutyWorkMapper dutyWorkMapper;
    private final ClassesValidator classesValidator;
    private final DutyWorkClassesCommandService dutyWorkClassesCommandService;
    private final ArrangeAndDutyValidator arrangeAndDutyValidator;
    private final StaffClient staffClient;
    private final AttendanceGroupQueryService groupQueryService;
    private final DutyWorkOperationService dutyWorkOperationService;

    @Override
    public void manualArrange(SubmitArrangeAndDutyWorkCommand submitArrangeAndDutyWorkCommand) {
        StaffDetailDTO detailDTO = staffClient.getById(submitArrangeAndDutyWorkCommand.getStaffId());
        Assert.isNotFound(Objects.isNull(detailDTO), "排班失败，该员工已被删除");
        Assert.isNotAcceptable(StaffStatusEnum.LEAVE_OFFICE.name().equals(detailDTO.getStatus()), "排班失败，该员工已离职");
        arrangeAndDutyValidator.verifyClasses(submitArrangeAndDutyWorkCommand, AttendanceGroupTypeEnum.DUTY.name());
        String format = DateUtils.format(submitArrangeAndDutyWorkCommand.getDate(), "yyyy-MM-dd");
        String today = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd");
        // 保存新的班次信息
        List<String> arrangeWorkClassesIds = dutyWorkClassesCommandService.saveClassesByOneDay(submitArrangeAndDutyWorkCommand.getClasses());
        boolean todayClassesIsUpdate = this.saveOrUpdateOneStaffOneDay(null, WorkDateCommand.builder()
                                                                               .date(submitArrangeAndDutyWorkCommand.getDate()).isHolidayAndAutoRest(false).build(),
                                                                       SubmitAttendanceGroupStaffCommand.builder()
                                                                               .staffId(submitArrangeAndDutyWorkCommand.getStaffId())
                                                                               .build(), arrangeWorkClassesIds, true);
        Assert.isNotAcceptable(format.equals(today) && !todayClassesIsUpdate, "当前班次及之前数据不能修改");
    }

    @Override
    public boolean saveOrUpdateOneStaffOneDay(Long groupId, WorkDateCommand data, SubmitAttendanceGroupStaffCommand staff,
                                              List<String> newWorkClassesIds, boolean isSaveLog
                                             ) {
        // 今日的班次是否被更改
        boolean todayClassesIsUpdate = false;
        // 查询用户当日排班数据
        String theDay = DateUtils.format(data.getDate(), "yyyy-MM-dd");
        DutyWorkPO theDayWorkData = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery()
                                                                     .eq(DutyWorkPO::getStaffId, staff.getStaffId())
                                                                     .eq(DutyWorkPO::getDate, theDay));
        // 如果当日没有排班数据，则新增一个
        if (null == theDayWorkData) {
            DutyWorkPO dutyWorkPO = ObjectUtils.copying(data, DutyWorkPO.class);
            dutyWorkPO.setStaffId(staff.getStaffId());
            dutyWorkPO.setStaffName(staff.getStaffName());
            dutyWorkPO.setStaffMobile(staff.getStaffMobile());
            dutyWorkPO.setStaffOrganizationId(staff.getStaffOrganizationId());
            dutyWorkPO.setDutyWorkClassesIds(String.join(",", newWorkClassesIds));
            dutyWorkPO.setGroupId(groupId);
            if (null == groupId) {
                this.setValueToAttrByManual(dutyWorkPO);
            }
            dutyWorkPO.setIsHoliday(data.getIsHolidayAndAutoRest() != null && data.getIsHolidayAndAutoRest());
            if (isSaveLog) {
                //非系统操作的才记日志
                dutyWorkOperationService.insertOperation(dutyWorkPO);
            }
            dutyWorkMapper.insert(dutyWorkPO);
        } else {
            DutyWorkPO oldPO = new DutyWorkPO();
            ObjectUtils.copying(theDayWorkData, oldPO);
            // 时间是否是今日，如果是今日则当前班次不能修改，之后的班次才能改
            String today = DateUtils.formatDate(LocalDateTime.now());
            if (today.equals(theDay)) {
                String oldWorkClassesIds = theDayWorkData.getDutyWorkClassesIds();
                // 筛选班次
                Set<String> saveClassesIds = new HashSet<>();
                this.contrastTodayClasses(oldWorkClassesIds, newWorkClassesIds, saveClassesIds);
                // 修改排班班次
                String saveClassesIdStr = String.join(",", saveClassesIds);
                todayClassesIsUpdate = !oldWorkClassesIds.equals(saveClassesIdStr);
                theDayWorkData.setDutyWorkClassesIds(saveClassesIdStr);
            } else {
                theDayWorkData.setDutyWorkClassesIds(String.join(",", newWorkClassesIds));
            }
            theDayWorkData.setIsHoliday(data.getIsHolidayAndAutoRest() != null && data.getIsHolidayAndAutoRest());
            theDayWorkData.setGroupId(groupId != null ? groupId : theDayWorkData.getGroupId());
            if (isSaveLog) {
                //非系统操作的才记日志
                dutyWorkOperationService.updateOperation(oldPO, theDayWorkData);
            }
            dutyWorkMapper.updateById(theDayWorkData);
        }
        return todayClassesIsUpdate;
    }

    @Override
    public void removeData(LocalDateTime date, Long staffId) {
        int betweenDay = (int) DateUtils.between(LocalDateTime.now(), date, ChronoUnit.DAYS, true);
        if (betweenDay == 0) {
            // 今日是否有值班
            DutyWorkPO theDayWorkData = dutyWorkMapper.selectOne(Wrappers.<DutyWorkPO>lambdaQuery().eq(DutyWorkPO::getStaffId, staffId)
                                                                         .eq(DutyWorkPO::getDate, DateUtils.formatDate(date)));
            if (null != theDayWorkData) {
                // 今日有排班，保留当前班次
                String currentClassesId = "";
                String dutyWorkClassesIdStr = theDayWorkData.getDutyWorkClassesIds();
                if (StringUtils.isNotEmpty(dutyWorkClassesIdStr)) {
                    String[] dutyWorkClassesIds = dutyWorkClassesIdStr.split(",");
                    for (String classesId : dutyWorkClassesIds) {
                        // 寻找是否有当前正在进行的班次
                        DutyWorkClassesPO arrangeWorkClassesPO = dutyWorkClassesMapper.selectById(classesId);
                        int compareClasses = classesValidator.nowDateCompareClasses(arrangeWorkClassesPO.getStartWorkTime(), arrangeWorkClassesPO.getEndWorkTime(), arrangeWorkClassesPO.getEndTimeIsMorrow());
                        if (compareClasses == 0) {
                            currentClassesId = classesId;
                            break;
                        }
                    }
                }
                if (StringUtils.isNotEmpty(currentClassesId)) {
                    theDayWorkData.setDutyWorkClassesIds(currentClassesId);
                    dutyWorkMapper.updateById(theDayWorkData);
                } else {
                    dutyWorkMapper.deleteById(theDayWorkData.getId());
                }
            }
        } else {
            dutyWorkMapper.delete(Wrappers.<DutyWorkPO>lambdaQuery().eq(DutyWorkPO::getDate, DateUtils.formatDate(date))
                                          .eq(DutyWorkPO::getStaffId, staffId));
        }
    }

    /**
     * 比对今日班次信息，筛选出保留的班次
     *
     * @param oldWorkClassesIdStr 旧的班次
     * @param newWorkClassesIds   新的班次
     * @param saveClassesIds      对比新老班次之后，筛选出来需要保存的当前班次ID
     */
    private void contrastTodayClasses(String oldWorkClassesIdStr, List<String> newWorkClassesIds, Set<String> saveClassesIds) {
        // 当前班次
        AtomicReference<DutyWorkClassesPO> currentClasses = new AtomicReference<>();
        if (StringUtils.isNotEmpty(oldWorkClassesIdStr)) {
            List<String> oldWorkClassesIds = Arrays.asList(oldWorkClassesIdStr.split(","));
            // 在旧班次中保留当前班次和当前时间之前的班次
            oldWorkClassesIds.forEach(oldWorkClassesId -> {
                // 寻找是否有当前正在进行的班次
                DutyWorkClassesPO arrangeWorkClassesPO = dutyWorkClassesMapper.selectById(oldWorkClassesId);
                // 当前和之前的班次保留，之后的班次删除
                int compareClasses = classesValidator.nowDateCompareClasses(arrangeWorkClassesPO.getStartWorkTime(), arrangeWorkClassesPO.getEndWorkTime(), arrangeWorkClassesPO.getEndTimeIsMorrow());
                if (compareClasses == 1) {
                    saveClassesIds.add(oldWorkClassesId);
                } else if (compareClasses == 0) {
                    saveClassesIds.add(oldWorkClassesId);
                    currentClasses.set(arrangeWorkClassesPO);
                }
            });
        }
        // 新的班次信息，只有在当前时间之后和当前班次之后的才能排班
        newWorkClassesIds.forEach(classesId -> {
            DutyWorkClassesPO dutyWorkClassesPO = dutyWorkClassesMapper.selectById(classesId);
            if (currentClasses.get() == null) {
                // 当前没有班次，则与当前时间比较
                int compareClasses = classesValidator.nowDateCompareClasses(dutyWorkClassesPO.getStartWorkTime(), dutyWorkClassesPO.getEndWorkTime(), dutyWorkClassesPO.getEndTimeIsMorrow());
                if (compareClasses == -1) {
                    // 当前时间在该班次上班之前，则可以排班
                    saveClassesIds.add(classesId);
                }
            } else {
                // 当前班次不为空，与当前班次比较
                int result = classesValidator.compareTwoClasses(ObjectUtils.copying(currentClasses.get(), Classes.class), ObjectUtils.copying(dutyWorkClassesPO, Classes.class));
                if (result == -1) {
                    // 新班次在当前班次之后，则可以排班
                    saveClassesIds.add(classesId);
                }
            }
        });
    }

    // 手动排班时属性设置
    private void setValueToAttrByManual(DutyWorkPO dutyWorkPO) {
        // 手动排班时，可能会存在当天没有排班的情况，那么基础数据需要关联查询
        // 查询员工信息
        StaffDetailDTO staffDTO = staffClient.getById(dutyWorkPO.getStaffId());
        Assert.isNotFound(staffDTO, "手动排班失败，未查询到该员工信息");
        dutyWorkPO.setStaffName(staffDTO.getName());
        dutyWorkPO.setStaffOrganizationId(staffDTO.getOrganizationId());
        dutyWorkPO.setStaffMobile(staffDTO.getMobile());
        // 查询员工所在的考勤组
        List<AttendanceGroupStaffVO> groupStaffVOS = groupQueryService.getGroupIdByStaffIds(Collections.singletonList(dutyWorkPO.getStaffId()), AttendanceGroupTypeEnum.DUTY.name());
        Assert.isNotFound(CollectionUtils.isEmpty(groupStaffVOS), "手动排班失败，未查询到该员工的考勤组信息");
        dutyWorkPO.setMonth(DateUtils.format(dutyWorkPO.getDate(), "yyyy-MM"));
        dutyWorkPO.setGroupId(groupStaffVOS.getFirst().getGroupId());
        dutyWorkPO.setWeek(dutyWorkPO.getDate().getDayOfWeek().getValue());
    }

}
