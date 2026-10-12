package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.handler;

import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.TrainStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.CultivateUnSignMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.CultivateTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.UserCultivateOverEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event.UserCultivateSignEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskEventHandler {

    private final MessageSendClient messageSendClient;

    /**
     * 负责人消息发送
     *
     * @param taskPublishedEvent
     */
    @EventListener
    public void sendChargeNotifyMessage(final CultivateTaskPublishedEvent taskPublishedEvent) {
        try {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("cultivateTaskName", taskPublishedEvent.getMessage().getTask().getName());
            msgParams.put("cultivateStartTime", DateUtils.format(taskPublishedEvent.getMessage().getTask().getCultivateStartTime()));
            this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                     .msgSceneCode("CULTIVATE_TASK_PUBLISHED_CHARGE_NOTIFY")
                                                     .businessId(taskPublishedEvent.getMessage().getTask().getId().toString())
                                                     .tmpParams(msgParams)
                                                     .tenant(SessionContextHolder.getRequester().getTenant())
                                                     .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                             .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                             .ids(Collections.singleton(taskPublishedEvent
                                                                                                                                                .getMessage()
                                                                                                                                                .getTask()
                                                                                                                                                .getChargeUserId()))
                                                                                                             .build()))
                                                     .build());
        } catch (Exception e) {
            //消息发送失败
            log.error("培训任务发布操作，负责人通知消息发送失败,任务主键:{}", taskPublishedEvent.getMessage().getTask().getId(), e);
        }
    }

    /**
     * 培训人消息发送
     *
     * @param taskPublishedEvent
     */
    @EventListener
    public void sendUserNotifyMessage(final CultivateTaskPublishedEvent taskPublishedEvent) {
        try {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("cultivateTaskName", taskPublishedEvent.getMessage().getTask().getName());
            msgParams.put("cultivateStartTime", DateUtils.format(taskPublishedEvent.getMessage().getTask()
                                                                         .getCultivateStartTime()));
            if (CollectionUtils.isEmpty(taskPublishedEvent.getMessage().getUsers())) {
                return;
            }
            this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                     .msgSceneCode("CULTIVATE_TASK_PUBLISHED_USER_NOTIFY")
                                                     .businessId(taskPublishedEvent.getMessage().getTask().getId().toString())
                                                     .tmpParams(msgParams)
                                                     .tenant(SessionContextHolder.getRequester().getTenant())
                                                     .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                             .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                             .ids(taskPublishedEvent
                                                                                                                          .getMessage()
                                                                                                                          .getUsers()
                                                                                                                          .stream()
                                                                                                                          .map(CultivateTaskUser::getUserId)
                                                                                                                          .collect(Collectors.toSet()))
                                                                                                             .build()))
                                                     .build());
        } catch (Exception e) {
            //消息发送失败
            log.error("培训任务发布操作，培训人通知消息发送失败,任务主键:{}", taskPublishedEvent.getMessage().getTask().getId(), e);
        }
    }

    /**
     * 发送待办任务消息
     *
     * @param taskPublishedEvent
     */
    @EventListener
    public void sendCreateTodoTaskMessage(final CultivateTaskPublishedEvent taskPublishedEvent) {
        if (!CollectionUtils.isEmpty(taskPublishedEvent.getMessage().getUsers())) {
            taskPublishedEvent.getMessage().getUsers().stream().forEach(user -> {
                try {
                    TodoTaskInitMessage todoTaskInitMessage = TodoTaskInitMessage.builder()
                            .businessKey(user.getId().toString())
                            .operationType(TodoTaskFlowOperationEnum.INSERT)
                            .taskName(taskPublishedEvent.getMessage().getTask().getName())
                            .taskTime(taskPublishedEvent.getMessage().getTask().getCultivateStartTime())
                            .applicationCode("SECURE_EDU")
                            .applicationName("安全教育")
                            .businessType("CULTIVATE_TASK")
                            .businessTypeName("培训任务")
                            .businessStatus(TrainStatusEnum.PUBLISHED.name())
                            .businessStatusName(TrainStatusEnum.PUBLISHED.getDesc())
                            .userIds(Arrays.asList(user.getUserId()))
                            .businessPrincipal(JsonUtils.serialize(taskPublishedEvent.getMessage()))
                            .build();
                    RocketMQContextHolder.send(TodoTaskInitMessage.TOPIC, todoTaskInitMessage);
                    sendCreateWorkTaskMessage(taskPublishedEvent, user);
                } catch (Exception e) {
                    //待办发送失败
                    log.error("培训任务发布操作，待办任务消息发送失败,任务主键:{}", taskPublishedEvent.getMessage().getTask().getId(), e);
                }
            });
        }
    }

    private void sendCreateWorkTaskMessage(CultivateTaskPublishedEvent taskPublishedEvent, CultivateTaskUser user) {
        WorkTaskInitMessage workTaskInitMessage = WorkTaskInitMessage.builder()
                .businessKey(user.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.INSERT)
                .taskName(taskPublishedEvent.getMessage().getTask().getName())
                .taskTime(taskPublishedEvent.getMessage().getTask().getCultivateStartTime())
                .applicationCode("SECURE_EDU")
                .applicationName("安全教育")
                .businessType("CULTIVATE_TASK")
                .businessTypeName("培训任务")
                .businessStatus(TrainStatusEnum.PUBLISHED.name())
                .businessStatusName(TrainStatusEnum.PUBLISHED.getDesc())
                .userIds(Arrays.asList(user.getUserId()))
                .businessPrincipal(JsonUtils.serialize(taskPublishedEvent.getMessage()))
                .build();
        RocketMQContextHolder.send(WorkTaskInitMessage.TOPIC, workTaskInitMessage);
    }

    @EventListener
    public void sendUpdateTodoTask(final UserCultivateSignEvent signEvent) {
        try {
            if (Objects.nonNull(signEvent.getMessage().getUser().getUserId())) {
                TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                        .businessKey(signEvent.getMessage().getUser().getId().toString())
                        .operationType(TodoTaskFlowOperationEnum.CHANGE)
                        .handlerTime(LocalDateTime.now())
                        .userId(signEvent.getMessage().getUser().getUserId())
                        .businessStatus(TrainStatusEnum.PUBLISHED.name())
                        .generatedNextStatusTask(false)
                        .nextBusinessStatus(TrainStatusEnum.OVER.name())
                        .nextBusinessStatusName(TrainStatusEnum.OVER.getDesc())
                        .build();
                RocketMQContextHolder.send(TodoTaskFlowCirculationMessage.TOPIC, todoTaskFlowCirculationMessage);
                sendUpdateWorkTask(signEvent);
            }
        } catch (Exception e) {
            //待办发送失败
            log.error("培训任务，用户签到操作待办任务消息发送失败,任务主键:{}", signEvent.getMessage().getTask().getId(), e);
        }
    }

    private void sendUpdateWorkTask(final UserCultivateSignEvent signEvent) {
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .businessKey(signEvent.getMessage().getUser().getId().toString())
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .handlerTime(LocalDateTime.now())
                .userId(signEvent.getMessage().getUser().getUserId())
                .businessStatus(TrainStatusEnum.PUBLISHED.name())
                .generatedNextStatusTask(false)
                .nextBusinessStatus(TrainStatusEnum.OVER.name())
                .nextBusinessStatusName(TrainStatusEnum.OVER.getDesc())
                .build();
        RocketMQContextHolder.send(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage);
    }

    @EventListener
    public void sendUserSignMqMessage(final UserCultivateSignEvent signEvent) {
        try {
            RocketMQContextHolder.send(signEvent.getMessage().TOPIC, signEvent.getMessage());
        } catch (Exception e) {
            throw new ServiceException("培训任务，用户签到消息发送失败,任务主键:" + signEvent.getMessage().getTask().getId(), e);
        }
    }

    @EventListener
    public void sendUpdateTodoTask(final UserCultivateOverEvent overEvent) {
        if (!CollectionUtils.isEmpty(overEvent.getTaskUserList())) {
            overEvent.getTaskUserList().forEach(taskUser -> {
                try {
                    TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                            .businessKey(taskUser.getId().toString())
                            .operationType(TodoTaskFlowOperationEnum.CHANGE)
                            .handlerTime(LocalDateTime.now())
                            .userId(taskUser.getUserId())
                            .businessStatus(TrainStatusEnum.PUBLISHED.name())
                            .generatedNextStatusTask(false)
                            .nextBusinessStatus(TrainStatusEnum.OVER.name())
                            .nextBusinessStatusName(TrainStatusEnum.OVER.getDesc())
                            .build();
                    RocketMQContextHolder.send(TodoTaskFlowCirculationMessage.TOPIC, todoTaskFlowCirculationMessage);
                    sendUpdateWorkTask(taskUser);
                } catch (Exception e) {
                    //待办发送失败
                    log.error("培训任务，用户签到操作待办任务消息发送失败,任务主键:{}", taskUser.getId(), e);
                }
            });
        }
    }

    private void sendUpdateWorkTask(CultivateTaskUser taskUser) {
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .businessKey(taskUser.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .handlerTime(LocalDateTime.now())
                .userId(taskUser.getUserId())
                .businessStatus(TrainStatusEnum.PUBLISHED.name())
                .generatedNextStatusTask(false)
                .nextBusinessStatus(TrainStatusEnum.OVER.name())
                .nextBusinessStatusName(TrainStatusEnum.OVER.getDesc())
                .build();
        RocketMQContextHolder.send(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage);
    }

    @EventListener
    public void sendUnSignMqMessage(final UserCultivateOverEvent overEvent) {
        if (!CollectionUtils.isEmpty(overEvent.getTaskUserList())) {
            try {
                CultivateUnSignMessage unSignMessage = CultivateUnSignMessage.builder()
                        .task(overEvent.getCultivateTask())
                        .users(overEvent.getTaskUserList())
                        .build();
                RocketMQContextHolder.send(CultivateUnSignMessage.TOPIC, unSignMessage);
            } catch (Exception e) {
                //待办发送失败
                throw new ServiceException("培训任务，任务结束未签到用户消息发送失败,任务主键:" + overEvent.getCultivateTask().getId(), e);
            }
        }
    }
}