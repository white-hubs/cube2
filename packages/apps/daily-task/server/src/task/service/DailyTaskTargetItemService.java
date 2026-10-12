package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetItemPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailyTaskTargetItemService extends IService<DailyTaskTargetItemPO> {
    void saveBatchByTaskId(Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap);

    Map<Long, Long> getTargetStandByTaskId(Long taskId, Set<Long> noStandIdList);
}
