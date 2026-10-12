package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttExchangeCheckDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttClassExchangeQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttArrangeTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.AttHandlerDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 换班
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassExchangeCommandService {

    private final AttClassExchangeQueryService attClassExchangeQueryService;
    private final AttSchedulingMapper attSchedulingMapper;
    private final AttDutyMapper attDutyMapper;
    private final AttGroupRepository attGroupRepository;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final UserClient userClient;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttSchedulingCommandService attSchedulingCommandService;

    /**
     * 换班
     *
     * @param param 入参
     */
    public void classExchange(ClassesChangeCommand param) {
        AttExchangeCheckDTO checkDTO = new AttExchangeCheckDTO();
        BeanUtils.copyProperties(param, checkDTO);
        UserDTO fromUser = userClient.getUser(param.getApplicantId());
        UserDTO toUser = userClient.getUser(param.getChangerId());
        AttGroupPO fromGroup = attGroupRepository.getGroupByStaffId(param.getApplicantId());
        AttSchedulingRulePO fromSchedulingRule = null;
        if (fromGroup != null) {
            fromSchedulingRule = attSchedulingRuleMapper.selectById(fromGroup.getSchedulingRuleId());
        }
        AttGroupPO toGroup = attGroupRepository.getGroupByStaffId(param.getChangerId());
        AttSchedulingRulePO toSchedulingRule = null;
        if (toGroup != null) {
            toSchedulingRule = attSchedulingRuleMapper.selectById(toGroup.getSchedulingRuleId());
        }
        // 换班
        if (AttArrangeTypeEnum.DUTY.name().equals(param.getWorkType())) {
            this.dutyClassExchange(checkDTO, fromUser, toUser);
        } else {
            this.schedulingClassExchange(checkDTO, fromUser, toUser, fromGroup, toGroup, fromSchedulingRule, toSchedulingRule);
        }
    }

    /**
     * 排班换班
     *
     * @param param 换班对象
     */
    public void schedulingClassExchange(AttExchangeCheckDTO param, UserDTO fromUser, UserDTO toUser, AttGroupPO fromGroup,
                                        AttGroupPO toGroup, AttSchedulingRulePO fromRule, AttSchedulingRulePO toRule
                                       ) {
        Long fromId = param.getApplicantId();
        Long toId = param.getChangerId();
        LocalDateTime fromDate = DateUtils.beginOfDay(param.getFromDate());
        LocalDateTime toDate = DateUtils.beginOfDay(param.getToDate());
        Long fromClassId = param.getFromClassesId();
        Long toClassId = param.getToClassesId();
        // 1.获取交换班次
        AttSchedulingPO fromScheduling = null;
        AttSchedulingPO toScheduling = null;
        List<AttSchedulingPO> fromDaySchedulingList = null;
        if (fromClassId != null && fromClassId > 0) {
            fromDaySchedulingList = attSchedulingQueryService.getStaffDaySchedulingPO(fromId, DateUtils.formatDate(fromDate));
            if (fromDaySchedulingList != null && !fromDaySchedulingList.isEmpty()) {
                for (AttSchedulingPO fromDayScheduling : fromDaySchedulingList) {
                    if (fromClassId.equals(fromDayScheduling.getClassesId())) {
                        fromScheduling = fromDayScheduling;
                        break;
                    }
                }
            }
        }
        List<AttSchedulingPO> toDaySchedulingList = null;
        if (toClassId != null && toClassId > 0) {
            toDaySchedulingList = attSchedulingQueryService.getStaffDaySchedulingPO(toId, DateUtils.formatDate(toDate));
            if (toDaySchedulingList != null && !toDaySchedulingList.isEmpty()) {
                for (AttSchedulingPO po : toDaySchedulingList) {
                    if (toClassId.equals(po.getClassesId())) {
                        toScheduling = po;
                    }
                }
            }
        }
        // 2. 校验
        attClassExchangeQueryService.arrangeChangeCheck(param, fromUser, toUser, fromScheduling, toScheduling);
        // 3. 固定需要变动的班次
        attSchedulingCommandService.fixedNowGroupScheduling(fromUser.getId(), fromGroup, DateUtils.newLocalDateTime(fromDate), String.valueOf(fromId));
        attSchedulingCommandService.fixedNowGroupScheduling(fromUser.getId(), fromGroup, DateUtils.newLocalDateTime(toDate), String.valueOf(fromId));
        attSchedulingCommandService.fixedNowGroupScheduling(fromUser.getId(), fromGroup, DateUtils.newLocalDateTime(fromDate), String.valueOf(toId));
        attSchedulingCommandService.fixedNowGroupScheduling(fromUser.getId(), fromGroup, DateUtils.newLocalDateTime(toDate), String.valueOf(toId));
        long nowLong = DateUtils.toTimestamp(LocalDateTime.now());
        // 4.移除原来的班次
        if (fromScheduling != null) {
            attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                               .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(fromDate))
                                               .eq(AttSchedulingPO::getStaffId, fromId)
                                               .eq(AttSchedulingPO::getClassesId, fromClassId)
                                               .set(AttSchedulingPO::getDeleted, nowLong)
                                      );
        } else {
            attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                               .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(fromDate))
                                               .eq(AttSchedulingPO::getStaffId, fromId)
                                               .set(AttSchedulingPO::getDeleted, nowLong)
                                      );
        }
        if (toScheduling != null) {
            attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                               .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(toDate))
                                               .eq(AttSchedulingPO::getStaffId, toId)
                                               .eq(AttSchedulingPO::getClassesId, toClassId)
                                               .set(AttSchedulingPO::getDeleted, nowLong)
                                      );
        } else {
            attSchedulingMapper.update(null, Wrappers.<AttSchedulingPO>lambdaUpdate()
                                               .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(toDate))
                                               .eq(AttSchedulingPO::getStaffId, toId)
                                               .set(AttSchedulingPO::getDeleted, nowLong)
                                      );
        }

        // 5.交换
        if (Objects.equals(fromId, toId)) {
            /* 自己和自己换 */

            // 组装交换班次
            Map<LocalDateTime, AttSchedulingPO> schedulingChangMap = AttHandlerDomain.handleSchedulingChang(fromScheduling, toScheduling, fromDate, toDate);
            AttSchedulingPO fromAddSchedulingPO = schedulingChangMap.get(fromDate);
            AttSchedulingPO toAddSchedulingPO = schedulingChangMap.get(toDate);

            if (fromAddSchedulingPO != null) {
                if (fromGroup != null) {
                    fromAddSchedulingPO.setStaffGroupId(fromGroup.getId());
                    fromAddSchedulingPO.setStaffGroupName(fromGroup.getName());
                    fromAddSchedulingPO.setStaffGroupRanges(fromGroup.getRanges());
                }

                attSchedulingMapper.insert(fromAddSchedulingPO);
            }
            if (toAddSchedulingPO != null) {
                if (toGroup != null) {
                    toAddSchedulingPO.setStaffGroupId(toGroup.getId());
                    toAddSchedulingPO.setStaffGroupName(toGroup.getName());
                    toAddSchedulingPO.setStaffGroupRanges(toGroup.getRanges());
                }
                attSchedulingMapper.insert(toAddSchedulingPO);
            }

        } else {
            // 自己和别人换

            AttSchedulingPO fromAddScheduling = null;
            AttSchedulingPO toAddScheduling = null;

            if (fromScheduling != null) {
                if (toGroup == null) {
                    toGroup = AttGroupPO.builder().build();
                }
                // 加入新的
                toAddScheduling = AttSchedulingPO.builder().build();
                BeanUtils.copyProperties(fromScheduling, toAddScheduling);
                toAddScheduling.setId(IdWorker.getId());
                toAddScheduling.setStaffId(toUser.getId());
                toAddScheduling.setStaffName(toUser.getName());
                toAddScheduling.setStaffDeptId(toUser.getOrganization() == null ? null : toUser.getOrganization().getId());
                toAddScheduling.setStaffDeptName(toUser.getOrganization() == null ? null : toUser.getOrganization().getName());
                toAddScheduling.setHandScheduling(true);
                toAddScheduling.setUpdateTime(LocalDateTime.now());
                toAddScheduling.setStaffGroupId(toGroup.getId());
                toAddScheduling.setSchedulingRuleId(toGroup.getSchedulingRuleId());
                toAddScheduling.setClockRuleId(toGroup.getClockRuleId());
                toAddScheduling.setStaffGroupName(toGroup.getName());
                if (LocalDateTime.now().isAfter(fromDate)) {
                    toAddScheduling.setStarted(true);
                }
                attSchedulingMapper.insert(toAddScheduling);
            }
            if (toScheduling != null) {
                if (fromGroup == null) {
                    fromGroup = AttGroupPO.builder().build();
                }
                // 加入新的
                fromAddScheduling = AttSchedulingPO.builder().build();
                BeanUtils.copyProperties(toScheduling, fromAddScheduling);
                fromAddScheduling.setId(IdWorker.getId());
                fromAddScheduling.setStaffId(fromUser.getId());
                fromAddScheduling.setStaffName(fromUser.getName());
                fromAddScheduling.setStaffDeptId(fromUser.getOrganization() == null ? null : fromUser.getOrganization().getId());
                fromAddScheduling.setStaffDeptName(fromUser.getOrganization() == null ? null : fromUser.getOrganization().getName());
                fromAddScheduling.setHandScheduling(true);
                fromAddScheduling.setUpdateTime(LocalDateTime.now());
                fromAddScheduling.setStaffGroupId(fromGroup.getId());
                fromAddScheduling.setSchedulingRuleId(fromGroup.getSchedulingRuleId());
                fromAddScheduling.setClockRuleId(fromGroup.getClockRuleId());
                fromAddScheduling.setStaffGroupName(fromGroup.getName());
                if (LocalDateTime.now().isAfter(toDate)) {
                    fromAddScheduling.setStarted(true);
                }
                attSchedulingMapper.insert(fromAddScheduling);
            }
        }
        // 6.原来有班次移除后无班次需要加入一个休息的班次（不然会被替换成规则的班次）
        if (fromScheduling != null) {
            List<AttSchedulingPO> fromDayList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                       .eq(AttSchedulingPO::getStaffId, fromId)
                                                                                       .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(fromDate))
                                                                              );
            if (fromDayList == null || fromDayList.isEmpty()) {
                AttSchedulingPO fromRestPO = AttHandlerDomain.handleRestScheduling(fromUser, DateUtils.newLocalDateTime(fromDate), fromGroup, fromRule, String.valueOf(fromId));
                attSchedulingMapper.insert(fromRestPO);
            }
        }
        if (toScheduling != null) {
            List<AttSchedulingPO> toDayList = attSchedulingMapper.selectList(Wrappers.<AttSchedulingPO>lambdaQuery()
                                                                                     .eq(AttSchedulingPO::getStaffId, toId)
                                                                                     .eq(AttSchedulingPO::getSchedulingDate, DateUtils.formatDate(toDate))
                                                                            );
            if (toDayList == null || toDayList.isEmpty()) {
                AttSchedulingPO toRestPO = AttHandlerDomain.handleRestScheduling(toUser, DateUtils.newLocalDateTime(toDate), toGroup, toRule, String.valueOf(toId));
                attSchedulingMapper.insert(toRestPO);
            }
        }
    }

    /**
     * 值班换班
     *
     * @param param 换班对象
     */
    public void dutyClassExchange(AttExchangeCheckDTO param, UserDTO fromUser, UserDTO toUser) {
        Long fromId = param.getApplicantId();
        Long toId = param.getChangerId();
        LocalDateTime fromDate = param.getFromDate();
        LocalDateTime toDate = param.getToDate();
        Long fromClassId = param.getFromClassesId();
        Long toClassId = param.getToClassesId();
        AttDutyPO fromDuty = null;
        AttDutyPO toDuty = null;
        long nowLong = DateUtils.toTimestamp(LocalDateTime.now());
        /* 1.获取交换班次 */
        if (fromClassId != null && fromClassId > 0) {
            List<AttDutyPO> fromDutyTempList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                                .eq(AttDutyPO::getDutyDate, DateUtils.formatDate(fromDate))
                                                                                .eq(AttDutyPO::getStaffId, fromId)
                                                                                .eq(AttDutyPO::getClassesId, fromClassId)

                                                                       );
            if (fromDutyTempList != null && !fromDutyTempList.isEmpty()) {
                fromDuty = fromDutyTempList.getFirst();
            }
        }

        if (toClassId != null && toClassId > 0) {
            List<AttDutyPO> toDutyTempList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                              .eq(AttDutyPO::getDutyDate, DateUtils.formatDate(toDate))
                                                                              .eq(AttDutyPO::getStaffId, toId)
                                                                              .eq(AttDutyPO::getClassesId, toClassId)

                                                                     );
            if (toDutyTempList != null && !toDutyTempList.isEmpty()) {
                toDuty = toDutyTempList.getFirst();
            }
        }
        // 校验
        attClassExchangeQueryService.dutyChangeCheck(param, fromUser, toUser, fromDuty, toDuty);

        /* 2.移除交换班次 */
        if (fromDuty != null) {
            attDutyMapper.update(null, Wrappers.<AttDutyPO>lambdaUpdate()
                                         .eq(AttDutyPO::getDutyDate, fromDate)
                                         .eq(AttDutyPO::getStaffId, fromId)
                                         .eq(AttDutyPO::getClassesId, fromClassId)
                                         .set(AttDutyPO::getDeleted, nowLong)
                                );
        } else {
            attDutyMapper.update(null, Wrappers.<AttDutyPO>lambdaUpdate()
                                         .eq(AttDutyPO::getDutyDate, fromDate)
                                         .eq(AttDutyPO::getStaffId, fromId)
                                         .set(AttDutyPO::getDeleted, nowLong)
                                );
        }

        if (toDuty != null) {
            attDutyMapper.update(null, Wrappers.<AttDutyPO>lambdaUpdate()
                                         .eq(AttDutyPO::getDutyDate, toDate)
                                         .eq(AttDutyPO::getStaffId, toId)
                                         .eq(AttDutyPO::getClassesId, toClassId)
                                         .set(AttDutyPO::getDeleted, nowLong)
                                );
        } else {
            attDutyMapper.update(null, Wrappers.<AttDutyPO>lambdaUpdate()
                                         .eq(AttDutyPO::getDutyDate, toDate)
                                         .eq(AttDutyPO::getStaffId, toId)
                                         .set(AttDutyPO::getDeleted, nowLong)
                                );
        }

        /* 3.组装交换班次 */
        AttDutyPO fromAddDuty = null;
        AttDutyPO toAddDuty = null;
        if (Objects.equals(fromId, toId)) { // 自己和自己换
            Map<LocalDateTime, AttDutyPO> changeDutyAddMap = AttHandlerDomain.handleDutyChang(fromDuty, toDuty, fromDate, toDate);
            fromAddDuty = changeDutyAddMap.get(fromDate);
            toAddDuty = changeDutyAddMap.get(toDate);
        } else { // 自己和别人换
            if (toDuty != null) {
                fromAddDuty = AttDutyPO.builder().build();
                BeanUtils.copyProperties(toDuty, fromAddDuty);
                fromAddDuty.setStaffId(fromId);
                fromAddDuty.setStaffName(fromUser.getName());
                fromAddDuty.setStaffDeptId(fromUser.getOrganization() == null ? null : fromUser.getOrganization().getId());
                fromAddDuty.setStaffDeptName(fromUser.getOrganization() == null ? null : fromUser.getOrganization().getName());
                fromAddDuty.setId(IdWorker.getId());
                fromAddDuty.setUpdateTime(LocalDateTime.now());
            }
            if (fromDuty != null) {
                toAddDuty = AttDutyPO.builder().build();
                BeanUtils.copyProperties(fromDuty, toAddDuty);
                toAddDuty.setStaffId(toId);
                toAddDuty.setStaffName(toUser.getName());
                toAddDuty.setStaffDeptId(toUser.getOrganization() == null ? null : toUser.getOrganization().getId());
                toAddDuty.setStaffDeptName(toUser.getOrganization() == null ? null : toUser.getOrganization().getName());
                toAddDuty.setId(IdWorker.getId());
                toAddDuty.setUpdateTime(LocalDateTime.now());
            }
        }

        /* 4.添加交换班次 */
        if (fromAddDuty != null) {
            attDutyMapper.insert(fromAddDuty);
        }
        if (toAddDuty != null) {
            attDutyMapper.insert(toAddDuty);
        }
    }
}
