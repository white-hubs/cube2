package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetItemPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.enums.DailySuperintendentTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskUserTypeEnum;
import com.guhecloud.cube.biz.daily.task.migration.MigrationUtils;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectTaskMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectTaskTargetItemMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectTaskTargetMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectTaskPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectTaskTargetItemPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetItemService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 巡检类型转换
 */
@Order(8)
@Component
@RequiredArgsConstructor
public class MigrationInspectTaskConvert extends MigrationBaseConvert<MigrationInspectTaskPO, DailyTaskPO> {
    private final MigrationInspectTaskMapper mapper;
    private final MigrationInspectTaskTargetMapper targetMapper;
    private final MigrationInspectTaskTargetItemMapper targetItemMapper;
    private final DailyTaskService service;
    private final DailyTaskUserService userService;
    private final DailyTaskTargetService targetService;
    private final DailyTaskTargetItemService targetItemService;

    @Override
    public Pagination<DailyTaskPO> getAndConvertDataPage(Long current) {
        // 查询原始检查记录数据
        var page = mapper.selectPage(new Page<>(current, 10), Wrappers.<MigrationInspectTaskPO>lambdaQuery()
                                             .eq(MigrationInspectTaskPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectTaskPO::getBusinessType).or()
                                                     .eq(MigrationInspectTaskPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectTaskPO::getBusinessType, ""))
                                    );
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var oldList = page.getRecords();
        if (CollectionUtils.isEmpty(oldList)) {
            return Pagination.empty();
        }
        // 任务+人员数据转换
        var taskIdList = new ArrayList<Long>();
        var taskUserList = new ArrayList<DailyTaskUserPO>();
        var taskStatusMap = new HashMap<Long, String>();
        var list = oldList.stream().map(old -> {
            taskIdList.add(old.getId());
            // 执行人
            taskUserList.addAll(Arrays.stream(old.getReceiveIds().split(",")).map(userId -> {
                var taskUserPO = DailyTaskUserPO.builder().id(IdWorker.getId()).masterId(old.getId())
                        .superintendentType(DailySuperintendentTypeEnum.USER.name())
                        .taskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name()).targetId(Long.valueOf(userId)).build();
                taskUserPO.setTenant(old.getTenant());
                taskUserPO.setDeleted(old.getDeleted());
                taskUserPO.setCreateBy(old.getCreateBy());
                taskUserPO.setCreateTime(old.getCreateTime());
                taskUserPO.setUpdateBy(old.getUpdateBy());
                taskUserPO.setUpdateTime(old.getUpdateTime());
                return taskUserPO;
            }).toList());
            // 负责人
            var taskUserPO = DailyTaskUserPO.builder().id(IdWorker.getId()).masterId(old.getId())
                    .superintendentType(DailySuperintendentTypeEnum.USER.name())
                    .taskUserType(DailyTaskUserTypeEnum.SUPERINTENDENT.name())
                    .targetId(StringUtils.isNotBlank(old.getPublisherId()) ? Long.valueOf(old.getPublisherId()) : null).build();
            taskUserPO.setTenant(old.getTenant());
            taskUserPO.setDeleted(old.getDeleted());
            taskUserPO.setCreateBy(old.getCreateBy());
            taskUserPO.setCreateTime(old.getCreateTime());
            taskUserPO.setUpdateBy(old.getUpdateBy());
            taskUserPO.setUpdateTime(old.getUpdateTime());
            taskUserList.add(taskUserPO);

            var LimitTotalMinuteDTO = MigrationUtils.convertMinutesToDHM(old.getLimitTotalMinute());
            var status = this.getHandlerStatus(old.getStatus());
            taskStatusMap.put(old.getId(), status);
            var po = DailyTaskPO.builder().id(old.getId()).dailyType("DAILY_INSPECTION").planId(old.getPlanId()).name(old.getName())
                    .status(status).taskCategoryId(old.getTypeId()).urgencyId(0L).targetCount(old.getTargetNum())//.progress()
                    .deadline(old.getValidEndTime()).startTime(old.getBeginTime()).endTime(old.getEndTime())
                    .taskDay(LimitTotalMinuteDTO.getTaskDay()).taskHour(LimitTotalMinuteDTO.getTaskHour())
                    .taskMinute(LimitTotalMinuteDTO.getTaskMinute())
                    .description(old.getRemark())
                    .build();
            setBasicTenant(old, po);
            return po;
        }).toList();
        // 任务对象数据转换
        var targetList = new ArrayList<DailyTaskTargetPO>();
        var oldTargetMap = new HashMap<String, MigrationInspectTaskTargetPO>();
        var oldTargetList = targetMapper.selectList(Wrappers.<MigrationInspectTaskTargetPO>lambdaQuery()
                                                            .in(MigrationInspectTaskTargetPO::getTaskId, taskIdList));
        Optional.ofNullable(oldTargetList).orElse(Collections.emptyList())
                .forEach(oldTarget -> {
                    var taskTarget = DailyTaskTargetPO.builder().id(oldTarget.getId()).targetId(oldTarget.getTargetId())
                            .taskId(oldTarget.getTaskId()).status(taskStatusMap.get(oldTarget.getTaskId()))
                            .targetCategory(oldTarget.getTargetCategory())
                            .targetName(oldTarget.getTargetName()).targetCode(oldTarget.getTargetCode())
                            .targetName(oldTarget.getTargetName())
                            .areaName(oldTarget.getTargetAreaName()).location(oldTarget.getLocation())
                            .targetLntAndLat(oldTarget.getLatAndLon())
                            .distributed(false).build();
                    taskTarget.setTenant(oldTarget.getTenant());
                    taskTarget.setDeleted(oldTarget.getDeleted());
                    taskTarget.setCreateBy(oldTarget.getCreateBy());
                    taskTarget.setCreateTime(oldTarget.getCreateTime());
                    taskTarget.setUpdateBy(oldTarget.getUpdateBy());
                    taskTarget.setUpdateTime(oldTarget.getUpdateTime());
                    targetList.add(taskTarget);
                    oldTargetMap.put(String.valueOf(oldTarget.getTaskId()) + oldTarget.getTargetId(), oldTarget);
                });
        // 任务对象执行内容
        var oldTargetItemList = targetItemMapper.selectList(Wrappers.<MigrationInspectTaskTargetItemPO>lambdaQuery()
                                                                    .in(MigrationInspectTaskTargetItemPO::getTaskId, taskIdList));
        var targetItemList = new ArrayList<DailyTaskTargetItemPO>();
        Optional.ofNullable(oldTargetItemList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(t -> String.valueOf(t.getTaskId()) + t.getTargetId())).forEach((key, ls) -> {
                    var first = ls.getFirst();
                    var oldTarget = oldTargetMap.getOrDefault(
                            String.valueOf(first.getTaskId()) + first.getTargetId(), new MigrationInspectTaskTargetPO());
                    targetItemList.addAll(ls.stream().map(item -> {
                        var targetItem = DailyTaskTargetItemPO.builder().id(item.getId()).taskId(item.getTaskId()).targetId(item.getTargetId())
                                .targetCategory(oldTarget.getTargetCategory()).standardId(item.getStandardId()).standardName(item.getStandardName())
                                .itemId(item.getItemId()).itemName(item.getItemName())
                                .itemContent(item.getItemName()).resultJsonString(item.getResultText()).build();
                        targetItem.setTenant(item.getTenant());
                        targetItem.setDeleted(item.getDeleted());
                        targetItem.setCreateBy(item.getCreateBy());
                        targetItem.setCreateTime(item.getCreateTime());
                        targetItem.setUpdateBy(item.getUpdateBy());
                        targetItem.setUpdateTime(item.getUpdateTime());
                        return targetItem;
                    }).toList());
                });
        if (CollectionUtils.isNotEmpty(list)) {
            for (DailyTaskPO dailyTaskPO : list) {
                service.save(dailyTaskPO);
            }
        }
        if (CollectionUtils.isNotEmpty(targetList)) {
            targetService.saveBatch(targetList, 100);
        }
        if (CollectionUtils.isNotEmpty(taskUserList)) {
            userService.saveBatch(taskUserList, 100);
        }
        if (CollectionUtils.isNotEmpty(targetItemList)) {
            targetItemService.saveBatch(targetItemList, 100);
        }
        // 返回转换后的分页数据
        return PagingUtils.convert(page, new ArrayList<>());
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyTaskPO> getService() {
        return service;
    }

    private String getHandlerStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return switch (status) {
            case "SPLIT", "ACCEPT", "EXECUTING", "EXPIRE" -> "WAIT";
            case "OVER", "OVERTIME" -> "DONE";
            case "STOP" -> "TERMINATED";
            default -> status;
        };
    }
}
