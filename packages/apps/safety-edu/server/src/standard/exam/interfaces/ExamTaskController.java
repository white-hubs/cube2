package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.ExamTaskCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.PublishedExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.WithdrawExamTaskCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskStandardVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

/**
 * 考试任务管理接口
 *
 * @author lanchun
 * @date 2023/4/19 19:07
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskController {

    private final ExamTaskCommandService examTaskCommandService;

    private final ExamTaskQueryService examTaskQueryService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    /**
     * 考试任务新增
     *
     * @param increaseExamTaskCommand
     */
    @PreAuthorize("hasScope('safety_education')")
    @PostMapping("/exam-task")
    public void increaseExamTask(@Valid @RequestBody IncreaseExamTaskCommand increaseExamTaskCommand) {
        this.examTaskCommandService.increaseExamTask(increaseExamTaskCommand);
    }

    /**
     * 考试任务编辑
     *
     * @param id
     * @param changeExamTaskCommand
     */
    @PutMapping("/exam-task/{id}")
    public void changeExamTask(@PathVariable("id") Long id, @Valid @RequestBody ChangeExamTaskCommand changeExamTaskCommand) {
        changeExamTaskCommand.setId(id);
        this.examTaskCommandService.changeExamTask(changeExamTaskCommand);
    }

    /**
     * 考试任务详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/exam-task/{id}")
    public ExamTaskVO getExamTask(@PathVariable("id") Long id) {
        return this.examTaskQueryService.getExamTask(id);
    }

    /**
     * 考试任务分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task/page")
    public Pagination<ExamTaskVO> getExamTaskPage(@Valid ExamTaskQuery query) {
        return this.examTaskQueryService.getExamTaskPage(query);
    }

    /**
     * 考试任务列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-tasks")
    public List<ExamTaskStandardVO> getExamTaskStandardList(@Valid ExamTaskQuery query) {
        return this.examTaskQueryService.getExamTaskStandardList(query);
    }

    /**
     * 考试任务删除
     *
     * @param id
     */
    @DeleteMapping("/exam-task/{id}")
    public void deleteExamTaskById(@PathVariable("id") Long id) {
        this.examTaskCommandService.deleteExamTask(new DeleteExamTaskCommand(Collections.singleton(id)));
    }

    /**
     * 考试任务发布
     *
     * @param id
     */
    @PutMapping("/exam-task/published/{id}")
    public void examTaskPublished(@PathVariable("id") Long id) {
        this.examTaskCommandService.publishedExamTask(new PublishedExamTaskCommand(id));
    }

    /**
     * 考试任务撤回
     *
     * @param id
     */
    @PutMapping("/exam-task/withdraw/{id}")
    public void examTaskWithdraw(@PathVariable("id") Long id) {
        this.examTaskCommandService.withdrawExamTask(new WithdrawExamTaskCommand(id));
    }

    /**
     * 获取考试试卷详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/exam-task/{id}/paper/{examPaperId}")
    public ExamPaperVO getExamTaskPaper(@PathVariable("id") Long id, @PathVariable("examPaperId") Long examPaperId) {
        return this.examTaskPaperQueryService.getExamTaskPaperVO(id, examPaperId);
    }

}
