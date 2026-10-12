package com.guhecloud.cube.base.flow.flowable.task.domain.repository;

import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstance;

import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/9/5 20:40
 * @description
 */
public interface TaskInstanceRepository {

    Optional<TaskInstance> get(String taskId);
}
