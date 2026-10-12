package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskStatusChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 巡更任务状态变更
 * @date 2023/6/2 9:02
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskStatusChangeServiceImpl implements PatrolTaskStatusChangeService {

    private final PatrolTaskMapper patrolTaskMapper;

    /**
     * @Author wql
     * @Description 变更为待接收
     * @Date 9:08 2023/6/2
     **/
    @Override
    public PatrolTask changeToWaitAccept(Long taskId) {
        PatrolTask task = patrolTaskMapper.selectById(taskId);
        task.setStatus(PatrolTaskStateEnum.TO_BE_RECEIVED.name());
        patrolTaskMapper.updateById(task);
        return task;
    }

    /**
     * @Author wql
     * @Description 变更为执行中
     * @Date 9:08 2023/6/2
     **/
    @Override
    public PatrolTask changeToInExecution(Long taskId) {
        PatrolTask task = patrolTaskMapper.selectById(taskId);
        task.setStatus(PatrolTaskStateEnum.IN_EXECUTION.name());
        patrolTaskMapper.updateById(task);
        return task;
    }

    /**
     * @Author wql
     * @Description 变更为已结束
     * @Date 9:08 2023/6/2
     **/
    @Override
    public PatrolTask changeToFinish(Long taskId) {
        PatrolTask task = patrolTaskMapper.selectById(taskId);
        task.setStatus(PatrolTaskStateEnum.FINISHED.name());
        patrolTaskMapper.updateById(task);
        return task;
    }
}
