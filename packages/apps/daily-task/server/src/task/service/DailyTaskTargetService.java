package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailyTaskTargetService extends IService<DailyTaskTargetPO> {
    List<DailyTaskTargetPO> getTargetByTaskId(Long taskId);

    void saveBatchByTaskId(String dailyType, Map<Long, List<DailyTargetStandardResultParam>> taskStandardMap);

    Pagination<DailyTaskTargetResult> getTargetByTaskId(DailyTaskTargetQueryParam param);

    List<DailyStandardResultDTO> getTargetStandardResult(Set<Long> id, Long targetId);

    List<DailyPlanTargetResult> getTargetByTaskIdsForApp(DailyTaskTargetQueryParam param);

    Long getCompleteTargetNum(Long id, String dailyType);

    List<DailyTargetDTO> getTaskOtherTarget(Long taskId, Long targetId, List<Long> itemIdList);

    List<Long> getTargetTagIdList(Long taskId);

    Integer handleTaskTarget(Long taskId, Long parentId, String dailyType);

    List<DailyTaskTargetPO> getNoDistributedTarget(Long taskId, String dailyType);

    List<DailyTaskTargetPO> getStatusTargetNum(Long taskId, String dailyType, List<String> noCompleteStatus);

    void removeByTaskId(Long taskId);

    Map<Long, List<DailyTaskTargetPO>> getTargetByTaskIdListGroupByTaskId(Set<Long> taskIdList);

    void distributedTaskTarget(Map<Long, Set<DailyTaskTargetPO>> dailyTaskTargetRefMap, boolean setNull);

    List<Long> getTaskIdByObjectId(Long objectId, String dailyType);

    DailyTaskTargetResult getDailyTaskTargetRelate(Long taskId, Long id);
}
