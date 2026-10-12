package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyParticipateConditionResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.domain.DailyStandardResultService;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetTagClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.TagBusinessResponse;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DailyTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.TagRefListRequest;
import com.guhecloud.cube.biz.daily.task.domain.adapter.service.DailyTargetQueryService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetRefPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetBatchParam;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import com.guhecloud.cube.biz.daily.task.status.bussiness.DailyTaskStateNodes;
import com.guhecloud.cube.biz.daily.task.status.service.BusinessStateMachine;
import com.guhecloud.cube.biz.daily.task.task.convert.DailyTaskTargetConvert;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskTargetMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetItemService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetRefService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserRuleService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.CREATE_TARGET;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.HANDLE_TARGET;

/**
 * 任务对象
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskTargetServiceImpl extends ServiceImpl<DailyTaskTargetMapper, DailyTaskTargetPO> implements DailyTaskTargetService {

    private final DailyTaskTargetItemService dailyTaskTargetItemService;

    private final DailyStandardResultService dailyStandardResultService;

    private final DailyRecordTargetService dailyRecordTargetService;

    private final DailyTargetQueryService dailyTargetQueryService;

    private final BusinessStateMachine businessStateMachine;
    private final DailyTaskTargetRefService dailyTaskTargetRefService;
    private final DailyTaskUserRuleService dailyTaskUserRuleService;
    private final DailyTargetTagClient dailyTargetTagClient;

    @Override
    public List<DailyTaskTargetPO> getTargetByTaskId(Long taskId) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new ArrayList<>();
        }
        return this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                        .collect(Collectors.toList()))
                .list();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchByTaskId(String dailyType, Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap) {
        if (MapUtils.isEmpty(taskTargetMap)) {
            return;
        }
        var taskTargetSaveList = new ArrayList<DailyTaskTargetPO>();
        var taskTargetItemSaveList = new ArrayList<DailyTaskTargetItemPO>();
        Set<Long> standerIds = new HashSet<>();
        Map<String, Set<Long>> targetCategoryMap = new HashMap<>();
        for (var entry : taskTargetMap.entrySet()) {
            for (var target : entry.getValue()) {
                targetCategoryMap.computeIfAbsent(target.getTargetCategory(), k -> new HashSet<>())
                        .add(target.getTargetId());
                if (CollectionUtils.isNotEmpty(target.getStandardList())) {
                    for (var dailyStandardResultDTO : target.getStandardList()) {
                        standerIds.add(dailyStandardResultDTO.getId());
                    }
                }
            }
        }

        Map<Long, DailyTargetDTO> targetResultMap = new HashMap<>();
        targetCategoryMap.forEach((targetCategory, targetIds) -> {
            DailyTargetQueryParam param = new DailyTargetQueryParam();
            param.setTargetIdSet(targetIds);
            param.setTargetCategory(targetCategory);
            targetResultMap.putAll(dailyTargetQueryService.postDailyTargetList(param));
        });

        List<Long> taskTargetIds = new ArrayList<>();
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = new ArrayList<>();
        Map<Long, DailyStandardResultDTO> detailResponseMap = dailyStandardResultService.handlerStandardContent(standerIds);
        for (var entry : taskTargetMap.entrySet()) {
            for (var taskTargetStandard : entry.getValue()) {
                DailyTargetDTO target = targetResultMap.get(taskTargetStandard.getTargetId());
                if (Objects.nonNull(target)) {
                    var taskTarget = DailyTaskTargetPO.builder().id(IdWorker.getId()).taskId(entry.getKey())
                            .targetId(target.getId())
                            .ruleId(taskTargetStandard.getParticipateCondition().getRuleId())
                            .targetCategory(target.getTargetCategory())
                            .targetCode(target.getTargetCode())
                            .targetName(target.getTargetName())
                            .location(target.getLocation())
                            .targetLntAndLat(target.getLntAndLat())
                            .distributed(false)
                            .snapshot(false)
                            .build();
                    taskTargetIds.add(taskTarget.getId());
                    taskTargetSaveList.add(taskTarget);

                    // 任务和对象关联关系
                    var taskTargetRef = DailyTaskTargetRefPO.builder().id(IdWorker.getId())
                            .taskId(entry.getKey()).targetRefId(taskTarget.getId()).build();
                    dailyTaskTargetRefPOS.add(taskTargetRef);
                }
                var standardList = taskTargetStandard.getStandardList();
                for (var standard : standardList) {
                    var standardDetail = detailResponseMap.get(standard.getId());
                    Assert.isParameterError(standardDetail == null, "标准不存在");
                    for (var item : standardDetail.getItemList()) {
                        var targetItem = DailyTaskTargetItemPO.builder().id(IdWorker.getId())
                                .taskId(entry.getKey()).targetCategory(taskTargetStandard.getTargetCategory())
                                .targetId(taskTargetStandard.getTargetId())
                                .itemId(item.getId())
                                .itemName(item.getName())
                                .itemContent(item.getContent())
                                .operationGuide(item.getOperationGuide())
                                .resultJsonString(JsonUtils.serialize(item.getResultList()))
                                .standardId(standard.getId())
                                .standardName(standard.getName()).build();
                        taskTargetItemSaveList.add(targetItem);
                    }
                }
            }
        }
        this.saveBatch(taskTargetSaveList);
        dailyTaskTargetItemService.saveBatch(taskTargetItemSaveList);
        dailyTaskTargetRefService.saveBatch(dailyTaskTargetRefPOS);
        businessStateMachine.pushEvent(taskTargetIds, dailyType, CREATE_TARGET);
    }

    @Override
    public Pagination<DailyTaskTargetResult> getTargetByTaskId(DailyTaskTargetQueryParam param) {
        if (Objects.nonNull(param.getExecuteTime()) || StringUtils.isNotBlank(param.getResult())) {
            var recordTargetPOS = dailyRecordTargetService
                    .lambdaQuery()
                    .select(DailyRecordTargetPO::getTargetId)
                    .between(ObjectUtils.isNotNull(param.getExecuteTime()), DailyRecordTargetPO::getCreateTime,
                             ObjectUtils.ifNull(param.getExecuteTime(), Period::getBegin),
                             ObjectUtils.ifNull(param.getExecuteTime(), Period::getEnd))
                    .eq(Objects.nonNull(param.getResult()), DailyRecordTargetPO::getExecutionResult, param.getResult())
                    .in(DailyRecordTargetPO::getTaskId, param.getTaskIds())
                    .list();
            if (CollectionUtils.isEmpty(recordTargetPOS)) {
                return new Pagination<>();
            }
            param.setTargetIds(recordTargetPOS.stream().map(DailyRecordTargetPO::getTargetId).collect(Collectors.toSet()));
        }
        if (Boolean.TRUE.equals(param.getAllowAssign())) {
            param.setTaskIds(Collections.singleton(param.getTaskId()));
            param.setStatus(DailyTaskStateNodes.TASK_STATE_NODE_WAIT.getStateCode());
        }
        Page<DailyTaskTargetResult> taskTargetResultPage = this.baseMapper.selectDailyTaskTargetPage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(taskTargetResultPage.getRecords())) {
            return new Pagination<>();
        }
        var taskIds = new HashSet<Long>();
        var targetIds = new HashSet<Long>();
        taskTargetResultPage.getRecords().forEach(tt -> {
            taskIds.add(tt.getTaskId());
            targetIds.add(tt.getId());
        });
        DailyRecordTargetBatchParam recordTargetBatchParam = new DailyRecordTargetBatchParam();
        recordTargetBatchParam.setTaskIds(new ArrayList<>(taskIds));
        recordTargetBatchParam.setTargetIds(new ArrayList<>(targetIds));
        var recordDataMap = Optional
                .ofNullable(dailyRecordTargetService.batchRecordTargetErrorList(recordTargetBatchParam))
                .orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(rte -> String.valueOf(rte.getTaskId()) + rte.getTargetId(), Function.identity(), (o, n) -> o));
        for (DailyTaskTargetResult record : taskTargetResultPage.getRecords()) {
            DailyTaskTargetConvert.setRecordResult(record, recordDataMap);
        }
        return PagingUtils.convert(taskTargetResultPage);
    }

    @Override
    public List<DailyStandardResultDTO> getTargetStandardResult(Set<Long> taskIds, Long targetId) {
        List<DailyTaskTargetItemPO> dailyTaskTargetItemPOS = dailyTaskTargetItemService.lambdaQuery()
                .in(DailyTaskTargetItemPO::getTaskId, taskIds)
                .eq(DailyTaskTargetItemPO::getTargetId, targetId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetItemPOS)) {
            return new ArrayList<>();
        }
        Map<Long, List<DailyTaskTargetItemPO>> groupByStandardIdMap = dailyTaskTargetItemPOS.stream()
                .collect(Collectors.groupingBy(DailyTaskTargetItemPO::getStandardId));
        List<DailyStandardResultDTO> resultDTOList = new ArrayList<>();
        for (var entry : groupByStandardIdMap.entrySet()) {
            DailyTaskTargetItemPO first = entry.getValue().getFirst();
            List<DailyItemResultDTO> itemResultDTOS = new ArrayList<>();
            for (var dailyTaskTargetItemPO : entry.getValue()) {
                itemResultDTOS.add(DailyItemResultDTO.builder()
                                           .id(dailyTaskTargetItemPO.getItemId())
                                           .name(dailyTaskTargetItemPO.getItemName())
                                           .content(dailyTaskTargetItemPO.getItemContent())
                                           .operationGuide(dailyTaskTargetItemPO.getOperationGuide())
                                           .resultList(JsonUtils.deserializeToList(dailyTaskTargetItemPO.getResultJsonString(), DailyResultDTO.class))
                                           .build());
            }
            resultDTOList.add(DailyStandardResultDTO.builder()
                                      .id(entry.getKey())
                                      .name(first.getStandardName())
                                      .itemList(itemResultDTOS)
                                      .build());
        }
        return resultDTOList;
    }

    @Override
    public List<DailyPlanTargetResult> getTargetByTaskIdsForApp(DailyTaskTargetQueryParam param) {
        Set<Long> targetIds = dailyTaskTargetRefService.getTargetIds(Collections.singleton(param.getTaskId()));
        if (CollectionUtils.isEmpty(targetIds)) {
            return new ArrayList<>();
        }
        var dailyTaskTargetPOList = this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, targetIds)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetPOList)) {
            return new ArrayList<>();
        }
        Set<Long> ruleIds = new HashSet<>();
        Set<Long> tagIds = new HashSet<>();
        Set<Long> targetIdSet = new HashSet<>();
        for (var dailyTaskTargetPO : dailyTaskTargetPOList) {
            ruleIds.add(dailyTaskTargetPO.getRuleId());
            targetIdSet.add(dailyTaskTargetPO.getTargetId());
            if (StringUtils.isNotBlank(dailyTaskTargetPO.getTagIds())) {
                tagIds.addAll(Arrays.stream(dailyTaskTargetPO.getTagIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
            }

        }
        Map<Long, List<DailyParticipateConditionResult>> ruleByRefIdMap = dailyTaskUserRuleService.getRuleByRefIds(new HashSet<>(ruleIds), DailyDomainTypeEnum.DAILY_TASK);

        var taskTargetResultList = new ArrayList<DailyPlanTargetResult>();

        var targetStandardRelationList = dailyTaskTargetItemService.lambdaQuery()
                .eq(DailyTaskTargetItemPO::getTaskId, param.getTaskId())
                .in(DailyTaskTargetItemPO::getTargetId, targetIdSet)
                .list();
        // 创建标准ID集合和目标ID与标准关系的映射
        var standardIdSet = new HashSet<Long>();
        var targetIdStandardMap = targetStandardRelationList.stream().peek(tsr -> standardIdSet.add(tsr.getStandardId()))
                .collect(Collectors.groupingBy(DailyTaskTargetItemPO::getTargetId));

        var standardDtoMap = dailyStandardResultService.handlerStandardContent(standardIdSet);

        //获取标签信息
        TagRefListRequest request = new TagRefListRequest();
        request.setTagIds(new ArrayList<>(tagIds));
        Map<Long, String> tagMap = dailyTargetTagClient.tagRefList(request)
                .stream().collect(Collectors.toMap(TagBusinessResponse::getTagId, TagBusinessResponse::getTagName));

        dailyTaskTargetPOList.forEach(ptr -> {
            var targetStandardList = targetIdStandardMap.get(ptr.getTargetId());
            var standardDtoList = new ArrayList<DailyStandardResultDTO>();
            Optional.ofNullable(targetStandardList).orElse(new ArrayList<>()).forEach(ts -> {
                if (standardDtoMap.containsKey(ts.getStandardId())) {
                    standardDtoList.add(standardDtoMap.get(ts.getStandardId()));
                }
            });
            List<DailyParticipateConditionResult> dailyParticipateConditionResults = ruleByRefIdMap.get(ptr.getRuleId());
            String tagNames = null;
            if (StringUtils.isNotBlank(ptr.getTagIds())) {
                tagNames = Arrays.stream(ptr.getTagIds().split(","))
                        .map(t -> tagMap.get(Long.valueOf(t)))
                        .collect(Collectors.joining(","));

            }
            var planTargetResult = DailyPlanTargetResult.builder()
                    .refId(param.getTaskId())
                    .targetCategory(ptr.getTargetCategory())
                    .targetId(ptr.getTargetId())
                    .targetName(ptr.getTargetName())
                    .tagIds(ptr.getTagIds())
                    .tagNames(tagNames)
                    .participateCondition(
                            CollectionUtils.isNotEmpty(dailyParticipateConditionResults) ? dailyParticipateConditionResults.getFirst()
                                                                                         : null)
                    .standardList(standardDtoList).build();
            taskTargetResultList.add(planTargetResult);
        });
        return taskTargetResultList;

    }

    @Override
    public Long getCompleteTargetNum(Long id, String dailyType) {
        Set<Long> targetRefIds = getTargetRefIds(id);
        if (targetRefIds == null) {
            return 0L;
        }
        return this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, targetRefIds)
                .eq(DailyTaskTargetPO::getStatus, businessStateMachine.getCompleteState(dailyType, HANDLE_TARGET))
                .count();
    }

    private Set<Long> getTargetRefIds(Long id) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, id)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return null;
        }
        return dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                .collect(Collectors.toSet());
    }

    /**
     * 获取任务下该档位 档主的其他[执行内容相同的]档位
     * 注意不包含自己
     *
     * @param taskId     任务id
     * @param targetId   对象(档位)id
     * @param itemIdList 执行项id集合
     *
     * @return 对象(档位)列表
     */
    @Override
    public List<DailyTargetDTO> getTaskOtherTarget(Long taskId, Long targetId, List<Long> itemIdList) {
        /* 获取对象信息 */
        Set<Long> targetRefIds = getTargetRefIds(taskId);
        var dailyTaskTargetPO = this.lambdaQuery().in(DailyTaskTargetPO::getId, targetRefIds).eq(DailyTaskTargetPO::getTargetId, targetId)
                .last("limit 1").one();
        if (dailyTaskTargetPO == null || !TaskTargetTypeEnum.STALL.name().equals(dailyTaskTargetPO.getTargetCategory())) {
            // 目前只支持处理档位
            return CollectionUtils.emptyList();
        }

        /* 获取相同执行内容的对象id */
        var dailyTaskTargetItemPOList = dailyTaskTargetItemService.lambdaQuery()
                .select(DailyTaskTargetItemPO::getTargetId)
                .eq(DailyTaskTargetItemPO::getTaskId, taskId)
                .eq(DailyTaskTargetItemPO::getTargetCategory, TaskTargetTypeEnum.STALL.name())
                .in(DailyTaskTargetItemPO::getItemId, itemIdList).ne(DailyTaskTargetItemPO::getTargetId, targetId).list();
        if (CollectionUtils.isEmpty(dailyTaskTargetItemPOList)) {
            return CollectionUtils.emptyList();
        }
        var targetIdSet = dailyTaskTargetItemPOList.stream().map(DailyTaskTargetItemPO::getTargetId).collect(Collectors.toSet());

        return dailyTargetQueryService.postStallMasterOthers(targetId, targetIdSet);
    }

    @Override
    public List<Long> getTargetTagIdList(Long taskId) {
        List<DailyTaskTargetPO> dailyTaskTargetPOS = this.lambdaQuery()
                .select(DailyTaskTargetPO::getTagIds)
                .isNotNull(DailyTaskTargetPO::getTagIds)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetPOS)) {
            return new ArrayList<>();
        }
        return dailyTaskTargetPOS.stream().map(t -> Arrays.stream(t.getTagIds().split(",")).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer handleTaskTarget(Long taskId, Long parentId, String dailyType) {

        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return 0;
        }
        Set<Long> targetRefIds = dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                .collect(Collectors.toSet());
        //获取未完成的对象
        List<String> noCompleteStatus = businessStateMachine.getNoFinalStatus(dailyType);
        List<DailyTaskTargetPO> dailyTaskTargetPOList = this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, targetRefIds)
                .in(DailyTaskTargetPO::getStatus, noCompleteStatus)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetPOList)) {
            return 0;
        }
        Set<Long> noCompleteRef = dailyTaskTargetPOList.stream().map(DailyTaskTargetPO::getId)
                .collect(Collectors.toSet());
        //获取除父级以及当前任务之前外关联的任务ID，如果存在，则不进行归还，直接生成快照
        List<DailyTaskTargetRefPO> existOtherTask = dailyTaskTargetRefService.lambdaQuery()
                .in(DailyTaskTargetRefPO::getTargetRefId, noCompleteRef)
                .notIn(DailyTaskTargetRefPO::getTaskId, taskId, parentId)
                .list();
        if (CollectionUtils.isEmpty(existOtherTask)) {
            //归还给父级
            dailyTaskTargetPOList.forEach(t -> {
                t.setTaskId(parentId);
                t.setDistributed(false);
            });
            this.updateBatchById(dailyTaskTargetPOList);
        }
        //生成对应快照
        Map<Long, DailyTaskTargetPO> dailyTaskTargetPOMap = dailyTaskTargetPOList.stream()
                .collect(Collectors.toMap(DailyTaskTargetPO::getId, t -> t));
        for (DailyTaskTargetRefPO dailyTaskTargetRefPO : dailyTaskTargetRefPOS) {
            if (dailyTaskTargetPOMap.containsKey(dailyTaskTargetRefPO.getTargetRefId())) {
                DailyTaskTargetPO dailyTaskTargetPO = dailyTaskTargetPOMap.get(dailyTaskTargetRefPO.getTargetRefId());
                dailyTaskTargetPO.setId(IdWorker.getId());
                dailyTaskTargetPO.setSnapshot(true);
                dailyTaskTargetPO.setDistributed(true);
                dailyTaskTargetRefPO.setTargetRefId(dailyTaskTargetPO.getId());
            }
        }
        this.saveBatch(dailyTaskTargetPOList);
        dailyTaskTargetRefService.updateBatchById(dailyTaskTargetRefPOS);
        return dailyTaskTargetPOList.size();
    }

    @Override
    public List<DailyTaskTargetPO> getNoDistributedTarget(Long taskId, String dailyType) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new ArrayList<>();
        }
        List<String> noCompleteStatus = businessStateMachine.getNoFinalStatus(dailyType);
        return this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                        .collect(Collectors.toList()))
                .eq(DailyTaskTargetPO::getSnapshot, false)
                .eq(DailyTaskTargetPO::getDistributed, false)
                .in(DailyTaskTargetPO::getStatus, noCompleteStatus)
                .list();
    }

    @Override
    public List<DailyTaskTargetPO> getStatusTargetNum(Long taskId, String dailyType, List<String> noCompleteStatus) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new ArrayList<>();
        }
        return this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                        .collect(Collectors.toList()))
                .in(DailyTaskTargetPO::getStatus, noCompleteStatus)
                .eq(DailyTaskTargetPO::getSnapshot, false)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByTaskId(Long taskId) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            dailyTaskTargetRefService.remove(new LambdaQueryWrapper<DailyTaskTargetRefPO>().eq(DailyTaskTargetRefPO::getTaskId, taskId));
            this.removeBatchByIds(dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId).collect(Collectors.toSet()));
        }
    }

    @Override
    public Map<Long, List<DailyTaskTargetPO>> getTargetByTaskIdListGroupByTaskId(Set<Long> taskIdList) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .in(DailyTaskTargetRefPO::getTaskId, taskIdList)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new HashMap<>();
        }
        List<DailyTaskTargetPO> dailyTaskTargetPOS = this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                        .collect(Collectors.toList()))
                .list();
        Map<Long, Set<Long>> groupByTaskIdMap = dailyTaskTargetRefPOS.stream()
                .collect(Collectors.groupingBy(DailyTaskTargetRefPO::getTaskId, Collectors.mapping(DailyTaskTargetRefPO::getTargetRefId, Collectors.toSet())));
        Map<Long, List<DailyTaskTargetPO>> result = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> entry : groupByTaskIdMap.entrySet()) {
            result.put(entry.getKey(), dailyTaskTargetPOS.stream().filter(t -> entry.getValue().contains(t.getId()))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributedTaskTarget(Map<Long, Set<DailyTaskTargetPO>> dailyTaskTargetRefMap, boolean setNull) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = new ArrayList<>();

        if (MapUtils.isEmpty(dailyTaskTargetRefMap)) {
            return;
        }
        Set<Long> targetRefId = dailyTaskTargetRefMap.values().stream().flatMap(Collection::stream)
                .map(DailyTaskTargetPO::getId).collect(Collectors.toSet());
        //对象已关联任务id
        Set<Long> existTaskId = Optional.ofNullable(dailyTaskTargetRefService.lambdaQuery()
                                                            .in(DailyTaskTargetRefPO::getTargetRefId, targetRefId)
                                                            .list()).orElse(new ArrayList<>())
                .stream().map(DailyTaskTargetRefPO::getTaskId).collect(Collectors.toSet());

        for (var entry : dailyTaskTargetRefMap.entrySet()) {
            Set<DailyTaskTargetPO> value = entry.getValue();
            for (DailyTaskTargetPO dailyTaskTargetPO : value) {
                if (setNull) {
                    dailyTaskTargetPO.setTaskId(null);
                    dailyTaskTargetPO.setDistributed(false);
                } else {
                    dailyTaskTargetPO.setTaskId(entry.getKey());
                    dailyTaskTargetPO.setDistributed(true);
                }
                //如果对象已关联任务id，则不添加
                if (!existTaskId.contains(entry.getKey())) {
                    dailyTaskTargetRefPOS.add(DailyTaskTargetRefPO.builder()
                                                      .id(IdWorker.getId())
                                                      .taskId(entry.getKey())
                                                      .targetRefId(dailyTaskTargetPO.getId())
                                                      .build());
                }
            }
        }
        this.updateBatchById(dailyTaskTargetRefMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
        dailyTaskTargetRefService.saveBatch(dailyTaskTargetRefPOS);
    }

    @Override
    public List<Long> getTaskIdByObjectId(Long objectId, String dailyType) {
        List<String> noCompleteStatus = businessStateMachine.getNoFinalStatus(dailyType);
        List<DailyTaskTargetPO> dailyTaskTargetPOS = this.lambdaQuery()
                .eq(DailyTaskTargetPO::getTargetId, objectId)
                .in(DailyTaskTargetPO::getStatus, noCompleteStatus)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetPOS)) {
            return new ArrayList<>();
        }
        List<Long> refIds = dailyTaskTargetPOS.stream().map(DailyTaskTargetPO::getId).collect(Collectors.toList());
        return dailyTaskTargetRefService.lambdaQuery()
                .in(DailyTaskTargetRefPO::getTargetRefId, refIds)
                .select(DailyTaskTargetRefPO::getTaskId)
                .list().stream().map(DailyTaskTargetRefPO::getTaskId).collect(Collectors.toList());
    }

    @Override
    public DailyTaskTargetResult getDailyTaskTargetRelate(Long taskId, Long id) {
        var dailyTaskTargetRefPOS = dailyTaskTargetRefService.lambdaQuery()
                .eq(DailyTaskTargetRefPO::getTaskId, taskId)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new DailyTaskTargetResult();
        }
        var dailyTaskTargetPO = this.lambdaQuery()
                .in(DailyTaskTargetPO::getId, dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId)
                        .collect(Collectors.toList()))
                .eq(DailyTaskTargetPO::getTargetId, id)
                .last("limit 1")
                .one();
        Assert.isNotFound(dailyTaskTargetPO, "任务目标不存在");
        return DailyTaskTargetConvert.convertToResult(dailyTaskTargetPO);
    }
}
