package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyPlanStatusEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailySuperintendentTypeEnum;
import com.guhecloud.cube.biz.daily.task.migration.MigrationUtils;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectPlanMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectPlanRelationMapper;
import com.guhecloud.cube.biz.daily.task.migration.dto.MigrationPlanCronTypeDTO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectPlanPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectPlanRelationPO;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyCronUtils;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanSuperintendentService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetRelationService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetStandardRelationService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 巡检类型转换
 */
@Slf4j
@Order(9)
@Component
@RequiredArgsConstructor
public class MigrationInspectPlanConvert extends MigrationBaseConvert<MigrationInspectPlanPO, DailyPlanPO> {
    private final MigrationInspectPlanMapper mapper;
    private final MigrationInspectPlanRelationMapper relationMapper;
    private final DailyPlanService service;
    private final DailyPlanTargetRelationService targetService;
    private final DailyPlanSuperintendentService superintendentService;
    private final DailyPlanTargetStandardRelationService targetStandardService;

    @Override
    public Pagination<DailyPlanPO> getAndConvertDataPage(Long current) {
        // 查询原始计划数据
        var page = mapper.selectPage(new Page<>(current, 1), Wrappers.<MigrationInspectPlanPO>lambdaQuery()
                                             .select(
                                                     MigrationInspectPlanPO::getId,
                                                     MigrationInspectPlanPO::getCode,
                                                     MigrationInspectPlanPO::getName,
                                                     MigrationInspectPlanPO::getTypeId,
                                                     MigrationInspectPlanPO::getTypeName,
                                                     MigrationInspectPlanPO::getLevel,
                                                     MigrationInspectPlanPO::getBeginTime,
                                                     MigrationInspectPlanPO::getEndTime,
                                                     MigrationInspectPlanPO::getValidBeginTime,
                                                     MigrationInspectPlanPO::getValidEndTime,
                                                     MigrationInspectPlanPO::getCronId,
                                                     MigrationInspectPlanPO::getCronName,
                                                     MigrationInspectPlanPO::getCronExpression,
                                                     MigrationInspectPlanPO::getEnable,
                                                     MigrationInspectPlanPO::getLimitTimeJson,
                                                     MigrationInspectPlanPO::getLimitTotalMinute,
                                                     MigrationInspectPlanPO::getReceiveDeptIds,
                                                     MigrationInspectPlanPO::getReceivePostIds,
                                                     MigrationInspectPlanPO::getReceiveRoleIds,
                                                     MigrationInspectPlanPO::getReceiveIds,
                                                     MigrationInspectPlanPO::getTargetNum,
                                                     MigrationInspectPlanPO::getExecTimes,
                                                     MigrationInspectPlanPO::getRemark,
                                                     MigrationInspectPlanPO::getCreatorId
                                                    )
                                             .eq(MigrationInspectPlanPO::getDeleted, false)
                                             .eq(MigrationInspectPlanPO::getStatus, "EXECUTING")
                                             .eq(MigrationInspectPlanPO::getIsCheck, false)
                                             .eq(MigrationInspectPlanPO::getCycle, true)
                                             .and(i -> i.isNull(MigrationInspectPlanPO::getBusinessType).or()
                                                     .eq(MigrationInspectPlanPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectPlanPO::getBusinessType, ""))
                                    );

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var oldList = page.getRecords();

        var planIdList = new ArrayList<Long>();
        // 计划人员集合
        var planUserList = new ArrayList<DailyPlanSuperintendentPO>();
        // 计划数据
        var list = oldList.stream().map(old -> {
            planIdList.add(old.getId());
            //人员
            this.handlerPlanUser(old, planUserList);
            // 计划
            return this.handlerPlan(old);
        }).collect(Collectors.toList());

        // 计划对象
        var planTargetList = new ArrayList<DailyPlanTargetRelationPO>();
        var planTargetItemList = new ArrayList<DailyPlanTargetStandardRelationPO>();
        this.handlerPlanTargetAndItem(planIdList, planTargetList, planTargetItemList);
        // 入库
        if (CollectionUtils.isNotEmpty(planUserList)) {
            superintendentService.saveBatch(planUserList, 100);
        }
        if (CollectionUtils.isNotEmpty(planTargetList)) {
            targetService.saveBatch(planTargetList, 100);
        }
        if (CollectionUtils.isNotEmpty(planTargetItemList)) {
            targetStandardService.saveBatch(planTargetItemList, 100);
        }
        // 返回转换后的分页数据
        return PagingUtils.convert(page, list);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyPlanPO> getService() {
        return service;
    }

    private DailyPlanPO handlerPlan(MigrationInspectPlanPO old) {
        MigrationPlanCronTypeDTO cronType = null;
        if (StringUtils.isNotBlank(old.getCronName()) && old.getCronName().contains("cycleType")) {
            cronType = JsonUtils.deserialize(old.getCronName(), MigrationPlanCronTypeDTO.class);
        }
        var limitTotalMinuteDTO = MigrationUtils.convertMinutesToDHM(old.getLimitTotalMinute());
        var cron = old.getCronExpression().length() > 6 ? DailyCronUtils.convertQuartzToSpring(old.getCronExpression())
                                                        : old.getCronExpression();
        LocalDateTime nextTime = null;
        try {
            CronExpression cronExpression = new CronExpression(cron);
            nextTime = cronExpression.getNextValidTimeAfter(LocalDateTime.now());
        } catch (ParseException e) {
            log.error("日常任务, 计划生成任务，cron表达式解析异常：", e);
        }
        var po = DailyPlanPO.builder()
                .id(old.getId())
                .dailyType("DAILY_INSPECTION")
                .name(old.getName())
                .status(DailyPlanStatusEnum.RUNNING.name())
                .enable(old.getEnable())
                .executeWay(old.getMethod())
                .cronId(old.getCronId())
                .nextTime(nextTime)
                .executionCount(old.getExecTimes())
                .taskDay(limitTotalMinuteDTO.getTaskDay())
                .taskHour(limitTotalMinuteDTO.getTaskHour())
                .taskMinute(limitTotalMinuteDTO.getTaskMinute())
                .taskCategoryId(old.getTypeId())
                .startTime(old.getValidBeginTime())
                .endTime(old.getValidEndTime())
                .description(old.getRemark())
                .build();
        if (cronType != null) {
            po.setSpecifiedType(cronType.getCycleType());
            po.setSpecifiedDays(cronType.getSelection());
            po.setSpecifiedTime(cronType.getHour());
        }
        setBasicTenant(old, po);
        return po;
    }

    private void handlerPlanUser(MigrationInspectPlanPO old, List<DailyPlanSuperintendentPO> planUserList) {
        if (StringUtils.isNotBlank(old.getReceiveIds())) {
            Arrays.asList(old.getReceiveIds().split(",")).forEach(targetId -> {
                planUserList.add(DailyPlanSuperintendentPO.builder().id(IdWorker.getId()).masterId(old.getId())
                                         .targetId(Long.valueOf(targetId))
                                         .superintendentType(DailySuperintendentTypeEnum.USER.name()).build());
            });
        } else {
            if (StringUtils.isNotBlank(old.getReceiveDeptIds())) {
                Arrays.asList(old.getReceiveDeptIds().split(",")).forEach(targetId -> {
                    planUserList.add(DailyPlanSuperintendentPO.builder().id(IdWorker.getId()).masterId(old.getId())
                                             .targetId(Long.valueOf(targetId))
                                             .superintendentType(DailySuperintendentTypeEnum.DEPT.name()).build());
                });
            }
            if (StringUtils.isNotBlank(old.getReceivePostIds())) {
                Arrays.asList(old.getReceivePostIds().split(",")).forEach(targetId -> {
                    planUserList.add(DailyPlanSuperintendentPO.builder().id(IdWorker.getId()).masterId(old.getId())
                                             .targetId(Long.valueOf(targetId))
                                             .superintendentType(DailySuperintendentTypeEnum.POST.name()).build());
                });
            }
            if (StringUtils.isNotBlank(old.getReceiveRoleIds())) {
                Arrays.asList(old.getReceiveRoleIds().split(",")).forEach(targetId -> {
                    planUserList.add(DailyPlanSuperintendentPO.builder().id(IdWorker.getId()).masterId(old.getId())
                                             .targetId(Long.valueOf(targetId))
                                             .superintendentType(DailySuperintendentTypeEnum.ROLE.name()).build());
                });
            }
        }
    }

    private void handlerPlanTargetAndItem(ArrayList<Long> planIdList, List<DailyPlanTargetRelationPO> planTargetList,
                                          List<DailyPlanTargetStandardRelationPO> planTargetItemList
                                         ) {
        var oldPlanRelationList = relationMapper.selectList(Wrappers.<MigrationInspectPlanRelationPO>lambdaQuery()
                                                                    .in(MigrationInspectPlanRelationPO::getPlanId, planIdList));
        Optional.ofNullable(oldPlanRelationList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(r -> String.valueOf(r.getPlanId()) + r.getTargetId())).forEach((k, ls) -> {
                    var first = ls.getFirst();
                    var planTarget = DailyPlanTargetRelationPO.builder().id(first.getId()).planId(first.getPlanId()).targetId(first.getTargetId())
                            .targetCategory(first.getTargetCategory()).build();
                    planTarget.setTenant(first.getTenant());
                    planTarget.setDeleted(first.getDeleted());
                    planTarget.setCreateBy(first.getCreateBy());
                    planTarget.setCreateTime(first.getCreateTime());
                    planTarget.setUpdateBy(first.getUpdateBy());
                    planTarget.setUpdateTime(first.getUpdateTime());
                    planTargetList.add(planTarget);
                    ls.stream().collect(Collectors.groupingBy(MigrationInspectPlanRelationPO::getStandardId))
                            .forEach((standardId, standardItemList) -> {
                                var standardItem = DailyPlanTargetStandardRelationPO.builder().id(IdWorker.getId()).planId(first.getPlanId())
                                        .targetId(first.getTargetId()).targetCategory(first.getTargetCategory()).standardId(standardId).build();
                                standardItem.setTenant(first.getTenant());
                                standardItem.setDeleted(first.getDeleted());
                                standardItem.setCreateBy(first.getCreateBy());
                                standardItem.setCreateTime(first.getCreateTime());
                                standardItem.setUpdateBy(first.getUpdateBy());
                                standardItem.setUpdateTime(first.getUpdateTime());
                                planTargetItemList.add(standardItem);
                            });
                });
    }

}
