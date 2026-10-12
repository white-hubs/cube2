package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskItemPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;

import java.util.List;
import java.util.Map;

public interface DailyTaskItemService extends IService<DailyTaskItemPO> {
    void saveBatchByTaskId(Map<Long, List<Long>> taskItemMap);

    List<DailyItemResultDTO> getTaskItemList(Long id);

    void handlerTaskItem(List<DailyTaskAddParam> paramList);

    void removeTaskItem(Long id, List<Long> taskItemIdList);
}
