package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttExchangeCheckDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttArrangeTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.vo.AttKeyValueVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.AttHandlerDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

/**
 * 换班
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassExchangeQueryService {

    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttDutyMapper attDutyMapper;
    private final AttGroupRepository attGroupRepository;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final UserClient userClient;

    /**
     * 获取人员换班班次
     *
     * @param workType 换班方式（值班/排班
     * @param staffId  人员id
     * @param day      日期
     *
     * @return 班次
     */
    public List<AttKeyValueVO> getStaffChangeClasses(String workType, Long staffId, String day) {
        List<AttKeyValueVO> result = new ArrayList<>();
        if (staffId == null || StringUtils.isBlank(day)) {
            return result;
        }
        LocalDateTime dateTime = DateUtils.parseDate(day);
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new ServiceException("不能交换之前的排班");
        }

        // 获取该日期的班
        if (AttArrangeTypeEnum.DUTY.name().equals(workType)) {
            List<AttDutyPO> dutyPOList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                          .eq(AttDutyPO::getStaffId, staffId)
                                                                          .eq(AttDutyPO::getDutyDate, DateUtils.formatDate(dateTime))
                                                                 );
            if (dutyPOList == null || dutyPOList.isEmpty()) {
                result.add(AttKeyValueVO.builder().id(-1L).name("休息").build());
            } else {
                result.addAll(dutyPOList.stream().map(dto -> {
                    if (dto != null && dto.getClassesRealStartTime() != null) {
                        return AttKeyValueVO.builder().id(dto.getClassesId()).name(dto.getClassesName() + " 班").build();
                    }
                    return AttKeyValueVO.builder().id(-1L).name("休息").build();
                }).toList());
            }
        } else {
            List<AttSchedulingClassesDTO> schedulingClassesDTOList = attSchedulingQueryService.getStaffDayScheduling(staffId, day);
            if (schedulingClassesDTOList == null || schedulingClassesDTOList.isEmpty()) {
                result.add(AttKeyValueVO.builder().id(-1L).name("休息").build());
            } else {
                result.addAll(schedulingClassesDTOList.stream().map(dto -> {
                    if (dto != null && dto.getStartWorkTime() != null) {
                        return AttKeyValueVO.builder().id(dto.getId()).name(dto.getName() + " 班").build();
                    }
                    return AttKeyValueVO.builder().id(-1L).name("休息").build();
                }).toList());
            }
        }

        return result;
    }

    /**
     * 校验换班是否符合
     *
     * @param param 换班对象
     *
     * @return 是否符合
     */
    public boolean classesChangeCheck(AttExchangeCheckDTO param) {
        Long fromId = param.getApplicantId();
        Long toId = param.getChangerId();
        Long fromClassId = param.getFromClassesId();
        Long toClassId = param.getToClassesId();
        if (fromId == null || toId == null) {
            throw new ServiceException("换班人员不能为空！");
        }
        UserDTO fromUser = userClient.getUser(fromId);
        UserDTO toUser = userClient.getUser(toId);

        if (AttArrangeTypeEnum.DUTY.name().equals(param.getWorkType())) {
            /* 1.获取交换班次 */
            AttDutyPO fromDuty = null;
            AttDutyPO toDuty = null;
            if (fromClassId != null) {
                List<AttDutyPO> fromDutyTempList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                                    .eq(AttDutyPO::getDutyDate, DateUtils.formatDate(param.getFromDate()))
                                                                                    .eq(AttDutyPO::getStaffId, fromId)
                                                                                    .eq(AttDutyPO::getClassesId, fromClassId)

                                                                           );
                if (fromDutyTempList != null && !fromDutyTempList.isEmpty()) {
                    fromDuty = fromDutyTempList.getFirst();
                }
            }

            if (toClassId != null) {
                List<AttDutyPO> toDutyTempList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                                  .eq(AttDutyPO::getDutyDate, DateUtils.formatDate(param.getToDate()))
                                                                                  .eq(AttDutyPO::getStaffId, toId)
                                                                                  .eq(AttDutyPO::getClassesId, toClassId)

                                                                         );
                if (toDutyTempList != null && !toDutyTempList.isEmpty()) {
                    toDuty = toDutyTempList.getFirst();
                }
            }
            return this.dutyChangeCheck(param, fromUser, toUser, fromDuty, toDuty);
        } else {
            // 1.获取交换班次
            AttSchedulingPO fromScheduling = null;
            AttSchedulingPO toScheduling = null;
            if (fromClassId != null) {
                List<AttSchedulingPO> fromDaySchedulingList = attSchedulingQueryService.getStaffDaySchedulingPO(fromId, DateUtils.formatDate(param.getFromDate()));
                if (fromDaySchedulingList != null && !fromDaySchedulingList.isEmpty()) {
                    for (AttSchedulingPO fromDayScheduling : fromDaySchedulingList) {
                        if (fromClassId.equals(fromDayScheduling.getClassesId())) {
                            fromScheduling = fromDayScheduling;
                            break;
                        }
                    }
                }
            }
            if (toClassId != null) {
                List<AttSchedulingPO> toDaySchedulingList = attSchedulingQueryService.getStaffDaySchedulingPO(toId, DateUtils.formatDate(param.getToDate()));
                if (toDaySchedulingList != null && !toDaySchedulingList.isEmpty()) {
                    for (AttSchedulingPO po : toDaySchedulingList) {
                        if (toClassId.equals(po.getClassesId())) {
                            toScheduling = po;
                            break;
                        }
                    }
                }
            }
            return this.arrangeChangeCheck(param, fromUser, toUser, fromScheduling, toScheduling);
        }
    }

    /**
     * 校验值班
     *
     * @param param
     *
     * @return
     */
    public boolean dutyChangeCheck(AttExchangeCheckDTO param, UserDTO fromUser, UserDTO toUser, AttDutyPO fromDuty, AttDutyPO toDuty) {
        Long fromId = param.getApplicantId();
        Long toId = param.getChangerId();
        String fromMonth = DateUtils.format(param.getFromDate(), "yyyy-MM");
        String toMonth = DateUtils.format(param.getToDate(), "yyyy-MM");
        // 申请人在被换日期所在月的值班
        List<AttDutyPO> fromToMonthDutyList = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                               .eq(AttDutyPO::getStaffId, fromId)
                                                                               .eq(AttDutyPO::getDutyMonth, toMonth)
                                                                      );
        if (fromToMonthDutyList == null || fromToMonthDutyList.isEmpty()) {
            fromToMonthDutyList = new ArrayList<>();
        }
        // 被换人在申请当天所在月份的排班（因为是自己和自己换所以是自己在被换日期所在月份的排班
        List<AttDutyPO> toFromMonthDutyList = new ArrayList<>();
        boolean sameMonth = fromMonth.equals(toMonth);
        if (!Objects.equals(fromId, toId) || !sameMonth) {
            // 和别人换 或者和自己的不同月份换 都要校验另一个月的班
            List<AttDutyPO> list = attDutyMapper.selectList(Wrappers.<AttDutyPO>lambdaQuery()
                                                                    .eq(AttDutyPO::getStaffId, toId)
                                                                    .eq(AttDutyPO::getDutyMonth, fromMonth)
                                                           );
            if (list != null && !list.isEmpty()) {
                toFromMonthDutyList = list;
            }

        }

        AttDutyPO fromAddDuty = null;
        AttDutyPO toAddDuty = null;

        if (Objects.equals(fromId, toId)) {
            // 自己和自己换（班次交换
            if (param.getFromClassesId() == null && param.getToClassesId() == null) {
                throw new ServiceException("同为休息无需交换");
            }
            if (param.getFromDate().equals(param.getToDate())) {
                throw new ServiceException("同一天班无需交换");
            }

            // 组装交换班次
            Map<LocalDateTime, AttDutyPO> changeMap = AttHandlerDomain.handleDutyChang(fromDuty, toDuty, param.getFromDate(), param.getToDate());
            fromAddDuty = changeMap.get(param.getFromDate());
            toAddDuty = changeMap.get(param.getToDate());
            // 自己和自己换如果是同一个月才需要移除用来交换的班次（这里是校验，可以不用管被移除的，移除班次一定能通过校验
            if (sameMonth) {
                // 移除用来交换的班次
                ListIterator<AttDutyPO> fromIterator = fromToMonthDutyList.listIterator();
                while (fromIterator.hasNext()) {
                    AttDutyPO po = fromIterator.next();
                    if (po == null || po.getClassesId() == null || po.getClassesRealStartTime() == null) {
                        fromIterator.remove();
                        continue;
                    }
                    if (param.getFromDate().equals(po.getDutyDate()) && po.getClassesId().equals(param.getFromClassesId())) {
                        fromIterator.remove();
                    }
                    if (param.getToDate().equals(po.getDutyDate()) && po.getClassesId().equals(param.getToClassesId())) {
                        fromIterator.remove();
                    }
                }
            }

            if (fromAddDuty != null) {
                fromToMonthDutyList.add(fromAddDuty);
            }

            if (toAddDuty != null) {
                if (sameMonth) { /* 如果是同一个月就都添加到申请月份中一起校验。不同月份就添加到各自月份中分别校验 */
                    fromToMonthDutyList.add(toAddDuty);
                } else {
                    toFromMonthDutyList.add(toAddDuty);
                }

            }

            /* 校验 */
            this.dutyCheck(fromToMonthDutyList, fromUser.getName());

            if (!sameMonth) { // 如果是不同月份的，还要校验另一个月
                /* 校验 */
                this.dutyCheck(toFromMonthDutyList, fromUser.getName());
            }

        } else {
            /* 自己和别人换 */
            if (param.getFromDate().equals(param.getToDate())) {
                // 如果是相同日期交换需要移除原本的班次
                ListIterator<AttDutyPO> fromIter = fromToMonthDutyList.listIterator();
                while (fromIter.hasNext()) {
                    AttDutyPO fromTemp = fromIter.next();
                    if (fromTemp == null || fromTemp.getClassesId() == null || fromTemp.getClassesId().equals(param.getFromClassesId())) {
                        fromIter.remove();
                        break;
                    }
                }
                ListIterator<AttDutyPO> toIter = toFromMonthDutyList.listIterator();
                while (toIter.hasNext()) {
                    AttDutyPO toTemp = toIter.next();
                    if (toTemp == null || toTemp.getClassesId() == null || toTemp.getClassesId().equals(param.getToClassesId())) {
                        toIter.remove();
                        break;
                    }
                }
            }
            if (toDuty != null) {
                fromAddDuty = AttDutyPO.builder().build();
                BeanUtils.copyProperties(toDuty, fromAddDuty);
                fromAddDuty.setStaffId(fromId);
                fromToMonthDutyList.add(fromAddDuty);
            }
            if (fromDuty != null) {
                toAddDuty = AttDutyPO.builder().build();
                BeanUtils.copyProperties(fromDuty, toAddDuty);
                toAddDuty.setStaffId(toId);
                toFromMonthDutyList.add(toAddDuty);
            }

            this.dutyCheck(fromToMonthDutyList, fromUser.getName());
            this.dutyCheck(toFromMonthDutyList, toUser.getName());
        }

        return true;
    }

    /**
     * 校验排班
     *
     * @param param
     *
     * @return
     */
    public boolean arrangeChangeCheck(AttExchangeCheckDTO param, UserDTO fromUser, UserDTO toUser, AttSchedulingPO fromScheduling,
                                      AttSchedulingPO toScheduling
                                     ) {
        Long fromId = param.getApplicantId();
        Long toId = param.getChangerId();
        String fromMonth = DateUtils.format(param.getFromDate(), "yyyy-MM");
        String toMonth = DateUtils.format(param.getToDate(), "yyyy-MM");
        if (fromScheduling == null && toScheduling == null) {
            throw new ServiceException("同为休息无需交换");
        }
        if (Objects.equals(fromId, toId)) {
            /* 自己和自己换*/
            if (param.getFromDate().equals(param.getToDate())) {
                throw new ServiceException("同一天班无需交换");
            }

            AttGroupPO groupPO = attGroupRepository.getGroupByStaffId(fromId);
            AttSchedulingRulePO rulePO = null;
            if (groupPO != null) {
                rulePO = attSchedulingRuleMapper.selectById(groupPO.getSchedulingRuleId());
            }
            boolean sameMonth = fromMonth.equals(toMonth);

            // 申请人在被换日期所在月的排班
            List<AttSchedulingPO> fromToMonthschedulingList = attSchedulingQueryService.getMonthScheduling(fromId, toMonth, groupPO, rulePO);
            if (fromToMonthschedulingList == null) {
                fromToMonthschedulingList = new ArrayList<>();
            }

            if (sameMonth) {
                // 移除用来交换的班次
                ListIterator<AttSchedulingPO> fromIterator = fromToMonthschedulingList.listIterator();
                while (fromIterator.hasNext()) {
                    AttSchedulingPO po = fromIterator.next();
                    if (po == null || po.getClassesId() == null || po.getClassesRealStartTime() == null) {
                        fromIterator.remove();
                        continue;
                    }
                    if (param.getFromClassesId() != null && param.getFromDate().equals(po.getSchedulingDate()) &&
                        po.getClassesId().equals(param.getFromClassesId())) {
                        fromIterator.remove();
                    }
                    if (param.getToClassesId() != null && param.getToDate().equals(po.getSchedulingDate()) &&
                        po.getClassesId().equals(param.getToClassesId())) {
                        fromIterator.remove();
                    }
                }
            }

            // 组装交换班次
            Map<LocalDateTime, AttSchedulingPO> changeMap = AttHandlerDomain.handleSchedulingChang(fromScheduling, toScheduling, param.getFromDate(), param.getToDate());
            AttSchedulingPO fromAddScheduling = changeMap.get(param.getFromDate());
            AttSchedulingPO toAddScheduling = changeMap.get(param.getToDate());
            // 添加交换后的班次
            if (fromAddScheduling != null) {
                fromToMonthschedulingList.add(fromAddScheduling);
            }
            if (sameMonth && toAddScheduling != null) {
                fromToMonthschedulingList.add(toAddScheduling);
            }
            /* 校验 */
            if (rulePO != null && AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
                attSchedulingQueryService.shiftsHandSchedulingCheck(fromUser.getName(), rulePO, fromToMonthschedulingList);
            } else {
                attSchedulingQueryService.fixedHandSchedulingCheck(fromUser.getName(), fromToMonthschedulingList);
            }

            if (!sameMonth) { // 如果是不同月份的，还要校验另一个月
                // 被换人在申请当天所在月份的排班（因为是自己和自己换所以是自己在被换日期所在月份的排班
                List<AttSchedulingPO> toFromMonthschedulingList = attSchedulingQueryService.getMonthScheduling(toId, fromMonth, groupPO, rulePO);
                if (toFromMonthschedulingList == null) {
                    toFromMonthschedulingList = new ArrayList<>();
                }
                ListIterator<AttSchedulingPO> toIterator = toFromMonthschedulingList.listIterator();
                while (toIterator.hasNext()) {
                    AttSchedulingPO po = toIterator.next();
                    if (po == null || po.getClassesId() == null || po.getClassesRealStartTime() == null) {
                        toIterator.remove();
                        continue;
                    }
                    if (param.getToDate().equals(po.getSchedulingDate()) && param.getToClassesId().equals(po.getClassesId())) {
                        toIterator.remove();
                    }
                }
                // 添加交换班次
                if (toAddScheduling != null) {
                    toAddScheduling.setStaffName(toUser.getName());
                    fromToMonthschedulingList.add(toAddScheduling);
                }
                /* 校验 */
                if (rulePO != null && AttSchedulingWaysEnum.SHIFTS.name().equals(rulePO.getWays())) {
                    attSchedulingQueryService.shiftsHandSchedulingCheck(fromUser.getName(), rulePO, toFromMonthschedulingList);
                } else {
                    attSchedulingQueryService.fixedHandSchedulingCheck(fromUser.getName(), toFromMonthschedulingList);
                }
            }

        } else {
            /* 自己和别人换 */
            // 申请人考勤组、规则
            AttGroupPO fromGroupPO = attGroupRepository.getGroupByStaffId(fromId);
            AttSchedulingRulePO fromRulePO = null;
            if (fromGroupPO != null) {
                fromRulePO = attSchedulingRuleMapper.selectById(fromGroupPO.getSchedulingRuleId());
            }
            // 被换人考勤组、规则
            AttGroupPO toGroupPO = attGroupRepository.getGroupByStaffId(toId);
            AttSchedulingRulePO toRulePO = null;
            if (toGroupPO != null) {
                toRulePO = attSchedulingRuleMapper.selectById(toGroupPO.getSchedulingRuleId());
            }

            // 申请人在被换日所在月份的排班
            List<AttSchedulingPO> fromTOMonthSchedulingList = attSchedulingQueryService.getMonthScheduling(fromId, toMonth, fromGroupPO, fromRulePO);
            if (fromTOMonthSchedulingList == null) {
                fromTOMonthSchedulingList = new ArrayList<>();
            }
            // 杯换人在申请日所在月份的排班
            List<AttSchedulingPO> toFromMonthSchedulingList = attSchedulingQueryService.getMonthScheduling(toId, fromMonth, toGroupPO, toRulePO);
            if (toFromMonthSchedulingList == null) {
                toFromMonthSchedulingList = new ArrayList<>();
            }
            if (param.getFromDate().equals(param.getToDate())) {
                // 如果是相同日期交换需要移除原本的班次
                ListIterator<AttSchedulingPO> fromIter = fromTOMonthSchedulingList.listIterator();
                while (fromIter.hasNext()) {
                    AttSchedulingPO fromTemp = fromIter.next();
                    if (fromTemp == null || fromTemp.getClassesId() == null || (fromTemp.getSchedulingDate().equals(param.getFromDate()) &&
                                                                                fromTemp.getClassesId().equals(param.getFromClassesId()))) {
                        fromIter.remove();
                        break;
                    }
                }
                ListIterator<AttSchedulingPO> toIter = toFromMonthSchedulingList.listIterator();
                while (toIter.hasNext()) {
                    AttSchedulingPO toTemp = toIter.next();
                    if (toTemp == null || toTemp.getClassesId() == null ||
                        (param.getToDate().equals(toTemp.getSchedulingDate()) && toTemp.getClassesId().equals(param.getToClassesId()))) {
                        toIter.remove();
                        break;
                    }
                }
            }
            // 在申请人 被换日期 中 加入被换班次(将被换排班中的人员id改为申请人再加入申请人的排班中
            if (toScheduling != null) {
                toScheduling.setStaffId(fromId);
                fromTOMonthSchedulingList.add(toScheduling);
            }
            // 在被换人 申请日期 中 加入申请班次(将申请排班中的人员id改为被换人再加入被换人的排班中
            if (fromScheduling != null) {
                fromScheduling.setStaffId(toId);
                toFromMonthSchedulingList.add(fromScheduling);
            }
            /* 校验 */
            if (fromRulePO != null && AttSchedulingWaysEnum.SHIFTS.name().equals(fromRulePO.getWays())) {
                attSchedulingQueryService.shiftsHandSchedulingCheck(fromUser.getName(), fromRulePO, fromTOMonthSchedulingList);
            } else {
                attSchedulingQueryService.fixedHandSchedulingCheck(fromUser.getName(), fromTOMonthSchedulingList);
            }
            if (toRulePO != null && AttSchedulingWaysEnum.SHIFTS.name().equals(toRulePO.getWays())) {
                attSchedulingQueryService.shiftsHandSchedulingCheck(toUser.getName(), toRulePO, toFromMonthSchedulingList);
            } else {
                attSchedulingQueryService.fixedHandSchedulingCheck(toUser.getName(), toFromMonthSchedulingList);
            }

        }
        return true;
    }

    /**
     * 值班校验交叉
     *
     * @param dutyPOList 排班
     * @param staffName  人员名称
     */
    private void dutyCheck(List<AttDutyPO> dutyPOList, String staffName) {
        if (dutyPOList == null || dutyPOList.isEmpty()) {
            return;
        }
        dutyPOList.sort(Comparator.comparing(AttDutyPO::getClassesRealStartTime));
        for (int i = 0; i < dutyPOList.size() - 1; i++) {
            AttDutyPO poI = dutyPOList.get(i);
            AttDutyPO poNext = dutyPOList.get(i + 1);
            if (poI.getClassesRealEndTime().isAfter(poNext.getClassesRealStartTime())) {
                if (poI.getDutyDate().equals(poNext.getDutyDate())) {
                    throw new ServiceException(staffName + DateUtils.formatDate(poI.getDutyDate()) + "班次有交叉");
                } else {
                    throw new ServiceException(
                            staffName + DateUtils.formatDate(poI.getDutyDate()) + "与" + DateUtils.formatDate(poNext.getDutyDate()) +
                            "班次有交叉");
                }
            }
        }
    }
}
