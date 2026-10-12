package com.guhecloud.cube.biz.daily.task.status.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/12 11:32
 * @Description: 业务状态机接口
 */
public interface BusinessStateMachine {

    /**
     * 发布任务事件
     */
    void pushEvent(Long taskId, String businessType, String event, Map<String, Object> variables);

    void pushEvent(Long taskId, String businessType, String event);

    void pushEvent(List<Long> taskIds, String businessType, String event);

    void pushEvent(List<Long> taskIds, String businessType, String event, Map<String, Object> variables);

    String getCompleteState(String businessType, String event);

    List<String> getNoFinalStatus(String dailyType);
}
