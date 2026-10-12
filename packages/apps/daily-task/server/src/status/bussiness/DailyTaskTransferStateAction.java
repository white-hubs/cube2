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
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum.DAILY_TASK_TRANSFER;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TRANSFER;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/17 20:21
 * @Description:
 */
@Service
@Slf4j
public class DailyTaskTransferStateAction implements StateAction {
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
        if (MapUtils.isEmpty(params) || Objects.isNull(params.get("userId"))) {
            log.error("转交后置动作处理异常，参数错误");
            return;
        }
        Long userId = (Long) params.get("userId");
        Set<Long> sourceUsers = (Set<Long>) params.get("sourceUsers");
        String sourceUserName = String.valueOf(params.get("sourceUserName"));
        Map<Long, String> beforeState = context.getBeforeState();
        for (BaseState base : dailyTaskPO) {
            DailyTaskPO po = (DailyTaskPO) base;
            //发送消息提醒
            Set<Long> userIds = Collections.singleton(userId);
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", po.getName());
            paramMap.put("userName", sourceUserName);
            List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
            MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
            notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
            notifyTarget.setIds(userIds);
            notifyTargets.add(notifyTarget);
            MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                    .tenant(po.getTenant())
                    .applicationCode(context.getBusinessTypeName())
                    .msgSceneCode(DAILY_TASK_TRANSFER.name())
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
            TodoTaskFlowCirculationMessage circulationMessage = new TodoTaskFlowCirculationMessage();
            circulationMessage.setBusinessKey(String.valueOf(po.getId()));
            circulationMessage.setBusinessStatus(beforeState.get(po.getId()));
            circulationMessage.setUserIds(sourceUsers);
            circulationMessage.setHandlerTime(LocalDateTime.now());
            circulationMessage.setTaskTime(LocalDateTime.now());
            circulationMessage.setGeneratedNextStatusTask(true);
            circulationMessage.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
            circulationMessage.setNextBusinessStatus(po.getStatus());
            circulationMessage.setNextBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
            circulationMessage.setNextUserIds(new ArrayList<>(userIds));
            //发消息
            RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, circulationMessage, String.valueOf(po.getId()));

            WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = new WorkTaskFlowCirculationMessage();
            workTaskFlowCirculationMessage.setBusinessKey(String.valueOf(po.getId()));
            workTaskFlowCirculationMessage.setBusinessStatus(beforeState.get(po.getId()));
            workTaskFlowCirculationMessage.setUserIds(sourceUsers);
            workTaskFlowCirculationMessage.setHandlerTime(LocalDateTime.now());
            workTaskFlowCirculationMessage.setTaskTime(LocalDateTime.now());
            workTaskFlowCirculationMessage.setGeneratedNextStatusTask(true);
            workTaskFlowCirculationMessage.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
            workTaskFlowCirculationMessage.setNextBusinessStatus(po.getStatus());
            workTaskFlowCirculationMessage.setNextBusinessStatusName(DailyTaskStateNodes.getStateNode(po.getStatus()).getStateName());
            workTaskFlowCirculationMessage.setNextUserIds(new ArrayList<>(userIds));
            //发消息
            RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage, String.valueOf(po.getId()));

        }

    }

    @Override
    public String getBusinessEvent() {
        return TRANSFER;
    }
}
