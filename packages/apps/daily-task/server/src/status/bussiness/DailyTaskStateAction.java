package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.cube.biz.daily.task.status.base.StateAction;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum.DAILY_TASK_GENERATE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.ISSUE_TASK;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/12 09:42
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskStateAction implements StateAction {

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
        Map<Long, Set<Long>> taskUserIdMap = (Map<Long, Set<Long>>) params.get("userIdMap");
        boolean sendTodo = (boolean) params.get("sendTodo");
        for (BaseState base : dailyTaskPO) {
            DailyTaskPO po = (DailyTaskPO) base;
            //发送消息提醒
            if (!taskUserIdMap.containsKey(po.getId())) {
                continue;
            }
            Set<Long> userIds = taskUserIdMap.getOrDefault(po.getId(), Collections.emptySet());
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", po.getName());
            List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
            MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
            notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
            notifyTarget.setIds(userIds);
            notifyTargets.add(notifyTarget);
            MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                    .tenant(po.getTenant())
                    .applicationCode(context.getBusinessTypeName())
                    .msgSceneCode(DAILY_TASK_GENERATE.name())
                    .businessId(String.valueOf(po.getId()))
                    .tmpParams(paramMap)
                    .messageNotifyTargets(notifyTargets)
                    .build();
            MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
            try {
                MessageSendResponse messageSendResponse = sendClient.sceneSend(message);
                log.info("日常任务下发消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
            } catch (Exception e) {
                log.error("日常任务下发消息发送失败", e);
            }
            if (sendTodo) {
                //生成待办任务
                TodoTaskInitMessage todoTaskInitMessage = new TodoTaskInitMessage();
                todoTaskInitMessage.setApplicationCode(context.getBusinessType());
                todoTaskInitMessage.setApplicationName(context.getBusinessTypeName());
                todoTaskInitMessage.setBusinessKey(String.valueOf(po.getId()));
                todoTaskInitMessage.setBusinessType(context.getBusinessType());
                todoTaskInitMessage.setBusinessTypeName(context.getBusinessTypeName());
                todoTaskInitMessage.setBusinessStatus(po.getStatus());
                todoTaskInitMessage.setBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
                todoTaskInitMessage.setTaskName(po.getName());
                todoTaskInitMessage.setTaskTime(po.getCreateTime());
                todoTaskInitMessage.setOperationType(TodoTaskFlowOperationEnum.INSERT);
                todoTaskInitMessage.setUserIds(new ArrayList<>(userIds));
                //发消息
                RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, todoTaskInitMessage, String.valueOf(po.getId()));
                WorkTaskInitMessage workTaskInitMessage = new WorkTaskInitMessage();
                workTaskInitMessage.setApplicationCode(context.getBusinessType());
                workTaskInitMessage.setApplicationName(context.getBusinessTypeName());
                workTaskInitMessage.setBusinessKey(String.valueOf(po.getId()));
                workTaskInitMessage.setBusinessType(context.getBusinessType());
                workTaskInitMessage.setBusinessTypeName(context.getBusinessTypeName());
                workTaskInitMessage.setBusinessStatus(po.getStatus());
                workTaskInitMessage.setBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
                workTaskInitMessage.setTaskName(po.getName());
                workTaskInitMessage.setTaskTime(po.getCreateTime());
                workTaskInitMessage.setOperationType(WorkTaskFlowOperationEnum.INSERT);
                workTaskInitMessage.setBusinessPrincipal(JsonUtils.serialize(po));
                workTaskInitMessage.setUserIds(new ArrayList<>(userIds));
                //发消息
                RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskInitMessage, String.valueOf(po.getId()));

            }
        }

    }

    @Override
    public String getBusinessEvent() {
        return ISSUE_TASK;
    }
}
