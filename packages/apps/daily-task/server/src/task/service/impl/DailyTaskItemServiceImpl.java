package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultDTO;
import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationInsertParam;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskItemPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordItemResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordItemService;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskItemMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskItemService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务执行内容
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskItemServiceImpl extends ServiceImpl<DailyTaskItemMapper, DailyTaskItemPO> implements DailyTaskItemService {

    private final DailyRecordItemService dailyRecordItemService;
    private final DailyItemService dailyItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchByTaskId(Map<Long, List<Long>> taskItemMap) {
        if (MapUtils.isEmpty(taskItemMap)) {
            return;
        }
        var taskItemSaveList = new ArrayList<DailyTaskItemPO>();
        Set<Long> itemIds = taskItemMap.values().stream().flatMap(List::stream)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        Map<Long, DailyItemResultDTO> itemResultDTOMap = dailyItemService.getItemInfoList(itemIds);
        for (var entry : taskItemMap.entrySet()) {
            for (var itemId : entry.getValue()) {
                var itemInfo = itemResultDTOMap.getOrDefault(itemId, new DailyItemResultDTO());
                taskItemSaveList.add(DailyTaskItemPO.builder().id(IdWorker.getId())
                                             .taskId(entry.getKey())
                                             .itemId(itemInfo.getId())
                                             .itemName(itemInfo.getName())
                                             .itemContent(itemInfo.getContent())
                                             .operationGuide(itemInfo.getOperationGuide())
                                             .resultJsonString(JsonUtils.serialize(itemInfo.getResultList())).build());
            }
        }
        this.saveBatch(taskItemSaveList);
    }

    @Override
    public List<DailyItemResultDTO> getTaskItemList(Long id) {
        var dailyTaskItemPOList = this.list(Wrappers.<DailyTaskItemPO>lambdaQuery()
                                                    .eq(DailyTaskItemPO::getTaskId, id));
        if (CollectionUtils.isEmpty(dailyTaskItemPOList)) {
            return new ArrayList<>();
        }
        var recordItemDetail = dailyRecordItemService.getRecordItemDetail(id);
        Map<Long, DailyRecordItemResult> dailyRecordItemResultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(recordItemDetail)) {
            dailyRecordItemResultMap = recordItemDetail.stream()
                    .collect(Collectors.toMap(DailyRecordItemResult::getItemId, Function.identity(), (k1, k2) -> k1));
        }
        var itemResultDTOS = new ArrayList<DailyItemResultDTO>();
        for (DailyTaskItemPO item : dailyTaskItemPOList) {
            itemResultDTOS.add(DailyItemResultDTO.builder()
                                       .id(item.getItemId())
                                       .name(item.getItemName())
                                       .content(item.getItemContent())
                                       .operationGuide(item.getOperationGuide())
                                       .resultList(JsonUtils.deserializeToList(item.getResultJsonString(), DailyResultDTO.class))
                                       .executeResult(dailyRecordItemResultMap.get(item.getItemId()))
                                       .build());
        }
        return itemResultDTOS;
    }

    @Override
    public void handlerTaskItem(List<DailyTaskAddParam> paramList) {
        List<DailyItemResultRelationInsertParam> noExistItemList = new ArrayList<>();
        for (DailyTaskAddParam dailyTaskAddParam : paramList) {
            if (CollectionUtils.isNotEmpty(dailyTaskAddParam.getNoExistItemList())) {
                List<Long> taskItemIds = new ArrayList<>();
                dailyTaskAddParam.getNoExistItemList().forEach(t -> {
                    t.setIsTemp(!dailyTaskAddParam.getIsSaveTempItem());
                    t.setId(IdWorker.getId());
                    t.setDailyType(dailyTaskAddParam.getDailyType());
                    taskItemIds.add(t.getId());
                });
                dailyTaskAddParam.setTaskItemIdList(taskItemIds);
                noExistItemList.addAll(dailyTaskAddParam.getNoExistItemList());
            }
        }
        dailyItemService.saveBatch(noExistItemList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTaskItem(Long id, List<Long> taskItemIdList) {
        Set<Long> existIds = this.lambdaQuery().eq(DailyTaskItemPO::getTaskId, id).list()
                .stream().map(DailyTaskItemPO::getItemId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(taskItemIdList)) {
            Set<Long> deletedIds = existIds.stream().filter(t -> !taskItemIdList.contains(t)).collect(Collectors.toSet());
            dailyItemService.removeTempItem(new ArrayList<>(deletedIds));
        } else {
            dailyItemService.removeTempItem(new ArrayList<>(existIds));
        }
        this.remove(new LambdaQueryWrapper<DailyTaskItemPO>().eq(DailyTaskItemPO::getTaskId, id));

    }
}
