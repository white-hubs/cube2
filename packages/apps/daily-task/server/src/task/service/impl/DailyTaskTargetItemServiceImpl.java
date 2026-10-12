package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetItemPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskTargetItemMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetItemService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务对象的执行内容
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskTargetItemServiceImpl extends ServiceImpl<DailyTaskTargetItemMapper, DailyTaskTargetItemPO> implements DailyTaskTargetItemService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchByTaskId(Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap) {
        var taskTargetItemSaveList = new ArrayList<DailyTaskTargetItemPO>();
        for (var entry : taskTargetMap.entrySet()) {
            for (var taskTargetStandard : entry.getValue()) {
                var standardList = taskTargetStandard.getStandardList();
                for (var standard : standardList) {
                    for (DailyItemResultDTO i : standard.getItemList()) {
                        var targetItem = DailyTaskTargetItemPO.builder().id(IdWorker.getId())
                                .taskId(entry.getKey()).targetCategory(taskTargetStandard.getTargetCategory())
                                .targetId(taskTargetStandard.getTargetId())
                                .standardId(standard.getId())
                                .standardName(standard.getName()).itemId(i.getId()).itemName(i.getName())
                                .resultJsonString(JsonUtils.serialize(i.getResultList())).build();
                        taskTargetItemSaveList.add(targetItem);
                    }
                }
            }
        }
        this.saveBatch(taskTargetItemSaveList);
    }

    @Override
    public Map<Long, Long> getTargetStandByTaskId(Long taskId, Set<Long> noStandIdList) {
        if (CollectionUtils.isEmpty(noStandIdList)) {
            return new HashMap<>();
        }
        return Map.of();
    }
}
