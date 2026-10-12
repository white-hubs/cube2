package com.guhecloud.cube.biz.daily.task.status.base;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 13:58
 * @Description: 任务状态提供接口
 */
public interface TaskStateProvider {

    /**
     * 更新状态
     */
    void updateState();

    void batchInit(List<Long> taskIds, String stateProvider);

    List<BaseState> batchGet();

    void clearTaskEntityInfo();

    void setEntity(List<BaseState> entity);
}
