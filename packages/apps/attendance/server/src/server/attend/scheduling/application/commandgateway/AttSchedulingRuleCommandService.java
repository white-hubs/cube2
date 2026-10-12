package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingRuleSameShiftDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.aggregate.AttSchedulingRulesAggregate;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.domainservice.AttSchedulingRuleClassesDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.repository.AttSchedulingRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingClasses;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttWeekEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRuleClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRuleParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleClassesResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.SchedulingRuleOperationVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 排班规则业务服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingRuleCommandService {

    private final AttSchedulingRuleRepository attSchedulingRuleRepository;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;
    private final AttClassesMapper classesMapper;
    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingRuleQueryService attSchedulingRuleQueryService;

    /**
     * 新增排班规则
     *
     * @param param 排班规则入参对象
     *
     * @return id
     */
    public Long addSchedulingRule(AttSchedulingRuleParam param) {
        param.validBusiness();
        // 校验规则班次是否合格
        this.checkSchedulingRuleClasses(param);
        AttSchedulingRulesAggregate aggregate = new AttSchedulingRulesAggregate();
        BeanUtils.copyProperties(param, aggregate);
        Long id = IdWorker.getId();
        aggregate.setId(id);

        attSchedulingRuleRepository.insertAttSchedulingRule(aggregate);
        attSchedulingRuleRepository.addPOLog(id, param.getName());
        return id;
    }

    /**
     * 修改排班规则
     *
     * @param param 排班规则入参
     *
     * @return id
     */
    public Long editSchedulingRule(AttSchedulingRuleParam param) {
        param.validBusiness();

        // 获取原来的规则
        AttSchedulingRulePO oldRulePO = attSchedulingRuleMapper.selectById(param.getId());
        if (oldRulePO == null) {
            throw new ServiceException("规则不存在,请检查是否已被删除");
        }

        AttSchedulingRulesAggregate aggregate = new AttSchedulingRulesAggregate();
        BeanUtils.copyProperties(param, aggregate);

        // 获取原来的排班规则班次
        List<AttSchedulingRuleClassesResult> ruleClassesResultList = attSchedulingRuleClassesMapper.selectRuleClassesByRuleId(param.getId());
        // 判断和原来的班次是否相同
        boolean listEqualsBool = AttSchedulingRuleClassesDomainService.equalsClassesList(param.getRulesClassesList(), ruleClassesResultList);

        AttSchedulingRulePO newRulePo = AttSchedulingRulePO.builder()
                .id(aggregate.getId())
                .name(aggregate.getName())
                .ways(aggregate.getWays())
                .legalHolidaysRest(aggregate.getLegalHolidaysRest())
                .continuousDays(aggregate.getContinuousDays())
                .intervalHour(aggregate.getIntervalHour())
                .sameShift(aggregate.getSameShift())
                .build();
        AttPoAttributeSet.initUpdateBasePoAttribute(newRulePo);

        // 获取当前登录用户
        Principal principal = SecurityContextHolder.getPrincipal();
        String loginUserId = principal == null ? "" : String.valueOf(principal.getId());
        // 修改规则
        attSchedulingRuleMapper.update(null, Wrappers.<AttSchedulingRulePO>lambdaUpdate().eq(AttSchedulingRulePO::getId, aggregate.getId())
                                               .set(StringUtils.isNotBlank(param.getName()), AttSchedulingRulePO::getName, param.getName())
                                               .set(StringUtils.isNotBlank(param.getWays()), AttSchedulingRulePO::getWays, param.getWays())
                                               .set(AttSchedulingRulePO::getLegalHolidaysRest, param.getLegalHolidaysRest())
                                               .set(AttSchedulingRulePO::getContinuousDays, param.getContinuousDays())
                                               .set(AttSchedulingRulePO::getIntervalHour, param.getIntervalHour())
                                               .set(AttSchedulingRulePO::getSameShift, param.getSameShift())
                                               .set(AttSchedulingRulePO::getUpdateTime, LocalDateTime.now())
                                               .set(AttSchedulingRulePO::getUpdateBy, loginUserId)
                                      );
        // 如果有修改规则的班次，则需要重新添加班次
        if (!listEqualsBool) {
            this.checkSchedulingRuleClasses(param);
            attSchedulingRuleClassesMapper.removeByRuleId(DateUtils.toTimestamp(LocalDateTime.now()), param.getId());
            aggregate.getRulesClassesList().forEach(classes -> {
                AttSchedulingRuleClassesPO classesPO = AttSchedulingRuleClassesPO.builder()
                        .id(classes.getId())
                        .ruleId(aggregate.getId())
                        .week(classes.getWeek())
                        .build();
                AttPoAttributeSet.initAddBasePoAttribute(classesPO);
                attSchedulingRuleClassesMapper.insert(classesPO);
            });
        }
        // 添加操作日志
        this.handleUpdateLog(oldRulePO, ruleClassesResultList, param);
        return param.getId();
    }

    public void handleUpdateLog(AttSchedulingRulePO oldRule, List<AttSchedulingRuleClassesResult> oldClassesList,
                                AttSchedulingRuleParam aggregate
                               ) {
        SchedulingRuleOperationVO oldPo = new SchedulingRuleOperationVO();
        Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
        List<AttClassesPO> classesPOList = classesMapper.selectList(Wrappers.emptyWrapper());
        if (classesPOList != null && !classesPOList.isEmpty()) {
            for (AttClassesPO classesPO : classesPOList) {
                classesPOMap.put(classesPO.getId(), classesPO);
            }
        }
        if (oldClassesList == null) {
            oldClassesList = new ArrayList<>();
        }
        if (oldRule != null) {
            oldPo.setName(oldRule.getName());
            oldPo.setId(oldRule.getId());
            oldPo.setWays(oldRule.getWays());
            if (AttSchedulingWaysEnum.FIXED.name().equals(aggregate.getWays())) {
                oldPo.setLegalHolidaysRest(oldRule.getLegalHolidaysRest());
                List<String> classlist = new ArrayList<>();
                for (AttSchedulingRuleClassesResult ruleClassesPO : oldClassesList) {
                    String str = "";
                    AttClassesPO cp = classesPOMap.get(ruleClassesPO.getId());
                    if (cp != null) {
                        str = ruleClassesPO.getWeek() + ":" + cp.getName() + ":" + cp.getStartWorkTime() + "-" + cp.getEndWorkTime();
                    }
                    classlist.add(str);
                }
                oldPo.setClasses(classlist);
            } else if (AttSchedulingWaysEnum.SHIFTS.name().equals(aggregate.getWays())) {
                oldPo.setContinuousDays(oldRule.getContinuousDays());
                oldPo.setIntervalHour(oldRule.getIntervalHour());
                List<String> sameShiftList = new ArrayList<>();
                String oldSameShift = oldRule.getSameShift();
                List<AttSchedulingRuleSameShiftDTO> oldSameShiftList = new ArrayList<>();
                if (StringUtils.isNotBlank(oldSameShift)) {
                    oldSameShiftList = JsonUtils.deserialize(oldSameShift, new TypeReference<>() {});
                }
                for (AttSchedulingRuleSameShiftDTO sameShiftDTO : oldSameShiftList) {
                    if (sameShiftDTO.getSameShiftNumber() == null || sameShiftDTO.getSameShiftNumber() <= AttConstants.zeroInt ||
                        CollectionUtils.isEmpty(sameShiftDTO.getSameShiftType())) {
                        continue;
                    }
                    List<String> types = sameShiftDTO.getSameShiftType();
                    StringBuilder sb = new StringBuilder();
                    for (String type : types) {
                        sb.append(AttClassesTypeEnum.valueOf(type).getValue()).append(",");
                    }
                    String typeNames = sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
                    String str = "不允许一个月有超过" + sameShiftDTO.getSameShiftNumber() + "个以上的" + typeNames;
                    sameShiftList.add(str);
                }
                oldPo.setSameShiftList(sameShiftList);
            }
        }
        SchedulingRuleOperationVO newPo = new SchedulingRuleOperationVO();
        if (aggregate != null) {
            newPo.setName(aggregate.getName());
            newPo.setId(aggregate.getId());
            newPo.setWays(aggregate.getWays());
            List<AttSchedulingRuleClassesParam> rulesClassesList = aggregate.getRulesClassesList();
            if (rulesClassesList == null) {
                rulesClassesList = new ArrayList<>();
            }
            if (AttSchedulingWaysEnum.FIXED.name().equals(aggregate.getWays())) {
                newPo.setLegalHolidaysRest(aggregate.getLegalHolidaysRest());
                List<String> classList = new ArrayList<>();
                for (AttSchedulingRuleClassesParam ruleClassesParam : rulesClassesList) {
                    String str = "";
                    AttClassesPO cp = classesPOMap.get(ruleClassesParam.getId());
                    if (cp != null) {
                        str = ruleClassesParam.getWeek() + ":" + cp.getName() + ":" + cp.getStartWorkTime() + "-" + cp.getEndWorkTime();
                    }
                    classList.add(str);
                    newPo.setClasses(classList);
                }
            } else if (AttSchedulingWaysEnum.SHIFTS.name().equals(aggregate.getWays())) {
                newPo.setContinuousDays(aggregate.getContinuousDays());
                newPo.setIntervalHour(aggregate.getIntervalHour());

                List<String> sameShiftList = new ArrayList<>();
                String sameShift = aggregate.getSameShift();
                List<AttSchedulingRuleSameShiftDTO> sameShiftDTOList = new ArrayList<>();
                if (StringUtils.isNotBlank(sameShift)) {
                    sameShiftDTOList = JsonUtils.deserialize(sameShift, new TypeReference<>() {});
                }
                for (AttSchedulingRuleSameShiftDTO sameShiftDTO : sameShiftDTOList) {
                    if (sameShiftDTO.getSameShiftNumber() == null || sameShiftDTO.getSameShiftNumber() <= AttConstants.zeroInt ||
                        CollectionUtils.isEmpty(sameShiftDTO.getSameShiftType())) {
                        continue;
                    }
                    List<String> types = sameShiftDTO.getSameShiftType();
                    StringBuilder sb = new StringBuilder();
                    for (String type : types) {
                        sb.append(AttClassesTypeEnum.valueOf(type).getValue()).append(",");
                    }
                    String typeNames = sb.substring(AttConstants.zeroInt, sb.length() - AttConstants.oneInt);
                    String str = "不允许一个月有超过" + sameShiftDTO.getSameShiftNumber() + "个以上的" + typeNames;
                    sameShiftList.add(str);
                }
                newPo.setSameShiftList(sameShiftList);
            }
        }
        attSchedulingRuleRepository.addUpdateRulePOLog(newPo, oldPo);
    }

    /**
     * 删除排班规则
     *
     * @param id 主键
     *
     * @return id
     */
    public Long removerSchedulingRule(Long id) {
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(id);
        if (rulePO == null) {
            throw new ServiceException("规则不存在!");
        }
        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                         .eq(AttGroupPO::getSchedulingRuleId, id)
                                                                );
        if (groupPOList != null && !groupPOList.isEmpty()) {
            StringBuilder errorBuffer = new StringBuilder("考勤组：");
            for (AttGroupPO groupPO : groupPOList) {
                errorBuffer.append(groupPO.getName()).append(",");
            }
            errorBuffer.append("在使用该规则 无法删除！");
            throw new ServiceException(errorBuffer.toString());
        }
        attSchedulingRuleMapper.removeById(DateUtils.toTimestamp(LocalDateTime.now()), id);
        attSchedulingRuleClassesMapper.removeByRuleId(DateUtils.toTimestamp(LocalDateTime.now()), id);
        // 添加操作日志
        attSchedulingRuleRepository.addDeleteRulePOLog(rulePO);
        return id;
    }

    /**
     * 校验班次是否满足规则
     *
     * @param param 整体入参
     */
    private void checkSchedulingRuleClasses(AttSchedulingRuleParam param) {
        if (param == null || param.getRulesClassesList() == null || param.getRulesClassesList().isEmpty()) {
            return;
        }
        // 处理成带日期的排班
        Map<Integer, List<AttSchedulingRuleClassesParam>> classesMap = param.getRulesClassesList().stream()
                .collect(Collectors.groupingBy(cp -> Integer.valueOf(cp.getWeek())));
        var oldDay = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
        List<AttSchedulingClasses> schedulingClassesList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            var week = oldDay.withDayOfMonth(i).getDayOfWeek().getValue();
            List<AttSchedulingRuleClassesParam> ruleClassesParamList = classesMap.get(week);
            if (ruleClassesParamList == null) {
                continue;
            }
            for (AttSchedulingRuleClassesParam ruleClassesParam : ruleClassesParamList) {
                if (ruleClassesParam.getId() != null) {
                    AttClassesPO classesPO = classesMapper.selectById(ruleClassesParam.getId());
                    if (classesPO == null) {
                        continue;
                    }
                    AttSchedulingClasses attSchedulingClasses = attSchedulingRuleQueryService.stringClassToDateClass(i, classesPO);
                    attSchedulingClasses.setWeek(week);
                    schedulingClassesList.add(attSchedulingClasses);
                }
            }
        }
        if (schedulingClassesList.size() <= 1) {
            return;
        }
        if (AttSchedulingWaysEnum.FIXED.name().equals(param.getWays())) {
            this.checkAdministrationSchedulingRule(schedulingClassesList);
        }
    }

    /**
     * 校验行政班
     *
     * @param schedulingClassesList 规则班次
     */
    private void checkAdministrationSchedulingRule(List<AttSchedulingClasses> schedulingClassesList) {
        schedulingClassesList.sort(Comparator.comparing(AttSchedulingClasses::getClassesRealStartTime));
        // 开始校验班次交叉
        for (int i = 0; i < schedulingClassesList.size(); i++) {
            int nextI = i + 1 >= schedulingClassesList.size() ? 0 : i + 1;
            AttSchedulingClasses nowClasses = schedulingClassesList.get(i);
            AttSchedulingClasses nextClasses = schedulingClassesList.get(nextI);
            if (i == schedulingClassesList.size() - 1) {
                nextClasses = this.dateClassesAddDay(nextClasses, 7);
            }
            if (!nowClasses.getClassesRealEndTime().isBefore(nextClasses.getClassesRealStartTime())) {
                String errStr =
                        AttWeekEnum.getWeekName(nowClasses.getWeek()) + "的班次与" + AttWeekEnum.getWeekName(nextClasses.getWeek()) +
                        "的班次有交叉";
                if (Objects.equals(nowClasses.getWeek(), nextClasses.getWeek())) {
                    errStr = AttWeekEnum.getWeekName(nowClasses.getWeek()) + "的班次有交叉";
                }
                throw new ServiceException(errStr);
            }
        }
    }

    private AttSchedulingClasses dateClassesAddDay(AttSchedulingClasses old, int day) {
        AttSchedulingClasses result = new AttSchedulingClasses();
        BeanUtils.copyProperties(old, result);
        result.setClassesBeforeStartTime(DateUtils.offsetDay(result.getClassesBeforeStartTime(), day));
        result.setClassesRealStartTime(DateUtils.offsetDay(result.getClassesRealStartTime(), day));
        result.setClassesAfterStartTime(DateUtils.offsetDay(result.getClassesAfterStartTime(), day));
        result.setClassesBeforeEndTime(DateUtils.offsetDay(result.getClassesBeforeEndTime(), day));
        result.setClassesRealEndTime(DateUtils.offsetDay(result.getClassesRealEndTime(), day));
        result.setClassesAfterEndTime(DateUtils.offsetDay(result.getClassesAfterEndTime(), day));
        return result;
    }
}
