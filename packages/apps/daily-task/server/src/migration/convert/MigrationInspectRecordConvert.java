package com.guhecloud.cube.biz.daily.task.migration.convert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyExecutionResultEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyRecordStatusEnum;
import com.guhecloud.cube.biz.daily.task.migration.convert.base.MigrationBaseConvert;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectRecordItemMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectRecordMapper;
import com.guhecloud.cube.biz.daily.task.migration.dao.MigrationInspectRecordOrderMapper;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectRecordItemPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectRecordOrderPO;
import com.guhecloud.cube.biz.daily.task.migration.po.MigrationInspectRecordPO;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetItemService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordWorkOrderService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 巡检类型转换(包含记录的对象执行内容)
 */
@Order(7)
@Component
@RequiredArgsConstructor
public class MigrationInspectRecordConvert extends MigrationBaseConvert<MigrationInspectRecordPO, DailyRecordPO> {
    private final MigrationInspectRecordMapper mapper;
    private final MigrationInspectRecordItemMapper recordItemMapper;
    private final MigrationInspectRecordOrderMapper orderMapper;
    private final DailyRecordService service;
    private final DailyRecordWorkOrderService orderService;
    private final DailyRecordTargetService recordTargetService;
    private final DailyRecordTargetItemService recordTargetItemService;

    /**
     * 分页获取并转换数据。
     * <p>
     * 该方法从数据库中分页查询指定ID列表的 MigrationInspectRecordPO 数据，并将其转换为 DailyRecordPO 及相关联的数据结构，
     * 同时保存转换后的记录目标（DailyRecordTargetPO）和记录项（DailyRecordTargetItemPO）到对应的表中。
     *
     * @param current 当前页码，用于分页查询
     *
     * @return 返回封装了 DailyRecordPO 的分页结果对象 Pagination<DailyRecordPO>
     */
    @Override
    public Pagination<DailyRecordPO> getAndConvertDataPage(Long current) {
        // 查询原始检查记录数据
        var page = mapper.selectPage(new Page<>(current, 100), Wrappers.<MigrationInspectRecordPO>lambdaQuery()
                                             .eq(MigrationInspectRecordPO::getDeleted, false)
                                             .and(i -> i.isNull(MigrationInspectRecordPO::getBusinessType).or()
                                                     .eq(MigrationInspectRecordPO::getBusinessType, "INSPECTION").or().eq(MigrationInspectRecordPO::getBusinessType, ""))
                                    );

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        var oldList = page.getRecords();
        // 初始化各类集合与映射关系
        var recordList = new ArrayList<DailyRecordPO>();
        var targetList = new ArrayList<DailyRecordTargetPO>();
        var oldRecordIdList = new ArrayList<Long>();
        var recordTaskTargetMap = new HashMap<Long, TaskTargetDTO>();

        // 转换主记录及构建关联的目标信息
        oldList.forEach(item -> {
            oldRecordIdList.add(item.getId());
            recordTaskTargetMap.put(item.getId(), TaskTargetDTO
                    .builder().taskId(item.getTaskId()).taskName(item.getTaskName()).targetId(item.getTargetId())
                    .targetCategory(item.getTargetCategory())
                    .build());

            // 构建 DailyRecordPO 对象
            var po = DailyRecordPO.builder()
                    .id(item.getId())
                    .taskId(item.getTaskId())
                    .taskName(item.getTaskName())
                    .executionResult(item.getResult())
                    .recordStatus(DailyExecutionResultEnum.NORMAL.name().equals(item.getResult())
                                  ? DailyRecordStatusEnum.NORMAL.name()
                                  : DailyRecordStatusEnum.ABNORMAL_WAIT_REPAIR.name())
                    .executionTime(item.getTime())
                    .executorId(item.getUserId())
                    .executorName(item.getUsername())
                    .dailyType("DAILY_INSPECTION")
                    .build();
            this.setBasicTenant(item, po);
            recordList.add(po);

            // 构建 DailyRecordTargetPO 对象
            var target = DailyRecordTargetPO.builder().id(item.getId()).targetId(item.getTargetId()).taskId(item.getTaskId())
                    .taskName(item.getTaskName()).targetName(item.getTargetName()).targetName(item.getTargetName())
                    .targetCategory(item.getTargetCategory()).recordId(item.getId()).executionResult(item.getResult()).build();
            target.setTenant(po.getTenant());
            target.setDeleted(item.getDeleted());
            target.setCreateBy(item.getCreateBy());
            target.setCreateTime(item.getCreateTime());
            target.setUpdateBy(item.getUpdateBy());
            target.setUpdateTime(item.getUpdateTime());
            targetList.add(target);
        });
        // 记录工单
        var oldOrderList = orderMapper.selectList(Wrappers.<MigrationInspectRecordOrderPO>lambdaQuery()
                                                          .in(MigrationInspectRecordOrderPO::getRecordId, oldRecordIdList));
        var workOrderList = Optional.ofNullable(oldOrderList).orElse(new ArrayList<>()).stream().map(item -> {
            var taskTarget = recordTaskTargetMap.getOrDefault(item.getRecordId(), new TaskTargetDTO());
            return DailyRecordWorkOrderPO.builder()
                    .id(item.getId()).workOrderType(item.getBusinessCategory()).taskId(taskTarget.getTaskId()).recordId(item.getRecordId())
                    .targetCategory(item.getTargetCategory()).targetId(item.getTargetId())
                    .processInstanceId(item.getBusinessId()).processInstanceName(item.getBusinessName()).processInstanceStatus("PASS")
                    .build();
        }).collect(Collectors.toList());
        // 查询所有相关的检查项记录
        var recordItemList = recordItemMapper.selectList(Wrappers.<MigrationInspectRecordItemPO>lambdaQuery()
                                                                 .in(MigrationInspectRecordItemPO::getRecordId, oldRecordIdList));

        // 将检查项记录转换为目标项记录
        var targetItemList = Optional.ofNullable(recordItemList).orElse(new ArrayList<>()).stream().map(item -> {
            var taskTargetDTO = recordTaskTargetMap.getOrDefault(item.getRecordId(), new TaskTargetDTO());
            return DailyRecordTargetItemPO.builder().id(item.getId()).recordId(item.getRecordId()).taskId(taskTargetDTO.getTaskId())
                    .taskName(taskTargetDTO.getTaskName())
                    .targetId(taskTargetDTO.getTargetId())
                    .targetCategory(taskTargetDTO.getTargetCategory()).targetName(taskTargetDTO.getTargetName())
                    .standardId(item.getStandardId())
                    .standardName(item.getStandardName()).itemId(item.getItemId()).itemName(item.getItemName()).itemTag(item.getItemTag())
                    .mustTakePhotoResultItems(item.getItemImageResult()).mustRemarkResultItems(item.getRemarkResult())
                    .resultId(item.getItemResultId()).resultName(item.getItemResultName()).resultItemId(item.getItemResultId())
                    .resultItemName(item.getItemResultName()).errorFlag(item.getErrorValueFlag()).build();
        }).collect(Collectors.toList());

        // 批量插入
        if (CollectionUtils.isNotEmpty(workOrderList)) {
            orderService.saveBatch(workOrderList, 100);
        }
        if (CollectionUtils.isNotEmpty(targetList)) {
            recordTargetService.saveBatch(targetList, 100);
        }
        if (CollectionUtils.isNotEmpty(targetItemList)) {
            recordTargetItemService.saveBatch(targetItemList, 100);
        }

        // 返回转换后的分页数据
        return PagingUtils.convert(page, recordList);
    }

    /**
     * 获取服务类
     *
     * @return 服务类
     */
    @Override
    public IService<DailyRecordPO> getService() {
        return service;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TaskTargetDTO {
        private Long taskId;
        private String taskName;
        private Long targetId;
        private String code;
        private String targetCategory;
        private String targetName;
    }
}
