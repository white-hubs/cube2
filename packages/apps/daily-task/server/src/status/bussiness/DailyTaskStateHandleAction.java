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
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum.DAILY_TASK_COMPLETE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_HANDLE;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/25 14:46
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskStateHandleAction implements StateAction {
    private final MessageSendClient messageSendClient;
    private final UserClient userClient;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void beforeActionExecute(TransitionContext context) {
        DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
        dailyTaskService.updateTaskProgress(context);
    }

    @Override
    public void afterActionExecute(TransitionContext context) {
        List<BaseState> dailyTaskPO = context.getEntity();
        DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
        //        //获取父级任务进度
        //        List<DailyTaskPO> dailyTaskPOS = dailyTaskService.lambdaQuery()
        //                .like(DailyTaskPO::getSequence, dailyTaskPO.getFirst().getId())
        //                .eq(DailyTaskPO::getProgress, BigDecimal.ZERO)
        //                .list();
        //        if (CollectionUtils.isEmpty(dailyTaskPOS)) {
        //            return;
        //        }
        //        List<Long> parentTaskIds = dailyTaskPOS.stream().map(DailyTaskPO::getId).collect(Collectors.toList());
        Map<Long, Set<Long>> taskTargetUserMap = dailyTaskService.getTaskTargetUserMap(context.getTaskIds());
        if (MapUtils.isEmpty(taskTargetUserMap)) {
            return;
        }
        // 更新父任务进度，给父任务发待办任务消息

        UserQuery userQuery = new UserQuery();
        taskTargetUserMap.forEach((k, userIds) -> {
                                      if (CollectionUtils.isEmpty(userQuery.getUserIds())) {
                                          userQuery.setUserIds(new HashSet<>());
                                      }
                                      userQuery.getUserIds().addAll(userIds);
                                  }

                                 );
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        Map<Long, String> beforeState = context.getBeforeState();
        for (BaseState base : dailyTaskPO) {
            DailyTaskPO po = (DailyTaskPO) base;
            if (!DailyTaskStateNodes.TASK_STATE_NODE_DONE.getStateCode().equals(po.getStatus())) {
                continue;
            }
            //发送消息提醒
            if (Objects.equals(po.getId(), context.getTopTaskId())) {
                try {
                    Map<String, String> paramMap = new HashMap<>();
                    Set<Long> userIds = taskTargetUserMap.get(po.getId());
                    String userNames = userDTOS.stream().filter(userDTO -> userIds.contains(userDTO.getId()))
                            .map(UserDTO::getName).collect(Collectors.joining(","));
                    paramMap.put("userName", userNames);
                    paramMap.put("taskName", po.getName());
                    List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
                    MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
                    notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
                    notifyTarget.setIds(userIds);
                    notifyTargets.add(notifyTarget);
                    MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                            .tenant(po.getTenant())
                            .applicationCode(context.getBusinessTypeName())
                            .msgSceneCode(DAILY_TASK_COMPLETE.name())
                            .businessId(String.valueOf(po.getId()))
                            .tmpParams(paramMap)
                            .messageNotifyTargets(notifyTargets)
                            .build();
                    MessageSendResponse messageSendResponse = messageSendClient.sceneSend(message);
                    log.info("日常任务完成消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
                } catch (Exception e) {
                    log.error("日常任务完成消息发送失败", e);
                }
            }
            TodoTaskFlowCirculationMessage circulationMessage = new TodoTaskFlowCirculationMessage();
            circulationMessage.setBusinessKey(String.valueOf(po.getId()));
            circulationMessage.setBusinessStatus(beforeState.get(po.getId()));
            circulationMessage.setUserIds(taskTargetUserMap.get(po.getId()));
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
            workTaskFlowCirculationMessage.setUserIds(taskTargetUserMap.get(po.getId()));
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
        return TASK_HANDLE;
    }
}
