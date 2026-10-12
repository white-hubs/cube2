package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.UserExamTaskRecord;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.UserExamTaskPublicSubmittedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.UserExamTaskSubmittedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamSubmittedMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskRecordSubmittedVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserExamTaskSubmittedEventHandler {

    private final ExamTaskService examTaskService;

    private final ExamTaskUserService examTaskUserService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void updateExamTaskUserInfo(final UserExamTaskSubmittedEvent userExamTaskSubmittedEvent) {
        ExamTaskUserPO examTaskUserPO = this.examTaskUserService.getOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                .eq(ExamTaskUserPO::getExamTaskId, userExamTaskSubmittedEvent
                                                                                        .getUserExamTaskRecord().getExamTaskId())
                                                                                .eq(ExamTaskUserPO::getUserId, userExamTaskSubmittedEvent
                                                                                        .getUserExamTaskRecord().getExamTaskUserInfo()
                                                                                        .getId()));
        if (Objects.isNull(examTaskUserPO)) {
            return;
        }
        if (UserExamStatusEnum.STAY_EXAM == examTaskUserPO.getExamStatus()) {
            examTaskUserPO.setExamNumber(1);
            examTaskUserPO.setUserExamTaskRecordId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getId());
            examTaskUserPO.setExamStatus(UserExamStatusEnum.EXAMINED);
            examTaskUserPO.setPassed(userExamTaskSubmittedEvent.getUserExamTaskRecord().getPassed());
            examTaskUserPO.setScore(userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore());
            //待办消息发送
            this.sendUpdateTodoTask(userExamTaskSubmittedEvent);
        } else {
            examTaskUserPO.setExamNumber(examTaskUserPO.getExamNumber() + 1);
            if (userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore() > examTaskUserPO.getScore()) {
                examTaskUserPO.setUserExamTaskRecordId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getId());
                examTaskUserPO.setPassed(userExamTaskSubmittedEvent.getUserExamTaskRecord().getPassed());
                examTaskUserPO.setScore(userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore());
            }
        }
        userExamTaskSubmittedEvent.accept(getUserExamTaskRecordSubmittedVO(userExamTaskSubmittedEvent.getUserExamTaskRecord()));
        this.examTaskUserService.updateById(examTaskUserPO);
        this.sendUserExamSubmittedMessage(userExamTaskSubmittedEvent.getUserExamTaskRecord());
    }

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void updatePublicExamTaskUserInfo(final UserExamTaskPublicSubmittedEvent userExamTaskSubmittedEvent) {
        ExamTaskUserPO examTaskUserPO = this.examTaskUserService.getOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                .eq(ExamTaskUserPO::getExamTaskId, userExamTaskSubmittedEvent
                                                                                        .getUserExamTaskRecord().getExamTaskId())
                                                                                .eq(ExamTaskUserPO::getUserIdentify, userExamTaskSubmittedEvent
                                                                                        .getUserExamTaskRecord().getExamTaskUserInfo()
                                                                                        .getIdentify()));
        boolean isExecuteSave = true;
        if (Objects.nonNull(examTaskUserPO)) {
            isExecuteSave = false;
            examTaskUserPO.setExamNumber(examTaskUserPO.getExamNumber() + 1);
            if (userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore() > examTaskUserPO.getScore()) {
                examTaskUserPO.setUserExamTaskRecordId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getId());
                examTaskUserPO.setPassed(userExamTaskSubmittedEvent.getUserExamTaskRecord().getPassed());
                examTaskUserPO.setScore(userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore());
            }
        } else {
            ExamTaskUserInfo examTaskUserInfo = userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTaskUserInfo();
            examTaskUserPO = ExamTaskUserPO.builder()
                    .examTaskId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTaskId())
                    .userIdentify(examTaskUserInfo.getIdentify())
                    .userName(examTaskUserInfo.getName())
                    .userPhone(examTaskUserInfo.getPhone())
                    .userDepartmentName(examTaskUserInfo.getDepartmentName())
                    .userPostName(examTaskUserInfo.getPostName())
                    .examNumber(1)
                    .userExamTaskRecordId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getId())
                    .examStatus(UserExamStatusEnum.EXAMINED)
                    .passed(userExamTaskSubmittedEvent.getUserExamTaskRecord().getPassed())
                    .score(userExamTaskSubmittedEvent.getUserExamTaskRecord().getScore())
                    .build();
        }
        userExamTaskSubmittedEvent.accept(getUserExamTaskRecordSubmittedVO(userExamTaskSubmittedEvent.getUserExamTaskRecord()));
        if (isExecuteSave) {
            this.examTaskUserService.save(examTaskUserPO);
        } else {
            this.examTaskUserService.updateById(examTaskUserPO);
        }
        this.sendUserExamSubmittedMessage(userExamTaskSubmittedEvent.getUserExamTaskRecord());
    }

    private void sendUserExamSubmittedMessage(UserExamTaskRecord userExamTaskRecord) {
        try {
            ExamTaskPO examTaskPO = this.examTaskService.getById(userExamTaskRecord.getExamTaskId());
            RocketMQContextHolder.send(UserExamSubmittedMessage.TOPIC, UserExamSubmittedMessage.builder()
                    .id(examTaskPO.getId())
                    .name(examTaskPO.getName())
                    .sourceCode(examTaskPO.getSourceCode())
                    .sourceName(examTaskPO.getSourceName())
                    .sourceKey(examTaskPO.getSourceKey())
                    .userExamTaskRecord(userExamTaskRecord)
                    .tenant(examTaskPO.getTenant())
                    .build());
        } catch (Exception e) {
            throw new ServiceException("考试任务提交后发送消息失败,任务主键:" + userExamTaskRecord.getExamTaskId(), e);
        }
    }

    public UserExamTaskRecordSubmittedVO getUserExamTaskRecordSubmittedVO(UserExamTaskRecord userExamTaskRecord) {
        //题目数量
        Integer examPaperQuestionNumber = userExamTaskRecord.getExamPaper().getExamPaperQuestionCategoryInfos()
                .stream()
                .mapToInt(examPaperQuestionCategoryInfos -> examPaperQuestionCategoryInfos.getExamPaperQuestionInfos().size())
                .sum();
        //正确题目数量
        Integer passQuestionNumber = userExamTaskRecord.getExamPaper().getExamPaperQuestionCategoryInfos().stream()
                .mapToInt(examPaperQuestionCategoryInfo ->
                                  examPaperQuestionCategoryInfo.getExamPaperQuestionInfos().stream()
                                          .filter(increaseExamTaskUserRecordQuestionCommand ->
                                                          Objects.equals(Boolean.TRUE, increaseExamTaskUserRecordQuestionCommand.getAnswerState())
                                                          &&
                                                          Objects.equals(Boolean.TRUE, increaseExamTaskUserRecordQuestionCommand.getEditState()))
                                          .collect(Collectors.toList())
                                          .size()
                         ).sum();
        float passRatio = new BigDecimal(passQuestionNumber)
                .divide(new BigDecimal(examPaperQuestionNumber), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)).floatValue();
        return UserExamTaskRecordSubmittedVO.builder()
                .id(userExamTaskRecord.getId())
                .paperScore(userExamTaskRecord.getExamPaper().getExamPaperInfo().getScore())
                .passed(userExamTaskRecord.getPassed())
                .score(userExamTaskRecord.getScore())
                .passingRate(passRatio)
                .build();
    }

    /**
     * 用户考试完成待办消息发送
     *
     * @param userExamTaskSubmittedEvent
     */
    private void sendUpdateTodoTask(final UserExamTaskSubmittedEvent userExamTaskSubmittedEvent) {
        try {
            ExamTaskUserPO examTaskUserPO = this.examTaskUserService.getOne(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                    .eq(ExamTaskUserPO::getExamTaskId, userExamTaskSubmittedEvent
                                                                                            .getUserExamTaskRecord().getExamTaskId())
                                                                                    .eq(ExamTaskUserPO::getUserId, userExamTaskSubmittedEvent
                                                                                            .getUserExamTaskRecord().getExamTaskUserInfo()
                                                                                            .getId()));
            if (Objects.isNull(examTaskUserPO)) {
                return;
            }
            TodoTaskFlowCirculationMessage todoTaskFlowCirculationMessage = TodoTaskFlowCirculationMessage.builder()
                    .businessKey(examTaskUserPO.getId().toString())
                    .operationType(TodoTaskFlowOperationEnum.CHANGE)
                    .handlerTime(userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTime())
                    .userId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTaskUserInfo().getId())
                    .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                    .generatedNextStatusTask(false)
                    .nextBusinessStatus(UserExamStatusEnum.EXAMINED.name())
                    .nextBusinessStatusName(UserExamStatusEnum.EXAMINED.getDescription())
                    .build();
            RocketMQContextHolder.send(TodoTaskFlowCirculationMessage.TOPIC, todoTaskFlowCirculationMessage);
            sendUpdateWorkTask(examTaskUserPO, userExamTaskSubmittedEvent);
        } catch (Exception e) {
            //待办发送失败
            log.error("考试任务用户提交操作待办任务消息发送失败,任务主键:{}", userExamTaskSubmittedEvent.getUserExamTaskRecord()
                    .getExamTaskId());
        }
    }

    private void sendUpdateWorkTask(ExamTaskUserPO examTaskUserPO, final UserExamTaskSubmittedEvent userExamTaskSubmittedEvent) {
        WorkTaskFlowCirculationMessage workTaskFlowCirculationMessage = WorkTaskFlowCirculationMessage.builder()
                .businessKey(examTaskUserPO.getId().toString())
                .operationType(WorkTaskFlowOperationEnum.CHANGE)
                .handlerTime(userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTime())
                .userId(userExamTaskSubmittedEvent.getUserExamTaskRecord().getExamTaskUserInfo().getId())
                .businessStatus(UserExamStatusEnum.STAY_EXAM.name())
                .generatedNextStatusTask(false)
                .nextBusinessStatus(UserExamStatusEnum.EXAMINED.name())
                .nextBusinessStatusName(UserExamStatusEnum.EXAMINED.getDescription())
                .build();
        RocketMQContextHolder.send(WorkTaskFlowCirculationMessage.TOPIC, workTaskFlowCirculationMessage);
    }
}
