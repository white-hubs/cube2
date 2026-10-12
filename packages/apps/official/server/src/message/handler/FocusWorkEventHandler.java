package com.guhecloud.cube.srv.gen.official.management.server.message.handler;

import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.WorkTaskClient;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkMapper;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkUpdateStatus;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkDelayEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkDeleteEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkPassEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkReplyEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkUpdateEvent;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkEventHandler {
    private final FocusWorkMapper focusWorkMapper;
    private final MessageSendClient messageSendClient;
    private final MsgRecordClient msgRecordClient;
    private final TodoTaskClient todoTaskClient;
    private final WorkTaskClient workTaskClient;
    private final FlowClient flowClient;

    /**
     * 消息发送
     *
     * @param event
     */
    @EventListener
    public void sendMessage(final FocusWorkPassEvent event) {
        try {
            //参数信息
            String workDesc = event.getFocusWork().getWorkDesc();
            workDesc = workDesc.length() >= 20 ? workDesc.substring(0, 20) + "..." : workDesc;
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("workDesc", workDesc);
            //通知用户
            Set<Long> allUsers = this.getAllNotifyUsers(event.getFocusWork());
            if (!CollectionUtils.isEmpty(allUsers)) {
                this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                         .msgSceneCode("FOCUS_WORK_PASS_PUBLISHED_NOTIFY")
                                                         .businessId(event.getFocusWork().getId().toString())
                                                         .tmpParams(msgParams)
                                                         .tenant(SessionContextHolder.getRequester().getTenant())
                                                         .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                 .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                 .ids(allUsers)
                                                                                                                 .build()))
                                                         .build());
            }
        } catch (Exception e) {
            //消息发送失败
            log.error("督办事项审批通过推送失败,任务主键:{}", event.getFocusWork().getId(), e);
        }
    }

    @EventListener
    public void sendMessage(final FocusWorkUpdateEvent event) {
        try {
            FocusWorkPO focusWorkPO = focusWorkMapper.selectById(event.getFocusWork().getFocusWorkId());
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            String workDesc = focusWorkPO.getWorkDesc();
            workDesc = workDesc.length() >= 20 ? workDesc.substring(0, 20) + "..." : workDesc;
            msgParams.put("workName", workDesc);//任务类型名称
            msgParams.put("workStatus", FocusWorkUpdateStatus.valueOf(event.getFocusWork().getStatus()).getLabel());//预警名称
            msgParams.put("workOverTime", event.getFocusWork().getUpdateOverTimeStr());
            //查询要推送的人员
            Set<Long> allUsers = this.getAllNotifyUsers(focusWorkPO);
            if (!CollectionUtils.isEmpty(allUsers)) {
                String msgSceneCode;
                if (FocusWorkUpdateStatus.PROCESSING_DELAY.name().equals(event.getFocusWork().getStatus())) {
                    msgSceneCode = "FOCUS_WORK_UPDATE_DELAY_PUBLISHED_NOTIFY";
                } else {
                    msgSceneCode = "FOCUS_WORK_UPDATE_OVER_PUBLISHED_NOTIFY";
                }
                this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                         .msgSceneCode(msgSceneCode)
                                                         .businessId(event.getFocusWork().getId().toString())
                                                         .tmpParams(msgParams)
                                                         .tenant(SessionContextHolder.getRequester().getTenant())
                                                         .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                 .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                 .ids(allUsers)
                                                                                                                 .build()))
                                                         .build());
            }
        } catch (Exception e) {
            //消息发送失败
            log.error("督办事件更新推送失败,业务主键为:{}", event.getFocusWork().getId(), e);
        }
    }

    private Set<Long> getAllNotifyUsers(FocusWorkPO focusWorkPO) {
        Set<Long> allUsers = new HashSet<>();
        if (!StringUtils.isEmpty(focusWorkPO.getFirstUserId())) {
            Set<Long> userIds = Arrays.stream(focusWorkPO.getFirstUserId().split(","))
                    .map(Long::parseLong).collect(Collectors.toSet());
            allUsers.addAll(userIds);
        }
        if (!StringUtils.isEmpty(focusWorkPO.getSecondUserId())) {
            Set<Long> userIds = Arrays.stream(focusWorkPO.getSecondUserId().split(","))
                    .map(Long::parseLong).collect(Collectors.toSet());
            allUsers.addAll(userIds);
        }
        return allUsers;
    }

    @EventListener
    public void sendMessage(final FocusWorkDelayEvent event) {
        try {
            //参数信息
            String workDesc = event.getFocusWork().getWorkDesc();
            workDesc = workDesc.length() >= 20 ? workDesc.substring(0, 20) + "..." : workDesc;
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("workDesc", workDesc);
            float percentage = event.getFocusWork().getPercentage() == null ? 0 : event.getFocusWork().getPercentage();
            msgParams.put("percentage", String.valueOf(percentage));
            msgParams.put("overtime", event.getOverTime().toString());
            //通知用户
            Set<Long> allUsers = this.getAllNotifyUsers(event.getFocusWork());
            if (!CollectionUtils.isEmpty(allUsers)) {
                //场景code
                String msgSceneCode;
                if (event.getOverTime() > 0) {//即将超时
                    msgSceneCode = "FOCUS_WORK_WILL_OVERTIME_PUBLISHED_NOTIFY";
                } else {//超时
                    msgSceneCode = "FOCUS_WORK_OVERTIME_PUBLISHED_NOTIFY";
                }
                this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                         .msgSceneCode(msgSceneCode)
                                                         .businessId(event.getFocusWork().getId().toString())
                                                         .tmpParams(msgParams)
                                                         .tenant(SessionContextHolder.getRequester().getTenant())
                                                         .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                 .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                 .ids(allUsers)
                                                                                                                 .build()))
                                                         .build());
            }
        } catch (Exception e) {
            //消息发送失败
            log.error("督办事项审批通过推送失败,任务主键:{}", event.getFocusWork().getId(), e);
        }
    }

    /**
     * 发送待办任务消息
     *
     * @param event
     */
    @EventListener
    public void sendCreateTodoTaskMessage(final FocusWorkPassEvent event) {
        String workDesc = event.getFocusWork().getWorkDesc();
        String finalWorkDesc = workDesc.length() >= 20 ? workDesc.substring(0, 20) + "..." : workDesc;
        //通知用户
        Set<Long> allUsers = this.getAllNotifyUsers(event.getFocusWork());
        if (!CollectionUtils.isEmpty(allUsers)) {
            allUsers.stream().forEach(userId -> {
                try {
                    String businessKey = event.getFocusWork().getId() + "_" + userId;
                    TodoTaskInitMessage todoTaskInitMessage = TodoTaskInitMessage.builder()
                            .businessKey(businessKey)
                            .operationType(TodoTaskFlowOperationEnum.INSERT)
                            .taskName(finalWorkDesc)
                            .taskTime(event.getFocusWork().getCreateTime())
                            .applicationCode("OFFICIAL_MANAGEMENT")
                            .applicationName("行政管理")
                            .businessType("OFFICIAL_FOCUS_WORK_REPLY")
                            .businessTypeName("督办事项反馈")
                            .businessStatus(FocusWorkStatus.PROCESSING.name())
                            .businessStatusName(FocusWorkStatus.PROCESSING.getLabel())
                            .userIds(Arrays.asList(userId))
                            .businessPrincipal(JsonUtils.serialize(event.getFocusWork()))
                            .build();
                    RocketMQContextHolder.send(TodoTaskInitMessage.TOPIC, todoTaskInitMessage);
                    sendCreateWorkTaskMessage(businessKey, finalWorkDesc, event, userId);
                } catch (Exception e) {
                    //待办发送失败
                    log.error("行政管理督办反馈，待办任务消息发送失败,任务主键:{}", event.getFocusWork().getId(), e);
                }
            });
        }
    }

    /**
     * 发送待办任务消息
     */
    public void sendCreateWorkTaskMessage(String businessKey, String finalWorkDesc, FocusWorkPassEvent event, Long userId) {
        WorkTaskInitMessage todoTaskInitMessage = WorkTaskInitMessage.builder()
                .businessKey(businessKey)
                .operationType(WorkTaskFlowOperationEnum.INSERT)
                .taskName(finalWorkDesc)
                .taskTime(event.getFocusWork().getCreateTime())
                .applicationCode("OFFICIAL_MANAGEMENT")
                .applicationName("行政管理")
                .businessType("OFFICIAL_FOCUS_WORK_REPLY")
                .businessTypeName("督办事项反馈")
                .businessStatus(FocusWorkStatus.PROCESSING.name())
                .businessStatusName(FocusWorkStatus.PROCESSING.getLabel())
                .userIds(Arrays.asList(userId))
                .businessPrincipal(JsonUtils.serialize(event.getFocusWork()))
                .build();
        RocketMQContextHolder.send(WorkTaskInitMessage.TOPIC, todoTaskInitMessage);
    }

    /**
     * @param replyEvent
     */
    @EventListener
    public void sendUpdateTodoTask(final FocusWorkReplyEvent replyEvent) {
        String status = replyEvent.getFocusWork().getStatus();
        try {
            String businessKey = replyEvent.getFocusWork().getId() + "_" + replyEvent.getFocusWorkReply().getApplicantId();
            TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                    .businessKey(businessKey)
                    .operationType(TodoTaskFlowOperationEnum.CHANGE)
                    .handlerTime(replyEvent.getFocusWorkReply().getApplyTime())
                    .userId(replyEvent.getFocusWorkReply().getApplicantId())
                    .businessStatus(FocusWorkStatus.PROCESSING.name())
                    .generatedNextStatusTask(false)
                    .nextBusinessStatus(FocusWorkStatus.valueOf(status).name())
                    .nextBusinessStatusName(FocusWorkStatus.valueOf(status).getLabel())
                    .build();
            RocketMQContextHolder.send(TodoTaskFlowCirculationMessage.TOPIC, todoTaskFlowCirculationMessage);
            sendUpdateWorkTaskMsg(businessKey, status, replyEvent);
        } catch (Exception e) {
            //待办发送失败
            log.error("行政管理督办反馈，待办任务状态更新消息发送失败,任务主键:{}", replyEvent.getFocusWorkReply().getId(), e);
        }
    }

    private void sendUpdateWorkTaskMsg(String businessKey, String status, FocusWorkReplyEvent replyEvent) {
        WorkTaskFlowCirculationMessage todoTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .businessKey(businessKey)
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .handlerTime(replyEvent.getFocusWorkReply().getApplyTime())
                .userId(replyEvent.getFocusWorkReply().getApplicantId())
                .businessStatus(FocusWorkStatus.PROCESSING.name())
                .generatedNextStatusTask(false)
                .nextBusinessStatus(FocusWorkStatus.valueOf(status).name())
                .nextBusinessStatusName(FocusWorkStatus.valueOf(status).getLabel())
                .build();
        RocketMQContextHolder.send(WorkTaskFlowCirculationMessage.TOPIC, todoTaskFlowCirculationMessage);
    }

    @EventListener
    public void removeTodoTask(final FocusWorkDeleteEvent event) {
        Set<String> ids = event.getIds().stream().map(String::valueOf)
                .collect(Collectors.toSet());
        todoTaskClient.removeBatchByBusinessIds(ids);
        workTaskClient.removeBatchByBusinessIds(ids);
    }

    @EventListener
    public void removeMessage(final FocusWorkDeleteEvent event) {
        Set<String> ids = event.getIds().stream().map(String::valueOf)
                .collect(Collectors.toSet());
        //删除消息
        msgRecordClient.removeBatchByBusinessIds(ids);
    }

    @EventListener
    public void removeFlow(final FocusWorkDeleteEvent event) {
        flowClient.deleteFormFlow(event.getProcessInstanceId());
    }
}
