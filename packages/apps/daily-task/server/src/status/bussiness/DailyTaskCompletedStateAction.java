package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum.DAILY_TASK_COMPLETE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_COMPLETE;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/18 10:57
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskCompletedStateAction implements StateAction {
    private final UserClient userClient;
    private final AssemblyNoticeClient assemblyNoticeClient;

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
        DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
        Map<Long, Set<Long>> taskTargetUserMap = dailyTaskService.getTaskTargetUserMap(context.getTaskIds());
        Map<String, Object> params = context.getParams();
        if (MapUtils.isEmpty(taskTargetUserMap)) {
            return;
        }
        if (MapUtils.isEmpty(params) || Objects.isNull(params.get("noCompleteTargetNum"))) {
            log.error("转交后置动作处理异常，参数错误");
            return;
        }
        // 更新父任务进度，给父任务发待办任务消息
        Integer noCompleteTargetNum = (Integer) params.get("noCompleteTargetNum");
        UserQuery userQuery = new UserQuery();
        taskTargetUserMap.forEach((k, userIds) -> {
                                      if (CollectionUtils.isEmpty(userQuery.getUserIds())) {
                                          userQuery.setUserIds(new HashSet<>());
                                      }
                                      userQuery.getUserIds().addAll(userIds);
                                  }

                                 );
        List<UserDTO> userDTOS = userClient.postList(userQuery);
        DailyTaskPO baseState = (DailyTaskPO) dailyTaskPO.getFirst();
        var messageSendConfigs = assemblyNoticeClient
                .getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                          .businessType(baseState.getDailyType())
                                          .sceneCodes(Collections.singletonList(DAILY_TASK_COMPLETE.name()))
                                          .build());
        MessageSendConfig messageSendConfig = CollectionUtils.isNotEmpty(messageSendConfigs) ? messageSendConfigs.getFirst() : null;
        Map<Long, String> beforeState = context.getBeforeState();
        for (BaseState base : dailyTaskPO) {
            DailyTaskPO po = (DailyTaskPO) base;
            //发送消息提醒
            if (Objects.nonNull(messageSendConfig)) {
                try {
                    Map<String, String> paramMap = new HashMap<>();
                    Set<Long> userIds = taskTargetUserMap.get(po.getId());
                    String userNames = userDTOS.stream().filter(userDTO -> userIds.contains(userDTO.getId()))
                            .map(UserDTO::getName).collect(Collectors.joining(","));
                    paramMap.put("userName", userNames);
                    paramMap.put("taskName", po.getName());
                    paramMap.put("number", noCompleteTargetNum.toString());
                    MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                                        .businessId(String.valueOf(po.getId()))
                                                                                        .notifyId(messageSendConfig.getNotifyId())
                                                                                        .msgSceneCode(messageSendConfig.getSceneCode())
                                                                                        .tempParams(paramMap)
                                                                                        .build());
                    log.info("日常任务消息发送结果:{}", JsonUtils.serialize(response));
                } catch (Exception e) {
                    log.error("日常任务下发消息发送失败", e);
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
        return TASK_COMPLETE;
    }
}
