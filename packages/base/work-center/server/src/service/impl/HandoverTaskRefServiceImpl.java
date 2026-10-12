package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.work.center.server.dao.HandoverTaskRefMapper;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverTaskRef;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.service.HandoverTaskRefService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:20
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HandoverTaskRefServiceImpl extends ServiceImpl<HandoverTaskRefMapper, HandoverTaskRef> implements HandoverTaskRefService {
    private final WorkTaskService workTaskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHandoverTaskRef(Long id, List<Long> handoverTaskList) {
        if (CollectionUtils.isEmpty(handoverTaskList)) {
            return;
        }
        var workTaskList = workTaskService.lambdaQuery().in(WorkTask::getId, handoverTaskList)
                .select(WorkTask::getId, WorkTask::getBusinessType)
                .list();
        if (CollectionUtils.isEmpty(workTaskList)) {
            return;
        }
        var taskTypeMap = workTaskList.stream()
                .collect(Collectors.toMap(WorkTask::getId, WorkTask::getBusinessType));
        var handoverTaskRefList = handoverTaskList.stream().map(t -> {
            var handoverTaskRef = new HandoverTaskRef();
            handoverTaskRef.setId(IdWorker.getId());
            handoverTaskRef.setHandoverId(id);
            handoverTaskRef.setTaskId(t);
            handoverTaskRef.setTaskType(taskTypeMap.get(t));
            return handoverTaskRef;
        }).collect(Collectors.toList());

        this.saveBatch(handoverTaskRefList);
    }
}
