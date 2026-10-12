package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyParticipateConditionResult;
import com.guhecloud.cube.biz.daily.task.domain.DailySuperintendentService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserRulePO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskUserTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateConditionParam;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanSuperintendentService;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskUserRuleMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserRuleService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/17 11:53
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskUserRuleServiceImpl extends ServiceImpl<DailyTaskUserRuleMapper, DailyTaskUserRulePO> implements DailyTaskUserRuleService {
    private final DailySuperintendentService dailySuperintendentService;
    private final DailyTaskUserService dailyTaskUserService;
    private final DailyPlanSuperintendentService dailyPlanSuperintendentService;
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRuleBindUser(Long refId, List<DailyParticipateConditionParam> participateList,
                                 List<DailyTargetStandardResultParam> targetResultList, List<DailyTaskUserPO> dailyTaskUserPOS,
                                 List<DailyPlanSuperintendentPO> dailyPlanSuperintendentPOS
                                ) {
        List<DailyTaskUserRulePO> dailyTaskUserRulePOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(participateList)) {
            for (var conditionParam : participateList) {
                buildRuleAndUerRef(refId, conditionParam, dailyTaskUserRulePOS, dailyTaskUserPOS, dailyPlanSuperintendentPOS);
            }
        }
        if (CollectionUtils.isNotEmpty(targetResultList)) {
            Map<DailyParticipateConditionParam, List<DailyTargetStandardResultParam>> conditionParamListMap = targetResultList
                    .stream()
                    .collect(Collectors.groupingBy(DailyTargetStandardResultParam::getParticipateCondition));
            for (var entry : conditionParamListMap.entrySet()) {
                var conditionParam = entry.getKey();
                // 对象规则关联id
                refId = IdWorker.getId();
                buildRuleAndUerRef(refId, conditionParam, dailyTaskUserRulePOS, dailyTaskUserPOS, dailyPlanSuperintendentPOS);
                //对象存入对应规则id
                for (var targetResultParam : entry.getValue()) {
                    targetResultParam.getParticipateCondition().setRuleId(refId);
                }
            }
        }
        this.saveBatch(dailyTaskUserRulePOS);
    }

    @Override
    public List<DailyParticipateConditionResult> getRuleByRefId(Long refId, DailyDomainTypeEnum typeEnum) {
        Map<Long, List<DailyParticipateConditionResult>> ruleByRefIds = this.getRuleByRefIds(Collections.singleton(refId), typeEnum);

        return ruleByRefIds.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<DailyParticipateConditionResult>> getRuleByRefIds(Set<Long> refIds, DailyDomainTypeEnum typeEnum) {
        if (CollectionUtils.isEmpty(refIds)) {
            return new HashMap<>();
        }
        List<DailyTaskUserRulePO> dailyTaskUserRulePOS = this.lambdaQuery().in(DailyTaskUserRulePO::getRefId, refIds)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskUserRulePOS)) {
            return new HashMap<>();
        }
        Set<Long> ruleIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        for (var dailyTaskUserRulePO : dailyTaskUserRulePOS) {
            ruleIds.add(dailyTaskUserRulePO.getId());
            if (StringUtils.isNotBlank(dailyTaskUserRulePO.getAbnormalUsers())) {
                userIds.addAll(Arrays.stream(dailyTaskUserRulePO.getAbnormalUsers().split(",")).map(Long::valueOf)
                                       .collect(Collectors.toSet()));
            }
        }
        Map<Long, String> abnormalUserMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            abnormalUserMap = Optional.ofNullable(userClient.listEnableUserByIds(userIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        Map<Long, List<DailySuperintendentDTO>> groupByMasterIdMap = getDailySuperintendent(typeEnum, ruleIds);
        Map<Long, List<DailyParticipateConditionResult>> resultMap = new HashMap<>();
        for (DailyTaskUserRulePO dailyTaskUserRulePO : dailyTaskUserRulePOS) {
            DailyParticipateConditionResult result = new DailyParticipateConditionResult();
            result.setRefId(dailyTaskUserRulePO.getRefId());
            result.setRuleId(dailyTaskUserRulePO.getId());
            result.setConditionType(dailyTaskUserRulePO.getRuleType().name());
            result.setParticipateParams(groupByMasterIdMap.get(dailyTaskUserRulePO.getId()));
            if (StringUtils.isNotBlank(dailyTaskUserRulePO.getAbnormalUsers())) {
                Map<Long, String> finalAbnormalUserMap = abnormalUserMap;
                result.setAbnormalUserList(Arrays.stream(dailyTaskUserRulePO.getAbnormalUsers().split(","))
                                                   .map(t -> DailySuperintendentDTO.builder()
                                                           .targetId(Long.valueOf(t))
                                                           .targetName(finalAbnormalUserMap.get(Long.valueOf(t)))
                                                           .build())
                                                   .collect(Collectors.toList()));
            }
            resultMap.computeIfAbsent(dailyTaskUserRulePO.getRefId(), v -> new ArrayList<>())
                    .add(result);
        }
        return resultMap;
    }

    @Override
    public Map<Long, List<DailyParticipateConditionParam>> getRulesForTaskParam(Set<Long> refIds,
                                                                                DailyDomainTypeEnum typeEnum
                                                                               ) {
        List<DailyTaskUserRulePO> dailyTaskUserRulePOS = this.lambdaQuery().in(DailyTaskUserRulePO::getRefId, refIds)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskUserRulePOS)) {
            return new HashMap<>();
        }
        Set<Long> ruleIds = dailyTaskUserRulePOS.stream().map(DailyTaskUserRulePO::getId).collect(Collectors.toSet());
        Map<Long, List<DailySuperintendentDTO>> groupByMasterIdMap = getDailySuperintendent(typeEnum, ruleIds);
        Map<Long, List<DailyParticipateConditionParam>> resultMap = new HashMap<>();
        for (DailyTaskUserRulePO dailyTaskUserRulePO : dailyTaskUserRulePOS) {
            DailyParticipateConditionParam result = new DailyParticipateConditionParam();
            result.setRuleId(dailyTaskUserRulePO.getId());
            result.setConditionType(dailyTaskUserRulePO.getRuleType());
            result.setAbnormalUserIdList(Arrays.stream(dailyTaskUserRulePO.getAbnormalUsers().split(","))
                                                 .map(Long::valueOf).toList());
            if (groupByMasterIdMap.containsKey(dailyTaskUserRulePO.getId())) {
                List<DailyParticipateParam> participateParams = groupByMasterIdMap.get(dailyTaskUserRulePO.getId()).stream()
                        .map(t -> DailyParticipateParam.builder()
                                .targetId(t.getTargetId())
                                .participateType(ConditionTypeEnum.valueOf(t.getParticipateType()))
                                .taskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name())
                                .build()).collect(Collectors.toList());
                result.setParticipateParams(participateParams);
            }

            resultMap.computeIfAbsent(dailyTaskUserRulePO.getRefId(), v -> new ArrayList<>())
                    .add(result);
        }
        return resultMap;
    }

    private Map<Long, List<DailySuperintendentDTO>> getDailySuperintendent(DailyDomainTypeEnum typeEnum, Set<Long> ruleIds) {
        Map<Long, List<DailySuperintendentDTO>> groupByMasterIdMap = new HashMap<>();
        if (DailyDomainTypeEnum.DAILY_PLAN.equals(typeEnum)) {
            List<DailyPlanSuperintendentPO> dailyPlanSuperintendentPOS = dailyPlanSuperintendentService.lambdaQuery()
                    .in(DailyPlanSuperintendentPO::getMasterId, ruleIds)
                    .list();
            List<DailySuperintendentDTO> dailySuperintendentDTOS = dailySuperintendentService.handlerSuperintendent(dailyPlanSuperintendentPOS);
            groupByMasterIdMap = dailySuperintendentDTOS.stream()
                    .collect(Collectors.groupingBy(DailySuperintendentDTO::getMasterId));
        } else if (DailyDomainTypeEnum.DAILY_TASK.equals(typeEnum)) {
            List<DailyTaskUserPO> dailyTaskUserPOS = dailyTaskUserService.lambdaQuery()
                    .in(DailyTaskUserPO::getMasterId, ruleIds)
                    .list();
            List<DailySuperintendentDTO> dailySuperintendentDTOS = dailySuperintendentService.handlerSuperintendent(dailyTaskUserPOS);
            groupByMasterIdMap = dailySuperintendentDTOS.stream()
                    .collect(Collectors.groupingBy(DailySuperintendentDTO::getMasterId));
        }
        return groupByMasterIdMap;
    }

    private void buildRuleAndUerRef(Long refId,
                                    DailyParticipateConditionParam conditionParam,
                                    List<DailyTaskUserRulePO> dailyTaskUserRulePOS,
                                    List<DailyTaskUserPO> dailyTaskUserPOS, List<DailyPlanSuperintendentPO> dailyPlanSuperintendentPOS
                                   ) {
        DailyTaskUserRulePO taskUserRulePO = DailyTaskUserRulePO.builder().id(IdWorker.getId())
                .refId(refId)
                .ruleType(conditionParam.getConditionType())
                .abnormalUsers(StringUtils.join(",", conditionParam.getAbnormalUserIdList()))
                .build();
        dailyTaskUserRulePOS.add(taskUserRulePO);
        if (Objects.nonNull(dailyTaskUserPOS)) {
            dailyTaskUserPOS.addAll(conditionParam.getParticipateParams().stream()
                                            .map(participate -> DailyTaskUserPO.builder().id(IdWorker.getId())
                                                    .masterId(taskUserRulePO.getId())
                                                    .superintendentType(participate.getParticipateType().name())
                                                    .targetId(participate.getTargetId()).build()).collect(Collectors.toList()));
        } else {
            dailyPlanSuperintendentPOS.addAll(conditionParam.getParticipateParams().stream()
                                                      .map(participate -> DailyPlanSuperintendentPO.builder().id(IdWorker.getId())
                                                              .masterId(taskUserRulePO.getId())
                                                              .superintendentType(participate.getParticipateType().name())
                                                              .targetId(participate.getTargetId()).build()).collect(Collectors.toList()));
        }
    }
}
