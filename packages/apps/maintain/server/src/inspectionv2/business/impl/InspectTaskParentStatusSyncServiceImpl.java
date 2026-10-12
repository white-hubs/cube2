package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskParentStatusSyncService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskParentStatusSyncServiceImpl implements InspectTaskParentStatusSyncService {

    private static final EnumSet<InspectTaskStatusEnum> COMPLETED_STATUSES = EnumSet.of(
            InspectTaskStatusEnum.OVER,
            InspectTaskStatusEnum.WAIT_REPAIR,
            InspectTaskStatusEnum.REPAIRED
    );

    private final InspectTaskMapper taskMapper;

    @Override
    public void syncParentTaskStatus(Long taskId) {
        if (taskId == null) {
            return;
        }
        InspectTaskPO task = taskMapper.selectById(taskId);
        if (ObjectUtils.isEmpty(task) || task.getParentId() == null || task.getParentId() == 0) {
            return;
        }
        InspectTaskPO parentTask = taskMapper.selectById(task.getParentId());
        if (ObjectUtils.isEmpty(parentTask)) {
            return;
        }
        List<InspectTaskPO> childTasks = taskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                        .eq(InspectTaskPO::getParentId, task.getParentId())
                                                                        .eq(InspectTaskPO::getDeleted, false));
        if (CollectionUtils.isEmpty(childTasks)) {
            return;
        }
        boolean allCompleted = childTasks.stream()
                .map(InspectTaskPO::getStatus)
                .allMatch(COMPLETED_STATUSES::contains);
        if (!allCompleted) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        parentTask.setStatus(resolveCompletedStatus(parentTask, now));
        parentTask.setEndTime(now);
        boolean hasAbnormal = childTasks.stream()
                .map(InspectTaskPO::getResult)
                .anyMatch(result -> Objects.equals(result, InspectRecordResultEnum.ABNORMAL.name()));
        parentTask.setResult(hasAbnormal ? InspectRecordResultEnum.ABNORMAL.name() : InspectRecordResultEnum.NORMAL.name());
        taskMapper.updateById(parentTask);
    }

    private InspectTaskStatusEnum resolveCompletedStatus(InspectTaskPO task, LocalDateTime now) {
        if (task.getValidEndTime() != null && task.getValidEndTime().isBefore(now)) {
            return InspectTaskStatusEnum.EXPIRE;
        }
        return InspectTaskStatusEnum.OVER;
    }
}
