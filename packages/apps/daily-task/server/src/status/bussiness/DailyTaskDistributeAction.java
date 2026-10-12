package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.cube.biz.daily.task.status.base.StateAction;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.DISTRIBUTE;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/19 18:51
 * @Description:
 */
@Service
@Slf4j
public class DailyTaskDistributeAction implements StateAction {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void beforeActionExecute(TransitionContext context) {

    }

    @Override
    public void afterActionExecute(TransitionContext context) {
        List<BaseState> dailyTaskPO = context.getEntity();
        Map<String, Object> params = context.getParams();
        if (MapUtils.isEmpty(params)) {
            return;
        }
        //剩余对象数
        long count = Long.parseLong(params.getOrDefault("targetNum", "0").toString());
        if (count > 0) {
            return;
        }
        Map<Long, String> beforeState = context.getBeforeState();
        Set<Long> sourceUsers = (Set<Long>) params.get("sourceUsers");
        //直接删除之前的待办消息
        for (BaseState base : dailyTaskPO) {
            DailyTaskPO po = (DailyTaskPO) base;
            TodoTaskFlowCirculationMessage circulationMessage = new TodoTaskFlowCirculationMessage();
            circulationMessage.setBusinessKey(String.valueOf(po.getId()));
            circulationMessage.setBusinessStatus(beforeState.get(po.getId()));
            circulationMessage.setUserIds(sourceUsers);
            circulationMessage.setHandlerTime(LocalDateTime.now());
            circulationMessage.setTaskTime(LocalDateTime.now());
            circulationMessage.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
            circulationMessage.setGeneratedNextStatusTask(false);
            circulationMessage.setNextBusinessStatus(po.getStatus());
            circulationMessage.setNextBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, circulationMessage, String.valueOf(po.getId()));
            WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = new WorkTaskFlowCirculationMessage();
            workTaskFlowCirculationMessage.setBusinessKey(String.valueOf(po.getId()));
            workTaskFlowCirculationMessage.setBusinessStatus(beforeState.get(po.getId()));
            workTaskFlowCirculationMessage.setUserIds(sourceUsers);
            workTaskFlowCirculationMessage.setHandlerTime(LocalDateTime.now());
            workTaskFlowCirculationMessage.setTaskTime(LocalDateTime.now());
            workTaskFlowCirculationMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
            workTaskFlowCirculationMessage.setGeneratedNextStatusTask(false);
            workTaskFlowCirculationMessage.setNextBusinessStatus(po.getStatus());
            workTaskFlowCirculationMessage.setNextBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
            //发消息
            RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage, String.valueOf(po.getId()));

        }

    }

    @Override
    public String getBusinessEvent() {
        return DISTRIBUTE;
    }
}
