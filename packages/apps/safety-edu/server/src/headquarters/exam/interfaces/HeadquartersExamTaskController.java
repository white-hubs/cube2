package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.HeadquartersExamTaskCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.ChangeHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.DeleteHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.IncreaseHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.PublishedHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command.WithdrawHeadquartersExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.HeadquartersExamTaskQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query.HeadquartersExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.mq.message.HeadquartersExamTaskPublishedMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.HeadquartersExamTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskOperationEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;

/**
 * 总部考试任务管理接口
 *
 * @author lanchun
 * @date 2024/4/9 18:28
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskController {

    private final HeadquartersExamTaskCommandService headquartersExamTaskCommandService;

    private final HeadquartersExamTaskQueryService headquartersExamTaskQueryService;

    private final HeadquartersExamTaskPaperService headquartersExamTaskPaperService;

    /**
     * 考试任务新增
     *
     * @param increaseExamTaskCommand
     */
    @PostMapping("/headquarters/exam-task")
    public void increaseExamTask(@Valid @RequestBody IncreaseHeadquartersExamTaskCommand increaseExamTaskCommand) {
        this.headquartersExamTaskCommandService.increaseExamTask(increaseExamTaskCommand);
    }

    /**
     * 考试任务编辑
     *
     * @param id
     * @param changeExamTaskCommand
     */
    @PutMapping("/headquarters/exam-task/{id}")
    public void changeExamTask(@PathVariable("id") Long id, @Valid @RequestBody ChangeHeadquartersExamTaskCommand changeExamTaskCommand) {
        changeExamTaskCommand.setId(id);
        this.headquartersExamTaskCommandService.changeExamTask(changeExamTaskCommand);
    }

    /**
     * 考试任务详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/headquarters/exam-task/{id}")
    public HeadquartersExamTaskVO getExamTask(@PathVariable("id") Long id) {
        return this.headquartersExamTaskQueryService.getExamTask(id);
    }

    /**
     * 考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/headquarters/exam-task/page")
    public Pagination<HeadquartersExamTaskVO> getExamTaskPage(@Valid HeadquartersExamTaskQuery query) {
        return this.headquartersExamTaskQueryService.getExamTaskPage(query);
    }

    /**
     * 考试任务删除
     *
     * @param id
     */
    @DeleteMapping("/headquarters/exam-task/{id}")
    public void deleteExamTaskById(@PathVariable("id") Long id) {
        this.headquartersExamTaskCommandService.deleteExamTask(new DeleteHeadquartersExamTaskCommand(Collections.singleton(id)));
    }

    /**
     * 考试任务发布
     *
     * @param id
     */
    @PutMapping("/headquarters/exam-task/published/{id}")
    public void examTaskPublished(@PathVariable("id") Long id) {
        HeadquartersExamTask headquartersExamTask = this.headquartersExamTaskCommandService.publishedExamTask(new PublishedHeadquartersExamTaskCommand(id));
        HeadquartersExamTaskPublishedMessage message = new HeadquartersExamTaskPublishedMessage();
        message.setId(headquartersExamTask.getId());
        message.setExamTaskContent(JsonUtils.serialize(headquartersExamTask));
        message.setOperationType(ExamTaskOperationEnum.PUBLISHED);
        RocketMQContextHolder.send(HeadquartersExamTaskPublishedMessage.TOPIC, message);
    }

    /**
     * 考试任务撤回
     *
     * @param id
     */
    @PutMapping("/headquarters/exam-task/withdraw/{id}")
    public void examTaskWithdraw(@PathVariable("id") Long id) {
        HeadquartersExamTask headquartersExamTask = this.headquartersExamTaskCommandService.withdrawExamTask(new WithdrawHeadquartersExamTaskCommand(id));
        HeadquartersExamTaskPublishedMessage message = new HeadquartersExamTaskPublishedMessage();
        message.setExamTaskContent(JsonUtils.serialize(headquartersExamTask));
        message.setOperationType(ExamTaskOperationEnum.WITHDRAW);
        RocketMQContextHolder.send(HeadquartersExamTaskPublishedMessage.TOPIC, message);
    }

    /**
     * 获取考试试卷详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/headquarters/exam-task/{id}/paper/{examPaperId}")
    public ExamPaperVO getExamTaskPaper(@PathVariable("id") Long id, @PathVariable("examPaperId") Long examPaperId) {
        return this.headquartersExamTaskPaperService.getExamTaskPaperVO(id, examPaperId);
    }
}
