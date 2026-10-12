package com.guhecloud.cube.base.flow.flowable.task.infrastructure.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import org.springframework.stereotype.Service;

@Service
public class TaskLogService extends ServiceImpl<TaskLogMapper, TaskLogPO> {
}
