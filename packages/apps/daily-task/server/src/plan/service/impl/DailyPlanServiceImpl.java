package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyParticipateConditionResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyCronMapper;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyTaskCategoryMapper;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyUrgencyService;
import com.guhecloud.cube.biz.daily.task.domain.DailyStandardResultService;
import com.guhecloud.cube.biz.daily.task.domain.DailySuperintendentService;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyTargetTagClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.TagBusinessResponse;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DailyTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.TagRefListRequest;
import com.guhecloud.cube.biz.daily.task.domain.adapter.service.DailyTargetQueryService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanExpandPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskCategoryPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserRulePO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyUrgencyPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyPlanStatusEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanCommandParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanEnableParam;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanPageParam;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyCronUtils;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyModelConverterUtil;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanExpandMapper;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanMapper;
import com.guhecloud.cube.biz.daily.task.plan.dto.DailyPlanGenerateTaskAfterUpdateDTO;
import com.guhecloud.cube.biz.daily.task.plan.result.DailyPlanPageResult;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanStandardRelationService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanSuperintendentService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetRelationService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetStandardRelationService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserRuleService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 计划
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanServiceImpl extends ServiceImpl<DailyPlanMapper, DailyPlanPO> implements DailyPlanService {

    private final DailySuperintendentService dailySuperintendentService;

    private final DailyPlanMapper dailyPlanMapper;
    private final DailyTaskCategoryMapper dailyTaskCategoryMapper;
    private final DailyPlanExpandMapper dailyPlanExpandMapper;
    private final DailyCronMapper dailyCronMapper;

    private final DailyStandardResultService dailyStandardResultService;
    private final DailyPlanSuperintendentService dailyPlanSuperintendentService;
    private final DailyPlanStandardRelationService dailyPlanStandardRelationService;
    private final DailyPlanTargetRelationService dailyPlanTargetRelationService;
    private final DailyPlanTargetStandardRelationService dailyPlanTargetStandardRelationService;
    private final DailyUrgencyService dailyUrgencyService;
    private final DailyItemService dailyItemService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;
    private final DailyTaskUserRuleService dailyTaskUserRuleService;
    private final DailyTargetTagClient dailyTargetTagClient;
    private final DailyTargetQueryService dailyTargetQueryService;

    /**
     * 计划分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<DailyPlanPageResult> pageDailyPlan(DailyPlanPageParam param) {
        var createTime = param.getCreateTime();
        var createTimeBool = createTime != null && createTime.getBegin() != null && createTime.getEnd() != null;

        var startAndEndTime = param.getStartAndEndTime();
        var startAndEndTimeBool = startAndEndTime != null && startAndEndTime.getBegin() != null && startAndEndTime.getEnd() != null;
        // 查询条件
        var page = this.page(PagingUtils.convert(param), Wrappers.<DailyPlanPO>lambdaQuery()
                .eq(DailyPlanPO::getDailyType, param.getDailyType())
                .between(createTimeBool, DailyPlanPO::getCreateTime, ObjectUtils.ifNull(createTime, Period::getBegin), ObjectUtils.ifNull(createTime, Period::getEnd))
                .ge(startAndEndTimeBool, DailyPlanPO::getEndTime, ObjectUtils.ifNull(createTime, Period::getBegin))
                .le(startAndEndTimeBool, DailyPlanPO::getStartTime, ObjectUtils.ifNull(createTime, Period::getEnd))
                .eq(param.getUrgencyId() != null, DailyPlanPO::getUrgencyId, param.getUrgencyId())
                .eq(param.getTaskCategoryId() != null, DailyPlanPO::getTaskCategoryId, param.getTaskCategoryId())
                .eq(StringUtils.isNoneBlank(param.getStatus()), DailyPlanPO::getStatus, param.getStatus())
                .like(StringUtils.isNoneBlank(param.getName()), DailyPlanPO::getName, param.getName())
                .orderByDesc(DailyPlanPO::getCreateTime));
        var pageList = page.getRecords();
        if (CollectionUtils.isEmpty(pageList)) {
            return Pagination.empty();
        }
        /* 获取任务分类 */
        var taskCategoryMap = new HashMap<Long, String>();
        var urgencyMap = new HashMap<Long, String>();
        var taskCategoryIdSet = new HashSet<Long>();
        var planIdSet = new HashSet<Long>();
        var urgencyIdSet = new HashSet<Long>();
        var createBySet = new HashSet<String>();
        pageList.forEach(po -> {
            taskCategoryIdSet.add(po.getTaskCategoryId());
            planIdSet.add(po.getId());
            urgencyIdSet.add(po.getUrgencyId());
            createBySet.add(po.getCreateBy());
        });
        if (CollectionUtils.isNotEmpty(taskCategoryIdSet)) {
            var taskCategoryList = dailyTaskCategoryMapper.selectList(Wrappers.<DailyTaskCategoryPO>lambdaQuery()
                                                                              .in(DailyTaskCategoryPO::getId, taskCategoryIdSet));
            Optional.ofNullable(taskCategoryList).orElse(new ArrayList<>())
                    .forEach(tc -> taskCategoryMap.put(tc.getId(), tc.getName()));
        }
        /* 获取紧急程度 */
        if (CollectionUtils.isNotEmpty(urgencyIdSet)) {
            var urgencyList = dailyUrgencyService.lambdaQuery().in(DailyUrgencyPO::getId, urgencyIdSet).list();
            Optional.ofNullable(urgencyList).orElse(new ArrayList<>()).forEach(tc -> urgencyMap.put(tc.getId(), tc.getName()));
        }
        // 负责人信息
        Map<Long, List<DailySuperintendentDTO>> planSuperintendentMap = dailyPlanSuperintendentService.getPlanSuperintendentByIds(planIdSet);
        /* 任务对象数 */
        var dailyTargetList = dailyPlanTargetRelationService.lambdaQuery().in(DailyPlanTargetRelationPO::getPlanId, planIdSet).list();
        var planTargetMap = Optional.ofNullable(dailyTargetList).orElse(new ArrayList<>()).stream()
                .collect(Collectors.groupingBy(DailyPlanTargetRelationPO::getPlanId));
        /* 创建人信息 */
        var userMap = dailySuperintendentService.getUserNameByAccounts(createBySet);

        /* 组装返回对象 */
        var dtoList = pageList.stream()
                .map(po -> DailyPlanPageResult.builder().id(po.getId()).dailyType(po.getDailyType()).planName(po.getName())
                        .taskCategoryId(po.getTaskCategoryId()).taskCategoryName(taskCategoryMap.get(po.getTaskCategoryId()))
                        .urgencyId(po.getUrgencyId()).urgencyName(urgencyMap.get(po.getUrgencyId())).enable(po.getEnable())
                        .createTime(po.getCreateTime()).superintendentList(planSuperintendentMap.get(po.getId()))
                        .executionCount(po.getExecutionCount()).startTime(po.getStartTime()).endTime(po.getEndTime()).status(po.getStatus())
                        .targetCount(planTargetMap.get(po.getId()) == null ? null : planTargetMap.get(po.getId()).size())
                        .createByName(userMap.get(po.getCreateBy()) == null ? null : userMap.get(po.getCreateBy()).getName())
                        .build()
                    ).collect(Collectors.toList());
        return PagingUtils.convert(page, dtoList);
    }

    /**
     * 获取指定ID的日程计划详情。
     *
     * <p>该方法会查询主表信息，并关联查询扩展信息、附件列表、标准执行项、负责人以及目标对象等详细数据。</p>
     *
     * @param id 日程计划的主键ID，不能为空
     *
     * @return 返回完整的日程计划详情结果对象 {@link DailyPlanDetailResult}
     */
    @Override
    public DailyPlanDetailResult detailDailyPlan(Long id) {
        var po = this.getById(id);
        Assert.isNotFound(po);
        var result = DailyModelConverterUtil.dailyPlanPoToResult(po);

        /* 获取紧急程度 */
        if (po.getUrgencyId() != null) {
            var urgency = dailyUrgencyService.getById(po.getUrgencyId());
            result.setUrgencyName(Optional.ofNullable(urgency).orElse(new DailyUrgencyPO()).getName());
        }

        /* 任务类型 */
        if (po.getTaskCategoryId() != null) {
            var taskCategory = dailyTaskCategoryMapper.selectById(po.getTaskCategoryId());
            result.setTaskCategoryName(Optional.ofNullable(taskCategory).orElse(new DailyTaskCategoryPO()).getName());
        }
        /* 计划周期 */
        if (po.getCronId() != null) {
            var cron = dailyCronMapper.selectById(po.getCronId());
            result.setCronName(Optional.ofNullable(cron).orElse(new DailyCronPO()).getName());
        }

        /* 查询并设置计划的扩展内容（metadata） */
        var planExpand = dailyPlanExpandMapper.selectOne(Wrappers.<DailyPlanExpandPO>lambdaQuery().eq(DailyPlanExpandPO::getId, id)
                                                                 .last("limit 1"));
        if (planExpand != null) {
            result.setMetadata(planExpand.getMetadata());
        }

        /* 查询并设置与当前计划相关的文件列表 */
        if (result.getFileRefKey() != null) {
            result.setFileList(fileClient.getByBusinessKey(result.getFileRefKey()));
        }

        /* 查询并处理计划的标准执行内容 */
        var planStandardRelationList = dailyPlanStandardRelationService.list(Wrappers.<DailyPlanStandardRelationPO>lambdaQuery()
                                                                                     .eq(DailyPlanStandardRelationPO::getPlanId, id));
        if (CollectionUtils.isNotEmpty(planStandardRelationList)) {
            Map<Long, DailyItemResultDTO> itemResultDTOMap = dailyItemService.getItemInfoList(planStandardRelationList.stream()
                                                                                                      .map(DailyPlanStandardRelationPO::getItemId)
                                                                                                      .collect(Collectors.toSet()));
            result.setTaskItemList(new ArrayList<>(itemResultDTOMap.values()));
        }


        /* 查询并构建计划的目标对象及其对应的执行标准内容 */
        var planTargetRelationList = dailyPlanTargetRelationService.list(Wrappers.<DailyPlanTargetRelationPO>lambdaQuery()
                                                                                 .eq(DailyPlanTargetRelationPO::getPlanId, id));
        //对象类型计划
        boolean targetType = false;
        if (CollectionUtils.isNotEmpty(planTargetRelationList)) {
            // 创建计划目标结果列表
            var planTargetResultList = new ArrayList<DailyPlanTargetResult>();
            Map<Long, Long> targetRuleMap = new HashMap<>();
            Map<String, Set<Long>> targetCategoryMap = new HashMap<>();
            Set<Long> tagIds = new HashSet<>();
            for (var targetRelationPO : planTargetRelationList) {
                targetRuleMap.put(targetRelationPO.getTargetId(), targetRelationPO.getRuleId());
                targetCategoryMap.computeIfAbsent(targetRelationPO.getTargetCategory(), k -> new HashSet<>())
                        .add(targetRelationPO.getTargetId());
                if (StringUtils.isNotBlank(targetRelationPO.getTagIds())) {
                    tagIds.addAll(Arrays.stream(targetRelationPO.getTagIds().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
                }
            }
            // 查询计划目标标准关联关系列表
            var targetStandardRelationList = dailyPlanTargetStandardRelationService.list(Wrappers
                                                                                                 .<DailyPlanTargetStandardRelationPO>lambdaQuery()
                                                                                                 .eq(DailyPlanTargetStandardRelationPO::getPlanId, id)
                                                                                                 .in(DailyPlanTargetStandardRelationPO::getTargetId, targetRuleMap.keySet()));
            // 创建标准ID集合和目标ID与标准关系的映射
            var standardIdSet = new HashSet<Long>();
            var targetIdStandardMap = targetStandardRelationList.stream().peek(tsr -> standardIdSet.add(tsr.getStandardId()))
                    .collect(Collectors.groupingBy(DailyPlanTargetStandardRelationPO::getTargetId));
            // 处理标准内容，获取标准DTO映射
            var standardDtoMap = dailyStandardResultService.handlerStandardContent(standardIdSet);
            // 获取对象及其规则关联信息
            Map<Long, List<DailyParticipateConditionResult>> ruleByRefIdMap = dailyTaskUserRuleService.getRuleByRefIds(new HashSet<>(targetRuleMap.values()), DailyDomainTypeEnum.DAILY_PLAN);

            //获取对象信息
            Map<Long, DailyTargetDTO> targetResultMap = new HashMap<>();
            targetCategoryMap.forEach((targetCategory, targetIds) -> {
                DailyTargetQueryParam param = new DailyTargetQueryParam();
                param.setTargetIdSet(targetIds);
                param.setTargetCategory(targetCategory);
                targetResultMap.putAll(dailyTargetQueryService.postDailyTargetList(param));
            });
            TagRefListRequest request = new TagRefListRequest();
            request.setTagIds(new ArrayList<>(tagIds));
            Map<Long, String> tagMap = dailyTargetTagClient.tagRefList(request)
                    .stream().collect(Collectors.toMap(TagBusinessResponse::getTagId, TagBusinessResponse::getTagName));
            // 遍历计划目标关联关系，构建计划目标结果列表
            planTargetRelationList.forEach(ptr -> {
                var targetStandardList = targetIdStandardMap.get(ptr.getTargetId());
                var standardDtoList = new ArrayList<DailyStandardResultDTO>();
                // 根据目标标准关系获取对应的标准DTO列表
                Optional.ofNullable(targetStandardList).orElse(new ArrayList<>()).forEach(ts -> {
                    if (standardDtoMap.containsKey(ts.getStandardId())) {
                        standardDtoList.add(standardDtoMap.get(ts.getStandardId()));
                    }
                });
                // 构建计划目标结果对象并添加到结果列表中
                List<DailyParticipateConditionResult> dailyParticipateConditionResults = new ArrayList<>();
                if (targetRuleMap.containsKey(ptr.getTargetId())) {
                    dailyParticipateConditionResults = ruleByRefIdMap.get(targetRuleMap.get(ptr.getTargetId()));
                }
                var tagTargetInfo = targetResultMap.getOrDefault(ptr.getTargetId(), new DailyTargetDTO());
                String tagNames = null;
                if (StringUtils.isNotBlank(ptr.getTagIds())) {
                    tagNames = Arrays.stream(ptr.getTagIds().split(","))
                            .filter(t -> tagMap.containsKey(Long.valueOf(t)))
                            .map(t -> tagMap.get(Long.valueOf(t)))
                            .collect(Collectors.joining(","));

                }
                var planTargetResult = DailyPlanTargetResult.builder().refId(id).targetCategory(ptr.getTargetCategory())
                        .targetId(ptr.getTargetId())
                        .targetName(tagTargetInfo.getTargetName())
                        .tagIds(ptr.getTagIds())
                        .tagNames(tagNames)
                        .participateCondition(
                                CollectionUtils.isNotEmpty(dailyParticipateConditionResults) ? dailyParticipateConditionResults.getFirst()
                                                                                             : null)
                        .standardList(standardDtoList).build();
                planTargetResultList.add(planTargetResult);
            });
            // 按目标分类对计划目标结果进行分组，并设置到结果对象中
            result.setTargetTypeResultMap(planTargetResultList.stream()
                                                  .collect(Collectors.groupingBy(DailyPlanTargetResult::getTargetCategory)));
            targetType = true;
        }
        /* 查询并处理计划的负责人信息及类型 */
        if (!targetType) {
            List<DailyParticipateConditionResult> ruleResultList = dailyTaskUserRuleService.getRuleByRefId(id, DailyDomainTypeEnum.DAILY_PLAN);
            result.setParticipateList(ruleResultList);
        }
        /* 创建人信息 */
        var userMap = dailySuperintendentService.getUserNameByAccounts(new HashSet<>(Collections.singletonList(po.getCreateBy())));
        var userDTO = userMap.get(po.getCreateBy());
        result.setCreateByName(userDTO == null ? "" : userDTO.getName());
        return result;
    }

    /**
     * 添加计划
     *
     * @param param 计划入参
     *
     * @return id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addDailyPlan(DailyPlanCommandParam param) {
        Assert.isConflict(this.exists(Wrappers.<DailyPlanPO>lambdaQuery().eq(DailyPlanPO::getName, param.getName())), "计划名称已存在!");

        var po = DailyModelConverterUtil.dailyPlanCommandParamToPo(param);
        po.setExecutionCount(0);
        po.setEnable(true);
        return this.saveOrUpdateHandler(po, param);
    }

    /**
     * 编辑计划
     *
     * @param param 计划入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long editDailyPlan(DailyPlanCommandParam param) {
        var po = this.getById(param.getId());
        Assert.isNotFound(po, "计划不存在");

        po.setName(param.getName());
        po.setTaskCategoryId(param.getTaskCategoryId());
        po.setUrgencyId(param.getUrgencyId());
        po.setStartTime(param.getStartTime());
        po.setEndTime(param.getEndTime());
        po.setCronId(param.getCronId());
        po.setDescription(param.getDescription());
        po.setExecuteWay(param.getExecuteWay());
        po.setNextTime(this.getNextTime(param.getCronId(), param.getSpecifiedType(), param.getSpecifiedDays(), param.getSpecifiedTime(), LocalDateTime.now()));
        po.setSpecifiedType(param.getSpecifiedType());
        po.setSpecifiedDays(param.getSpecifiedDays());
        po.setSpecifiedTime(param.getSpecifiedTime());
        po.setTaskDay(param.getTaskDay());
        po.setTaskHour(param.getTaskHour());
        po.setTaskMinute(param.getTaskMinute());
        po.setMethod(param.getMethod());
        po.setIsOnDuty(param.getIsOnDuty());
        po.setRequireProgress(param.getRequireProgress());
        po.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        po.setUpdateTime(LocalDateTime.now());

        dailyPlanExpandMapper.deleteById(po.getId());
        if (po.getFileRefKey() != null) {
            fileRefClient.removeByBusinessKey(po.getFileRefKey());
        }
        po.setFileRefKey(null);
        Set<Long> refIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(param.getTargetResultList())) {
            refIds.addAll(param.getTargetResultList().stream()
                                  .filter(t -> Objects.nonNull(t.getParticipateCondition().getRuleId()))
                                  .map(t -> t.getParticipateCondition().getRuleId()).collect(Collectors.toSet()));
        }
        Set<Long> existItemIds = dailyPlanStandardRelationService.lambdaQuery()
                .eq(DailyPlanStandardRelationPO::getPlanId, po.getId())
                .list()
                .stream().map(DailyPlanStandardRelationPO::getItemId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(param.getTaskItemIdList())) {
            List<Long> deletedIds = existItemIds.stream().filter(t -> !param.getTaskItemIdList().contains(t))
                    .collect(Collectors.toList());
            dailyItemService.removeTempItem(deletedIds);
        } else {
            dailyItemService.removeTempItem(new ArrayList<>(existItemIds));
        }
        refIds.add(po.getId());
        dailyTaskUserRuleService.remove(Wrappers.<DailyTaskUserRulePO>lambdaQuery().in(DailyTaskUserRulePO::getRefId, refIds));
        dailyPlanSuperintendentService.remove(Wrappers.<DailyPlanSuperintendentPO>lambdaQuery()
                                                      .eq(DailyPlanSuperintendentPO::getMasterId, po.getId()));
        dailyPlanStandardRelationService.remove(Wrappers.<DailyPlanStandardRelationPO>lambdaQuery()
                                                        .eq(DailyPlanStandardRelationPO::getPlanId, po.getId()));
        dailyPlanTargetRelationService.remove(Wrappers.<DailyPlanTargetRelationPO>lambdaQuery()
                                                      .eq(DailyPlanTargetRelationPO::getPlanId, po.getId()));
        dailyPlanTargetStandardRelationService.remove(Wrappers.<DailyPlanTargetStandardRelationPO>lambdaQuery()
                                                              .eq(DailyPlanTargetStandardRelationPO::getPlanId, po.getId()));
        return this.saveOrUpdateHandler(po, param);
    }

    /**
     * 启用或禁用日常计划
     *
     * @param param 包含计划ID和启用状态的参数对象
     *
     * @return 操作结果，true表示操作成功
     */
    @Override
    public Boolean enablePlan(DailyPlanEnableParam param) {
        var po = this.getById(param.getId());
        Assert.isNotFound(po, "计划不存在");
        var enable = Boolean.TRUE.equals(param.getEnable());
        // 更新指定ID的日常计划的启用状态

        this.lambdaUpdate().set(DailyPlanPO::getEnable, param.getEnable())
                .set(DailyPlanPO::getStatus, enable ? DailyPlanStatusEnum.RUNNING.name() : DailyPlanStatusEnum.PAUSED.name())
                .set(DailyPlanPO::getNextTime, enable
                                               ? this.getNextTime(po.getCronId(), po.getSpecifiedType(), po.getSpecifiedDays(), po.getSpecifiedTime(), LocalDateTime.now())
                                               : null)
                .eq(DailyPlanPO::getId, param.getId()).update();

        return true;
    }

    /**
     * 删除计划
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDailyPlan(Long id) {
        var po = this.getById(id);
        if (po == null) {
            return true;
        }
        // 删除计划
        DatabaseContextHolder.softDelete(this, id);
        // 删除扩展信息
        DatabaseContextHolder.softDelete(DailyPlanExpandPO.class, id);
        // 删除计划的执行内容
        var planStandardRelationList = dailyPlanStandardRelationService.lambdaQuery().eq(DailyPlanStandardRelationPO::getPlanId, id).list();
        DatabaseContextHolder.softDelete(dailyPlanStandardRelationService, planStandardRelationList);
        // 删除计划的对象
        var planTargetRelationList = dailyPlanTargetRelationService.lambdaQuery().eq(DailyPlanTargetRelationPO::getPlanId, id).list();
        DatabaseContextHolder.softDelete(dailyPlanTargetRelationService, planTargetRelationList);
        // 删除计划对象的标准内容
        var planTargetStandardRelationList = dailyPlanTargetStandardRelationService.lambdaQuery()
                .eq(DailyPlanTargetStandardRelationPO::getPlanId, id).list();
        DatabaseContextHolder.softDelete(dailyPlanTargetStandardRelationService, planTargetStandardRelationList);
        // 删除文件
        if (po.getFileRefKey() != null && po.getFileRefKey() > 0) {
            fileRefClient.removeByBusinessKey(po.getFileRefKey());
        }
        return true;
    }

    /**
     * 批量更新计划上次执行时间,下次执行时间,执行次数,修改时间
     *
     * @param updates 修改对象集合
     */
    @Override
    public void batchUpdateMultiFields(List<DailyPlanGenerateTaskAfterUpdateDTO> updates) {
        dailyPlanMapper.batchUpdateMultiFields(updates);
    }

    /**
     * 获取下次执行时间
     *
     * @param cornId        表达式对象id
     * @param specifiedType 循环类型
     * @param specifiedDays 循环日期
     * @param specifiedTime 循环时间
     * @param now           当前时间
     *
     * @return 下次执行时间
     */
    private LocalDateTime getNextTime(Long cornId, String specifiedType, String specifiedDays, String specifiedTime,
                                      LocalDateTime now
                                     ) {
        String cron;
        if (cornId != null && cornId > 0) {
            var cronPO = dailyCronMapper.selectById(cornId);
            cron = Optional.ofNullable(cronPO).orElse(new DailyCronPO()).getCron();
        } else {
            cron = DailyCronUtils.handlerCorn(specifiedType, specifiedDays, specifiedTime);
        }
        LocalDateTime nextTime = null;
        try {
            CronExpression cronExpression = new CronExpression(cron);
            nextTime = cronExpression.getNextValidTimeAfter(now);
        } catch (ParseException e) {
            log.error("日常任务, 计划生成任务，cron表达式解析异常：", e);
        }
        return nextTime;
    }

    /**
     * 计划新增或编辑处理
     *
     * @param po    计划
     * @param param 入参
     *
     * @return 计划id
     */
    private Long saveOrUpdateHandler(DailyPlanPO po, DailyPlanCommandParam param) {
        var planId = po.getId() == null ? IdWorker.getId() : po.getId();
        if (CollectionUtils.isNotEmpty(param.getNoExistItemList())) {
            List<Long> taskItemIds = new ArrayList<>();
            param.getNoExistItemList().forEach(t -> {
                t.setIsTemp(!param.getIsSaveTempItem());
                t.setId(IdWorker.getId());
                t.setDailyType(po.getDailyType());
                taskItemIds.add(t.getId());
            });
            param.setTaskItemIdList(taskItemIds);
            dailyItemService.saveBatch(param.getNoExistItemList());
        }

        /* 下次执行时间 */
        po.setNextTime(this.getNextTime(param.getCronId(), param.getSpecifiedType(), param.getSpecifiedDays(), param.getSpecifiedTime(), LocalDateTime.now()));
        /* 文件 */
        FileRefCreateParam fileRefCreateParam = null;
        if (CollectionUtils.isNotEmpty(param.getFileIds())) {
            var fileRefKey = IdWorker.getId();
            po.setFileRefKey(fileRefKey);
            fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setSceneCode(DailyConstants.DAILY_TASK_CREATE_FILE);
            fileRefCreateParam.setFileIdList(param.getFileIds());
            fileRefCreateParam.setBusinessKey(fileRefKey);
        }
        /* 人员 */
        List<DailyPlanSuperintendentPO> dailyBaseSuperintendents = new ArrayList<>();
        dailyTaskUserRuleService.saveRuleBindUser(planId, param.getParticipateList(), param.getTargetResultList(), null, dailyBaseSuperintendents);
        /* 计划的标准 */
        List<DailyPlanStandardRelationPO> planStandardList = null;
        if (CollectionUtils.isNotEmpty(param.getTaskItemIdList())) {
            planStandardList = param.getTaskItemIdList().stream()
                    .map(sl -> DailyPlanStandardRelationPO.builder().id(IdWorker.getId()).planId(planId).itemId(sl).build())
                    .collect(Collectors.toList());
        }
        /* 计划的对象和对象的标准*/
        var planTargetRelationList = new ArrayList<DailyPlanTargetRelationPO>();
        var targetStandardRelationList = new ArrayList<DailyPlanTargetStandardRelationPO>();
        if (CollectionUtils.isNotEmpty(param.getTargetResultList())) {
            param.getTargetResultList().forEach(tr -> {
                planTargetRelationList.add(DailyPlanTargetRelationPO.builder().id(IdWorker.getId()).planId(planId)
                                                   .targetCategory(tr.getTargetCategory())
                                                   .ruleId(tr.getParticipateCondition().getRuleId())
                                                   .targetId(tr.getTargetId()).build());
                tr.getStandardList()
                        .forEach(standard -> targetStandardRelationList.add(DailyPlanTargetStandardRelationPO.builder().id(IdWorker.getId())
                                                                                    .planId(planId)
                                                                                    .targetCategory(tr.getTargetCategory())
                                                                                    .targetId(tr.getTargetId())
                                                                                    .standardId(standard.getId()).build()));
            });
        }
        if (po.getId() == null) {
            po.setId(planId);
            this.save(po);
        } else {
            this.updateById(po);
        }

        //* 计划扩展字段 *
        if (StringUtils.isNotBlank(param.getMetadata())) {
            dailyPlanExpandMapper.insert(DailyPlanExpandPO.builder().id(planId).metadata(param.getMetadata()).build());
        }
        // 人员
        if (CollectionUtils.isNotEmpty(dailyBaseSuperintendents)) {
            dailyPlanSuperintendentService.saveBatch(dailyBaseSuperintendents);
        }
        // 计划的执行内容
        if (CollectionUtils.isNotEmpty(planStandardList)) {
            dailyPlanStandardRelationService.saveBatch(planStandardList);
        }
        // 计划的执行对象
        if (CollectionUtils.isNotEmpty(planTargetRelationList)) {
            dailyPlanTargetRelationService.saveBatch(planTargetRelationList);
        }
        // 对象的执行内容
        if (CollectionUtils.isNotEmpty(targetStandardRelationList)) {
            dailyPlanTargetStandardRelationService.saveBatch(targetStandardRelationList);
        }

        // 文件持久化(这里放最后防止 事务回滚后client调用回滚不了)
        if (fileRefCreateParam != null) {
            fileRefClient.refInduce(fileRefCreateParam);
        }
        return po.getId();
    }
}
