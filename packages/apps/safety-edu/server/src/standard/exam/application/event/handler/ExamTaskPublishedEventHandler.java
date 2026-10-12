package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskPublishedEventHandler {

    private final ExamTaskMapper examTaskMapper;

    private final ExamTaskUserService examTaskUserService;

    private final ThemeService themeService;

    private final UserClient userClient;

    private final MessageSendClient messageSendClient;

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    public void createExamTaskPaper(final ExamTaskPublishedEvent examTaskPublishedEvent) {
        ExamTask examTask = examTaskPublishedEvent.getExamTask();
        //更改任务状态
        ExamTaskPO examTaskPO = examTaskMapper.selectById(examTask.getId());
        if (!examTask.getExamTaskDateInfo().getStartTime().isAfter(LocalDateTime.now())) {
            examTaskPO.setExamTaskStatus(ExamTaskStatusEnum.IN_PROGRESS);
        }
        //负责人信息
        UserDTO userDTO = this.userClient.getUser(examTaskPO.getChargeUserId());
        if (Objects.nonNull(userDTO)) {
            examTaskPO.setChargeUserName(userDTO.getName());
        }
        //主题信息
        ThemePO themePO = this.themeService.getById(examTask.getThemeInfo().getId());
        if (Objects.nonNull(themePO)) {
            examTaskPO.setThemeName(themePO.getName());
        }
        this.examTaskMapper.updateById(examTaskPO);
    }

    /**
     * 负责人消息发送
     *
     * @param examTaskPublishedEvent
     */
    @EventListener
    public void sendTaskChargeNotifyMessage(final ExamTaskPublishedEvent examTaskPublishedEvent) {
        try {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("examTaskName", examTaskPublishedEvent.getExamTask().getExamTaskInfo().getName());
            msgParams.put("examStartTime", DateUtils.format(examTaskPublishedEvent.getExamTask().getExamTaskDateInfo()
                                                                    .getStartTime()));
            messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                .msgSceneCode("EXAM_TASK_PUBLISHED_CHARGE_NOTIFY")
                                                .businessId(examTaskPublishedEvent.getExamTask().getId().toString())
                                                .tmpParams(msgParams)
                                                .tenant(SessionContextHolder.getRequester().getTenant())
                                                .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                        .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                        .ids(Collections.singleton(examTaskPublishedEvent
                                                                                                                                           .getExamTask()
                                                                                                                                           .getExamTaskChargeUserInfo()
                                                                                                                                           .getId()))
                                                                                                        .build()))
                                                .build());
        } catch (Exception e) {
            //消息发送失败
            log.error("考试任务发布负责人通知消息发送失败,任务主键:{}", examTaskPublishedEvent.getExamTask().getId(), e);
        }
    }

    /**
     * 考试人员消息发送
     *
     * @param examTaskPublishedEvent
     */
    @EventListener
    public void sendTaskUserNotifyMessage(final ExamTaskPublishedEvent examTaskPublishedEvent) {
        try {
            //参数信息
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("examTaskName", examTaskPublishedEvent.getExamTask().getExamTaskInfo().getName());
            msgParams.put("examStartTime", DateUtils.format(examTaskPublishedEvent.getExamTask().getExamTaskDateInfo()
                                                                    .getStartTime()));
            if (CollectionUtils.isEmpty(examTaskPublishedEvent.getExamTask().getExamTaskUserInfos())) {
                return;
            }
            messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                .msgSceneCode("EXAM_TASK_PUBLISHED_USER_NOTIFY")
                                                .businessId(examTaskPublishedEvent.getExamTask().getId().toString())
                                                .tmpParams(msgParams)
                                                .tenant(SessionContextHolder.getRequester().getTenant())
                                                .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                        .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                        .ids(examTaskPublishedEvent
                                                                                                                     .getExamTask()
                                                                                                                     .getExamTaskUserInfos()
                                                                                                                     .stream()
                                                                                                                     .map(ExamTaskUserInfo::getId)
                                                                                                                     .collect(Collectors.toSet()))
                                                                                                        .build()))
                                                .build());
        } catch (Exception e) {
            //消息发送失败
            log.error("考试任务发布考试人员通知消息发送失败,任务主键:{}", examTaskPublishedEvent.getExamTask().getId(), e);
        }
    }

    /**
     * 发送待办任务消息
     *
     * @param examTaskPublishedEvent
     */
    @EventListener
    public void sendCreateTodoTaskMessage(final ExamTaskPublishedEvent examTaskPublishedEvent) {
        try {
            List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                         .eq(ExamTaskUserPO::getExamTaskId, examTaskPublishedEvent
                                                                                                 .getExamTask().getId()));
            examTaskUserPOS.stream().forEach(examTaskUserInfo -> {
                TodoTaskInitMessage todoTaskInitMessage = TodoTaskInitMessage.builder()
                        .businessKey(examTaskUserInfo.getId().toString())
                        .operationType(TodoTaskFlowOperationEnum.INSERT)
                        .taskName(examTaskPublishedEvent.getExamTask().getExamTaskInfo().getName())
                        .taskTime(examTaskPublishedEvent.getExamTask().getExamTaskDateInfo().getStartTime())
                        .applicationCode("SECURE_EDU")
                        .applicationName("安全教育")
                        .businessType("EXAM_TASK")
                        .businessTypeName("考试任务")
                        .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                        .businessStatusName(UserExamStatusEnum.STAY_EXAM.getDescription())
                        .userIds(Collections.singletonList(examTaskUserInfo.getUserId()))
                        .businessPrincipal(JsonUtils.serialize(examTaskPublishedEvent.getExamTask()))
                        .build();
                RocketMQContextHolder.send(TodoTaskInitMessage.TOPIC, todoTaskInitMessage);
                sendCreateWorkTaskMessage(examTaskUserInfo, examTaskPublishedEvent);
            });
        } catch (Exception e) {
            //待办发送失败
            log.error("考试任务发布操作待办任务消息发送失败,任务主键:{}", examTaskPublishedEvent.getExamTask().getId());
        }
    }

    private void sendCreateWorkTaskMessage(ExamTaskUserPO examTaskUserInfo, final ExamTaskPublishedEvent examTaskPublishedEvent) {
        WorkTaskInitMessage workTaskInitMessage = WorkTaskInitMessage.builder()
                .businessKey(examTaskUserInfo.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.INSERT)
                .taskName(examTaskPublishedEvent.getExamTask().getExamTaskInfo().getName())
                .taskTime(examTaskPublishedEvent.getExamTask().getExamTaskDateInfo().getStartTime())
                .applicationCode("SECURE_EDU")
                .applicationName("安全教育")
                .businessType("EXAM_TASK")
                .businessTypeName("考试任务")
                .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                .businessStatusName(UserExamStatusEnum.STAY_EXAM.getDescription())
                .userIds(Collections.singletonList(examTaskUserInfo.getUserId()))
                .businessPrincipal(JsonUtils.serialize(examTaskPublishedEvent.getExamTask()))
                .build();
        RocketMQContextHolder.send(WorkTaskInitMessage.TOPIC, workTaskInitMessage);
    }
}
