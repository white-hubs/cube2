package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.ChangeHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.DeleteHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.IncreaseHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.PublishedHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.WithdrawHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator.ChangeHeadquartersExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator.DeleteHeadquartersExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator.PublishedHeadquartersExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.validator.WithdrawHeadquartersExamTaskValidator;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.event.HeadquartersExamTaskPublishedEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.event.HeadquartersExamTaskWithdrawEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.factory.HeadquartersExamTaskFactory;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.HeadquartersExamTaskRepository;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskCommandService {

    private final HeadquartersExamTaskRepository headquartersExamTaskRepository;

    private final ChangeHeadquartersExamTaskValidator changeHeadquartersExamTaskValidator;

    private final PublishedHeadquartersExamTaskValidator publishedHeadquartersExamTaskValidator;

    private final WithdrawHeadquartersExamTaskValidator withdrawHeadquartersExamTaskValidator;

    private final DeleteHeadquartersExamTaskValidator deleteHeadquartersExamTaskValidator;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 任务新增
     *
     * @param increaseExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseExamTask(IncreaseHeadquartersExamTaskCommand increaseExamTaskCommand) {
        HeadquartersExamTask examTask = HeadquartersExamTaskFactory.newExamTask(increaseExamTaskCommand);
        this.headquartersExamTaskRepository.save(examTask);
    }

    /**
     * 任务编辑
     *
     * @param changeExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeExamTask(ChangeHeadquartersExamTaskCommand changeExamTaskCommand) {
        //编辑校验
        this.changeHeadquartersExamTaskValidator.verify(changeExamTaskCommand);
        HeadquartersExamTask examTask = this.headquartersExamTaskRepository.get(changeExamTaskCommand.getId()).get();
        examTask.changeExamTaskInfo(changeExamTaskCommand.getName(), changeExamTaskCommand.getRemark());
        examTask.changeThemeInfo(changeExamTaskCommand.getThemeId());
        examTask.changeExamTaskChargeUserInfo(changeExamTaskCommand.getChargeUserId());
        examTask.settingExamTaskDateInfo(changeExamTaskCommand.getExamStartTime(), changeExamTaskCommand.getExamEndTime());
        examTask.changeExamTaskType(changeExamTaskCommand.getExamTaskType());
        examTask.changeExamPaper(changeExamTaskCommand.getExamPaperIds());
        examTask.settingExamTaskTimedInfo(changeExamTaskCommand.getTimed(), changeExamTaskCommand.getExamDuration(), changeExamTaskCommand.getExamDurationUnit());
        examTask.settingExamTaskRetakeInfo(changeExamTaskCommand.getRetake(), changeExamTaskCommand.getRetakeNumber());
        examTask.settingExamTaskUserNum(changeExamTaskCommand.getExamUserNumber());
        examTask.changeExamTaskMarketInfos(changeExamTaskCommand.getExamTaskMarkets());
        this.headquartersExamTaskRepository.save(examTask);
    }

    /**
     * 任务删除
     *
     * @param deleteExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamTask(DeleteHeadquartersExamTaskCommand deleteExamTaskCommand) {
        this.deleteHeadquartersExamTaskValidator.verify(deleteExamTaskCommand);
        this.headquartersExamTaskRepository.deleteExamTaskById(deleteExamTaskCommand.getExamTaskIds());
    }

    /**
     * 任务发布
     *
     * @param publishedExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public HeadquartersExamTask publishedExamTask(PublishedHeadquartersExamTaskCommand publishedExamTaskCommand) {
        this.publishedHeadquartersExamTaskValidator.verify(publishedExamTaskCommand);
        HeadquartersExamTask examTask = this.headquartersExamTaskRepository.get(publishedExamTaskCommand.getId()).get();
        examTask.changeExamTaskStatus(ExamTaskStatusEnum.STAY_EXAM);
        examTask.settingExamTaskPublishedInfo(LocalDateTime.now());
        this.headquartersExamTaskRepository.save(examTask);
        //发送任务发布事件
        this.applicationEventPublisher.publishEvent(new HeadquartersExamTaskPublishedEvent(this, examTask));
        return examTask;
    }

    /**
     * 任务撤回
     *
     * @param withdrawExamTaskCommand
     */
    @Transactional(rollbackFor = Exception.class)
    public HeadquartersExamTask withdrawExamTask(WithdrawHeadquartersExamTaskCommand withdrawExamTaskCommand) {
        this.withdrawHeadquartersExamTaskValidator.verify(withdrawExamTaskCommand);
        HeadquartersExamTask examTask = this.headquartersExamTaskRepository.get(withdrawExamTaskCommand.getId()).get();
        examTask.changeExamTaskStatus(ExamTaskStatusEnum.UNPUBLISHED);
        examTask.resetExamTaskPublishedInfo();
        this.headquartersExamTaskRepository.save(examTask);
        //发送任务撤回事件
        this.applicationEventPublisher.publishEvent(new HeadquartersExamTaskWithdrawEvent(this, examTask));
        return examTask;
    }
}
