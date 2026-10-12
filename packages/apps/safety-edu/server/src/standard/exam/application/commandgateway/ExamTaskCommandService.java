package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.PublishedExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.WithdrawExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.ChangeExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.DeleteExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.IncreaseExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.PublishedExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.PublishedPublicExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.validator.WithdrawExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event.ExamTaskWithdrawEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.factory.ExamTaskFactory;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.ExamTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskCommandService {

    private final ExamTaskRepository examTaskRepository;

    private final IncreaseExamTaskValidator increaseExamTaskValidator;

    private final ChangeExamTaskValidator changeExamTaskValidator;

    private final PublishedExamTaskValidator publishedExamTaskValidator;

    private final PublishedPublicExamTaskValidator publishedPublicExamTaskValidator;

    private final WithdrawExamTaskValidator withdrawExamTaskValidator;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final DeleteExamTaskValidator deleteExamTaskValidator;

    /**
     * 任务新增
     *
     * @param increaseExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseExamTask(IncreaseExamTaskCommand increaseExamTaskCommand) {
        //新增前置校验
        this.increaseExamTaskValidator.verify(increaseExamTaskCommand);
        ExamTask examTask = ExamTaskFactory.newExamTask(increaseExamTaskCommand);
        this.examTaskRepository.save(examTask);
    }

    /**
     * 任务编辑
     *
     * @param changeExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeExamTask(ChangeExamTaskCommand changeExamTaskCommand) {
        //编辑校验
        this.changeExamTaskValidator.verify(changeExamTaskCommand);
        ExamTask examTask = this.examTaskRepository.get(changeExamTaskCommand.getId()).get();
        examTask.changeExamTaskInfo(changeExamTaskCommand.getName(), changeExamTaskCommand.getRemark());
        examTask.changeThemeInfo(changeExamTaskCommand.getThemeId(), examTask.getThemeInfo().getName());
        examTask.settingExamTaskDateInfo(changeExamTaskCommand.getExamStartTime(), changeExamTaskCommand.getExamEndTime());
        examTask.changeExamTaskChargeUserInfo(changeExamTaskCommand.getChargeUserId());
        examTask.changeExamPaper(changeExamTaskCommand.getExamPaperMode(), changeExamTaskCommand.getExamPaperIds());
        examTask.settingExamTaskTimedInfo(changeExamTaskCommand.getTimed(), changeExamTaskCommand.getExamDuration(), changeExamTaskCommand.getExamDurationUnit());
        examTask.settingExamTaskRetakeInfo(changeExamTaskCommand.getRetake(), changeExamTaskCommand.getRetakeNumber());
        examTask.settingExamTaskUserNum(changeExamTaskCommand.getExamUserNumber());
        examTask.changeExamTaskType(changeExamTaskCommand.getExamTaskType());
        examTask.changeExamTaskUserInfos(changeExamTaskCommand.getExamUsers());
        this.examTaskRepository.save(examTask);
    }

    /**
     * 任务删除
     *
     * @param deleteExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTask(DeleteExamTaskCommand deleteExamTaskCommand) {
        this.deleteExamTaskValidator.verify(deleteExamTaskCommand);
        this.examTaskRepository.deleteExamTaskById(deleteExamTaskCommand.getExamTaskIds());
    }

    /**
     * 任务删除
     * 根据来源键进行删除
     *
     * @param deleteExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTaskBySourceId(DeleteExamTaskCommand deleteExamTaskCommand) {
        this.examTaskRepository.deleteExamTaskBySourceId(deleteExamTaskCommand);
    }

    /**
     * 任务发布
     *
     * @param publishedExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishedExamTask(PublishedExamTaskCommand publishedExamTaskCommand) {
        this.publishedExamTaskValidator.verify(publishedExamTaskCommand);
        ExamTask examTask = this.examTaskRepository.get(publishedExamTaskCommand.getId()).get();
        examTask.changeExamTaskStatus(ExamTaskStatusEnum.STAY_EXAM);
        examTask.settingExamTaskPublishedInfo(LocalDateTime.now());
        this.examTaskRepository.save(examTask);
        //发送任务发布事件(未避免超过考试时间时发布无法通过定时任务更改状态)
        this.applicationEventPublisher.publishEvent(new ExamTaskPublishedEvent(this, examTask));
    }

    /**
     * 公共任务发布
     *
     * @param publishedExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void publishedPublicExamTask(PublishedExamTaskCommand publishedExamTaskCommand) {
        this.publishedPublicExamTaskValidator.verify(publishedExamTaskCommand);
        ExamTask examTask = this.examTaskRepository.get(publishedExamTaskCommand.getId()).get();
        examTask.changeExamTaskStatus(ExamTaskStatusEnum.STAY_EXAM);
        examTask.settingExamTaskPublishedInfo(LocalDateTime.now());
        this.examTaskRepository.save(examTask);
        //发送任务发布事件(未避免超过考试时间时发布无法通过定时任务更改状态)
        this.applicationEventPublisher.publishEvent(new ExamTaskPublishedEvent(this, examTask));
    }

    /**
     * 任务撤回
     *
     * @param withdrawExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void withdrawExamTask(WithdrawExamTaskCommand withdrawExamTaskCommand) {
        this.withdrawExamTaskValidator.verify(withdrawExamTaskCommand);
        ExamTask examTask = this.examTaskRepository.get(withdrawExamTaskCommand.getId()).get();
        examTask.changeExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED);
        examTask.resetExamTaskPublishedInfo();
        this.examTaskRepository.save(examTask);
        //发送任务撤回事件
        this.applicationEventPublisher.publishEvent(new ExamTaskWithdrawEvent(this, examTask));
    }
}
