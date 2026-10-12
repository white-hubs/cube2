package com.guhecloud.cube.biz.daily.task.record.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;

import java.util.Map;

public class DailyRecordTargetConvert {

    /**
     * 构建记录目标对象
     *
     * @param taskId   任务ID
     * @param taskName 任务名称
     * @param recordId 记录ID
     * @param targetId 目标ID
     *
     * @return 构建完成的记录目标持久化对象
     */
    public static DailyRecordTargetPO builderRecordTarget(Long taskId, String taskName, Long recordId, Long targetId,
                                                          Map<Long, DailyTaskTargetPO> taskTargetMap
                                                         ) {
        // 使用建造者模式构建DailyRecordTargetPO对象，设置各种属性包括ID、任务信息、记录ID、目标信息等
        var taskTarget = taskTargetMap.getOrDefault(targetId, new DailyTaskTargetPO());
        return DailyRecordTargetPO.builder().id(IdWorker.getId()).taskId(taskId).taskName(taskName)
                .recordId(recordId).targetId(taskTarget.getTargetId()).targetName(taskTarget.getTargetName())
                .targetCategory(taskTarget.getTargetCategory()).targetCode(taskTarget.getTargetCode())
                .lntAndLat(taskTarget.getTargetLntAndLat()).location(taskTarget.getLocation()).build();
    }
}
