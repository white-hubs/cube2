package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskDelMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskDelMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserRecordService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskEndEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskWithdrawEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserRecordPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamMissMessage;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskWithdrawEventHandler {

    private final ExamTaskUserRecordService examTaskUserRecordService;

    private final ExamTaskPaperService examTaskPaperService;

    private final ExamTaskService examTaskService;

    private final ExamTaskUserService examTaskUserService;

    /**
     * 同步删除待考试状态已提交的考试记录与试卷信息
     *
     * @param examTaskWithdrawEvent
     */
    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTaskUserRecord(final ExamTaskWithdrawEvent examTaskWithdrawEvent) {
        List<ExamTaskUserRecordPO> examTaskUserRecordPOS = this.examTaskUserRecordService.list(Wrappers.<ExamTaskUserRecordPO>lambdaQuery()
                                                                                                       .eq(ExamTaskUserRecordPO::getExamTaskId, examTaskWithdrawEvent
                                                                                                               .getExamTask().getId()));
        if (!CollectionUtils.isEmpty(examTaskUserRecordPOS)) {
            DatabaseContextHolder.softDelete(ExamTaskUserRecordPO.class, examTaskUserRecordPOS);
        }
    }

    /**
     * 同步删除待考试状态已提交的考试记录与试卷信息
     *
     * @param examTaskWithdrawEvent
     */
    @EventListener
    public void sendDeleteTodoTaskMessage(final ExamTaskWithdrawEvent examTaskWithdrawEvent) {
        //待办消息发送(删除之前发布出去的待办任务)
        try {
            List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                         .eq(ExamTaskUserPO::getExamTaskId, examTaskWithdrawEvent
                                                                                                 .getExamTask().getId()));
            examTaskUserPOS.stream().forEach(examTaskUserPO -> {
                TodoTaskDelMessage todoTaskDelMessage = TodoTaskDelMessage.builder()
                        .businessKey(examTaskUserPO.getId().toString())
                        .operationType(TodoTaskFlowOperationEnum.DELETE)
                        .build();
                RocketMQContextHolder.send(TodoTaskDelMessage.TOPIC, todoTaskDelMessage);
                sendDeleteWorkTaskMessage(examTaskUserPO);
            });
        } catch (Exception e) {
            //待办发送失败
            log.error("考试任务撤回操作待办任务消息发送失败,任务主键:{}", examTaskWithdrawEvent.getExamTask().getId());
        }
    }

    private void sendDeleteWorkTaskMessage(ExamTaskUserPO examTaskUserPO) {
        WorkTaskDelMessage workTaskDelMessage = WorkTaskDelMessage.builder()
                .businessKey(examTaskUserPO.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.DELETE)
                .build();
        RocketMQContextHolder.send(WorkTaskDelMessage.TOPIC, workTaskDelMessage);
    }

    @EventListener
    public void sendEndTodoTaskMessage(final ExamTaskEndEvent examTaskEndEvent) {
        //待办消息发送(结束之前发布出去的待办任务)
        try {
            examTaskEndEvent.getExamTaskUserPOS().forEach(examTaskUserPO -> {
                TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                        .businessKey(examTaskUserPO.getId().toString())
                        .operationType(TodoTaskFlowOperationEnum.CHANGE)
                        .handlerTime(LocalDateTime.now())
                        .userId(examTaskUserPO.getUserId())
                        .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                        .generatedNextStatusTask(false)
                        .nextBusinessStatus(ExamTaskStatusEnum.ENDED.name())
                        .nextBusinessStatusName(ExamTaskStatusEnum.ENDED.getDescription())
                        .build();
                RocketMQContextHolder.send(TodoTaskDelMessage.TOPIC, todoTaskFlowCirculationMessage);
                sendEndWorkTaskMessage(examTaskUserPO);
            });
        } catch (Exception e) {
            //待办发送失败
            log.error("考试任务结束操作待办任务消息发送失败,任务主键:{}", examTaskEndEvent.getExamTaskId());
        }
    }

    private void sendEndWorkTaskMessage(final ExamTaskUserPO examTaskUserPO) {
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .businessKey(examTaskUserPO.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .handlerTime(LocalDateTime.now())
                .userId(examTaskUserPO.getUserId())
                .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                .generatedNextStatusTask(false)
                .nextBusinessStatus(ExamTaskStatusEnum.ENDED.name())
                .nextBusinessStatusName(ExamTaskStatusEnum.ENDED.getDescription())
                .build();
        RocketMQContextHolder.send(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage);
    }

    @EventListener
    public void sendEndMqMessage(final ExamTaskEndEvent examTaskEndEvent) {
        //待办消息发送(结束之前发布出去的待办任务)
        ExamTaskPO examTaskPO = this.examTaskService.getById(examTaskEndEvent.getExamTaskId());
        try {
            RocketMQContextHolder.send(UserExamMissMessage.TOPIC, UserExamMissMessage.builder()
                    .id(examTaskPO.getId())
                    .name(examTaskPO.getName())
                    .sourceCode(examTaskPO.getSourceCode())
                    .sourceName(examTaskPO.getSourceName())
                    .sourceKey(examTaskPO.getSourceKey())
                    .users(examTaskEndEvent.getExamTaskUserPOS())
                    .tenant(examTaskPO.getTenant())
                    .build());
        } catch (Exception e) {
            throw new ServiceException("考试任务结束后发送消息失败,任务主键:" + examTaskPO.getId(), e);
        }
    }
}
